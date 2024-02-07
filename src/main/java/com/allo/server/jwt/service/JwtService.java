package com.allo.server.jwt.service;

import com.allo.server.domain.oauth.dto.response.LoginResponse;
import com.allo.server.domain.user.entity.UserEntity;
import com.allo.server.domain.user.repository.UserRepository;
import com.allo.server.error.exception.custom.BadRequestException;
import com.allo.server.error.exception.custom.TokenException;
import com.allo.server.util.PasswordUtil;
import com.allo.server.util.RedisUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.allo.server.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtService {

    // 프로퍼티 주입 부분
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Integer accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Integer refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String ID_CLAIM = "id";
    private static final String ROLE_CLAIM = "roleName";
    private static final String BEARER = "Bearer ";
    private final UserRepository userRepository;
    private final RedisUtil redisUtil;
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();


    /**
     * AccessToken 생성 메소드
     */
    public String createAccessToken(Long id, String roleName) {
        Date now = new Date();

        try {
            return JWT.create()
                    .withSubject(ACCESS_TOKEN_SUBJECT)
                    .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod))
                    .withClaim(ID_CLAIM, id)
                    .withClaim(ROLE_CLAIM, roleName)
                    .sign(Algorithm.HMAC512(secretKey));
        } catch (Exception e) {
            log.error("토큰 생성에 실패했습니다.");
            throw new TokenException(TOKEN_NOT_CREATED);
        }
    }

    /**
     * RefreshToken 생성 메소드
     */
    public String createRefreshToken(Long id, String roleName) {
        Date now = new Date();

        try {
            return JWT.create()
                    .withSubject(REFRESH_TOKEN_SUBJECT)
                    .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
                    .withClaim(ID_CLAIM, id)
                    .withClaim(ROLE_CLAIM, roleName)
                    .sign(Algorithm.HMAC512(secretKey));
        } catch (Exception e) {
            log.error("토큰 생성에 실패했습니다.");
            throw new TokenException(TOKEN_NOT_CREATED);
        }
    }

    /**
     * 로그인 시 AccessToken + RefreshToken 바디에 실어서 보내기
     */
    public Map<String, String> sendAccessAndRefreshToken(String accessToken, String refreshToken) {
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }


    /**
     * 클라이언트 요청 헤더에서 RefreshToken 추출
     */
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER)) // Bearer 제외 순수 토큰만 추출
                .map(refreshToken -> refreshToken.replace(BEARER, "")); // 헤더를 가져온 후 "Bearer"를 삭제(""로 replace)
    }

    /**
     * 클라이언트 요청 헤더에서 AccessToken 추출
     */
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    /**
     * AccessToken에서 Id 추출 -> 유효하지 않다면 빈 Optional 객체 반환
     */
    public Optional<Long> extractId(String token) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(token)
                    .getClaim(ID_CLAIM)
                    .asLong());
        } catch (Exception e) {
            log.error("토큰에서 ID_CLAIM을 추출하는 데 실패하였습니다.");
            return Optional.empty();
        }
    }

    /**
     * AccessToken에서 roleName 추출 -> 유효하지 않다면 빈 Optional 객체 반환
     */
    public Optional<String> extractRoleName(String token) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(token)
                    .getClaim(ROLE_CLAIM)
                    .asString());
        } catch (Exception e) {
            log.error("토큰에서 ROLE_CLAIM을 추출하는 데 실패하였습니다.");
            return Optional.empty();
        }
    }

    /**
     * RefreshToken 저장(업데이트)
     */
    public void updateRefreshToken(String roleName, Long id, String refreshToken) {
//        redisUtil.set(roleName, id, refreshToken, refreshTokenExpirationPeriod);
    }

    /**
     * 토큰 유효성 검사
     */
    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);

//            if(redisUtil.hasKeyBlackList(token)) {
//                throw new TokenException(ALREADY_LOGOUT_MEMBER);
//            }
            return true;
        } catch (Exception e) {
            log.info("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }

    /**
     * RefreshToken 검증 메소드
     */
    public boolean isRefreshTokenMatch(String roleName, Long id, String refreshToken) {
//        if (redisUtil.get(roleName, id).equals(refreshToken)) {
//            return true;
//        }
        throw new TokenException(INVALID_TOKEN);
    }

    /**
     * AccessToken, RefreshToken 재발급 + 인증 + 응답 바디에 보내기
     */
    public LoginResponse reIssueToken(String refreshToken) {

        Long id = extractId(refreshToken).orElseThrow(() -> new TokenException(INVALID_TOKEN));
        String roleName = extractRoleName(refreshToken).orElseThrow(() -> new TokenException(INVALID_TOKEN));

        Boolean isMatched = isRefreshTokenMatch(roleName, id, refreshToken);
        if (!isMatched) { throw new TokenException(TOKEN_NOT_MATCHED); }

        String newAccessToken = null;
        String newRefreshToken = null;
        switch (roleName) {
            case "USER":
                newAccessToken = createAccessToken(id, roleName);
                newRefreshToken = createRefreshToken(id, roleName);
                break;
            default:
                throw new BadRequestException(INVALID_ROLE_NAME); // 다른 roleName 들어왔을 경우의 예외 처리
        }

        getAuthentication(newAccessToken);
//        redisUtil.delete(roleName, id);
        updateRefreshToken(roleName, id, newRefreshToken);

        return LoginResponse.of(roleName, newAccessToken, newRefreshToken);
    }

    /**
     * [인증 처리 메소드]
     * 인증 허가 처리된 객체를 SecurityContextHolder에 담기
     */
    public void getAuthentication(String accessToken) {
        extractId(accessToken)
                .ifPresent(id -> {
            Optional<String> roleNameOpt = extractRoleName(accessToken);

            if (roleNameOpt.isPresent()) {
                String roleName = roleNameOpt.get();
                switch (roleName) {
                    case "USER":
                        userRepository.findById(id).ifPresent(this::saveUserAuthentication);
                        break;
                    default:
                        throw new BadRequestException(INVALID_ROLE_NAME); // 다른 roleName 들어왔을 경우의 예외 처리
                }
            } else {
                log.error("토큰에서 ROLE_CLAIM을 추출하는 데 실패하였습니다.");
            }
        });
    }


    /**
     * [인증 허가 메소드]
     * 파라미터의 유저 : 우리가 만든 회원 객체 / 빌더의 유저 : UserDetails의 User 객체
     */
    public void saveUserAuthentication(UserEntity userEntity) {
        String password = userEntity.getPassword();
        if (password == null) { // 소셜 로그인 유저의 비밀번호 임의로 설정 하여 소셜 로그인 유저도 인증 되도록 설정
            password = PasswordUtil.generateRandomPassword();
        }

        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
                .username(userEntity.getEmail())
                .password(password)
                .roles(userEntity.getRole().name())
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                        authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
