package com.allo.server.domain.user.repository;

import com.allo.server.domain.user.entity.SocialType;
import com.allo.server.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findBySocialTypeAndSocialId(SocialType socialType, String socialId);
    Boolean existsByEmail(String email);
    Boolean existsByNickname(String nickname);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByNickname(String nickname);
}
