package com.allo.server.domain.user.service;


import com.allo.server.domain.user.dto.request.SignUpReq;
import com.allo.server.domain.user.entity.UserEntity;
import com.allo.server.domain.user.repository.UserRepository;
import com.allo.server.global.config.security.jwt.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.allo.server.global.config.response.BaseResponseStatus.DUPLICATED_EMAIL;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private final BCryptPasswordEncoder encoder;
    private final UserRepository repository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public String login(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        // 검증
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 검증된 인증 정보로 JWT 토큰 생성
        String token = jwtTokenProvider.generateToken(authentication);

        return token;
    }

    public Long signup(SignUpReq signUpReq) {
        Optional<UserEntity> optionalUserEntity = checkEmailExists(signUpReq.getEmail());

        if (optionalUserEntity.isPresent()) {
            throw new IllegalArgumentException(DUPLICATED_EMAIL.getMessage());
        }

        String encPwd = encoder.encode(signUpReq.getPassword());

        UserEntity userEntity = repository.save(signUpReq.toEntity(encPwd));

        if(userEntity != null) {
            return userEntity.getUserId();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public Optional<UserEntity> checkEmailExists(String email) {
        return repository.findByEmail(email);
    }

}
