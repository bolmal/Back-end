package com.example.bolmalre.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

@Service
public interface MemberProfileImageService {

    List<String> uploadImages(List<MultipartFile> files, String dirName, String username);

    @Transactional
    void deleteImage(String username) throws FileNotFoundException;

    String findImageByFileName(String fileName);

    List<String> findImagesByUsername(String username);
}
