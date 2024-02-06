package com.allo.server.domain.word.repository.impl;

import com.allo.server.domain.word.dto.response.WordGetResponse;
import com.allo.server.domain.word.repository.CustomWordRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.allo.server.domain.word.entity.QWord.word1;


@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomWordRepositoryImpl implements CustomWordRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<WordGetResponse> getWords(Long userId, Pageable pageable) {
        List<WordGetResponse> response = queryFactory
                .select(Projections.constructor(WordGetResponse.class, word1.word, word1.meaning, word1.pos, word1.trans_word, word1.example))
                .from(word1)
                .where(word1.userEntity.userId.eq(userId))
                .orderBy(word1.createdAt.desc())
                .offset(pageable.getOffset()) // 페이지 번호
                .limit(pageable.getPageSize()) // 페이지 사이즈
                .fetch();

        return response;
    }
}