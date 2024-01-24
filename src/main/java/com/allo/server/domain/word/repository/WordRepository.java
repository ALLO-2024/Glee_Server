package com.allo.server.domain.word.repository;

import com.allo.server.domain.user.entity.UserEntity;
import com.allo.server.domain.word.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    Optional<Word> findByUserEntityAndWord(UserEntity userEntity, String word);
}