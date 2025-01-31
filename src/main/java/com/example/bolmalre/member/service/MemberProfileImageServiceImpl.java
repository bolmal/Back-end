package com.example.bolmalre.member.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus;
import com.example.bolmalre.common.apiPayLoad.exception.handler.ImageHandler;
import com.example.bolmalre.common.apiPayLoad.exception.handler.MemberHandler;
import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.domain.MemberProfileImage;
import com.example.bolmalre.member.infrastructure.MemberProfileImageRepository;
import com.example.bolmalre.member.infrastructure.MemberRepository;
import com.example.bolmalre.member.web.port.MemberProfileImageService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MemberProfileImageServiceImpl implements MemberProfileImageService {


    private final MemberRepository memberRepository;
    private final MemberProfileImageRepository memberProfileImageRepository;

    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    /**
     * 이미지를 업로드하는 메서드입니다
     *
     * 사용하시는 옵션에 맞게 수정해서 사용해주시면 될 것 같습니다
     *
     * 추가로 여러장을 받도록 구현해놓은 이유는 나중에 여러장 받는 메서드를 구현할때 활용도를 높이기 위해서 그렇게 구현하였습니다. 참고해주세요!
     * */
        @Override
        public List<String> uploadImages(List<MultipartFile> files, String dirName, String username) {

            List<MemberProfileImage> images = new ArrayList<>();

            if (files.size() > 1) {
                throw new MemberHandler(ErrorStatus.MEMBER_IMAGE_COUNT_ERROR);
            }

            Member memberByUsername = getMemberByUsername(username);
            List<MemberProfileImage> memberProfileImages = memberByUsername.getMemberProfileImages();

            if (!memberProfileImages.isEmpty()) {
                throw new MemberHandler(ErrorStatus.MEMBER_IMAGE_EXIST);
            }

            try {
                images = files.parallelStream()
                        .map(file -> uploadImage(dirName, file,memberByUsername))
                        .collect(Collectors.toList());

                memberByUsername.setMemberProfileImages(images);

                memberProfileImageRepository.saveAll(images);

            } catch (Exception e) {
                throw new ImageHandler(ErrorStatus.IMAGE_UPLOAD_ERROR);
            }

            return images.stream()
                    .map(MemberProfileImage::getImageLink)
                    .collect(Collectors.toList());
        }


    @Override
    @Transactional
    public void deleteImage(String username) throws FileNotFoundException {

        // username 에 해당하는 이미지 리스트 조회
        List<String> findImagesByMealDiary = findImagesByUsername(username);

        Member memberByUsername = getMemberByUsername(username);
        List<MemberProfileImage> byMemberEntity = memberProfileImageRepository.findByMember(memberByUsername);

        if (findImagesByMealDiary.isEmpty()) {
            throw new ImageHandler(ErrorStatus.IMAGE_NOT_FOUND);
        }

        // S3에서 이미지 삭제 및 데이터베이스 레코드 삭제
        for (MemberProfileImage memberProfileImage : byMemberEntity) {
            try {
                // S3에서 이미지 삭제
                amazonS3.deleteObject(bucket, memberProfileImage.getFileName());


                /**
                 *
                 * JPA의 영속성 컨텍스트안에 삭제해야하는 엔티티와 연관된 엔티티가 존재함
                 * 그렇기 때문에 삭제 쿼리가 발생하지 않는 오류 발견
                 *
                 * -> 엔티티 간의 연관관계를 끊어줌
                 *
                 * */
                memberProfileImage.getMember().removeMemberProfileImage(memberProfileImage);
                memberProfileImage.removeMember();
                memberProfileImageRepository.save(memberProfileImage);



                // 데이터베이스에서 이미지 레코드 삭제
                memberProfileImageRepository.delete(memberProfileImage);
            } catch (Exception e) {
                throw new ImageHandler(ErrorStatus.IMAGE_REMOVE_ERROR);
            }

            memberRepository.save(memberByUsername);
        }
    }


    /**
     * 단일 이미지를 조회하는 메서드입니다
     * 필요하실까봐 만들어놓았습니다!
     *
     * 사용하시는 옵션에 맞게 수정해서 사용해주시면 될 것 같습니다
     * */
    @Override
    public String findImageByFileName(String fileName) {

        MemberProfileImage image = memberProfileImageRepository.findByFileName(fileName)
                .orElseThrow(() -> new ImageHandler(ErrorStatus.IMAGE_NOT_FOUND));

        return image.getImageLink();
    }


    /**
     * 매핑되어있는 정보를 통해 이미지를 찾는 메서드입니다
     *
     * 사용하시는 옵션에 맞게 수정해서 사용해주시면 될 것 같습니다
     * */
    @Override
    public List<String> findImagesByUsername(String username) {

        Member memberByUsername = getMemberByUsername(username);

        return memberByUsername.getMemberProfileImages().stream()
                .map(MemberProfileImage::getImageLink)
                .collect(Collectors.toList());
    }

    /**
     * 이미지를 업로드하는 메서드입니다
     *
     * 사용하시는 옵션에 맞게 수정해서 사용해주시면 될 것 같습니다
     * */
    public MemberProfileImage uploadImage(String dirName, MultipartFile file, Member member) {

        String fileName = dirName + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        String originalFileName = file.getOriginalFilename();

        try {
            log.info("Uploading image: {}");

            amazonS3.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata));
            log.info("Image uploaded successfully: {}");
        } catch (AmazonServiceException e) {
            throw new ImageHandler(ErrorStatus.IMAGE_UPLOAD_ERROR);
        } catch (SdkClientException e) {
            throw new RuntimeException("S3 client error: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Image upload error: " + e.getMessage(), e);
        }

        String imagePath = amazonS3.getUrl(bucket, fileName).toString();

        return MemberProfileImage.builder()
                .imageLink(imagePath)
                .fileName(fileName)
                .imageName(originalFileName)
                .member(member)
                .build();
    }














    // 회원 찾는 메서드
    public Member getMemberByUsername(String username){
        return memberRepository.findByUsername(username)
                .orElseThrow(()-> new IllegalArgumentException("test"));
    }
}