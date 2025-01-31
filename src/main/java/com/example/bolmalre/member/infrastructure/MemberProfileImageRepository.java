package com.example.bolmalre.member.infrastructure;


import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.domain.MemberProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberProfileImageRepository extends JpaRepository<MemberProfileImage, Long> {

    List<MemberProfileImage> findByMember(Member memberEntity);

    Optional<MemberProfileImage> findByFileName(String fileName);
}
