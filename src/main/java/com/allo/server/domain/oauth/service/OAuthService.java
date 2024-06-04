package com.allo.server.domain.oauth.service;

import com.allo.server.domain.oauth.dto.request.SocialLoginCodeRequest;
import com.allo.server.domain.oauth.dto.request.SocialLoginRequest;
import com.allo.server.domain.oauth.dto.response.LoginResponse;
import com.allo.server.domain.oauth.dto.response.OAuthInfoResponse;
import com.allo.server.domain.user.entity.Role;
import com.allo.server.domain.user.entity.SocialType;
import com.allo.server.domain.user.entity.UserEntity;
import com.allo.server.domain.user.repository.UserRepository;
import com.allo.server.error.exception.custom.BadRequestException;
import com.allo.server.jwt.service.JwtService;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;


import static com.allo.server.error.ErrorCode.USER_NOT_FOUND;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OAuthService {
    private final UserRepository userRepository;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final JwtService jwtService;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KAKAO_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String KAKAO_CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URI;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String NAVER_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String NAVER_CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String NAVER_REDIRECT_URI;

    public String getAccessToken (SocialLoginCodeRequest codeRequest) {

        String access_Token = "";
        String refresh_Token = "";
        String client_id = "";
        String client_secret = "";
        String redirect_uri = "";
        String reqURL = "";
        String authorize_code = codeRequest.code();

        if (codeRequest.provider().equals(SocialType.KAKAO)) {
            client_id = KAKAO_CLIENT_ID;
            client_secret = KAKAO_CLIENT_SECRET;
            redirect_uri = KAKAO_REDIRECT_URI;
            reqURL = "https://kauth.kakao.com/oauth/token";
        }
        else if (codeRequest.provider().equals(SocialType.NAVER)) {
            client_id = NAVER_CLIENT_ID;
            client_secret = NAVER_CLIENT_SECRET;
            redirect_uri = NAVER_REDIRECT_URI;
            reqURL = "https://nid.naver.com/oauth2.0/token";
        }

        try {
            URL url = new URL(reqURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // POST 요청을 위해 기본값이 false인 setDoOutput을 true로

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            // POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송

            // Content-Type 헤더 추가
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");

            sb.append("&client_secret=" + client_secret); //본인이 발급받은 key
            sb.append("&client_id=" + client_id); //본인이 발급받은 key
            sb.append("&redirect_uri=" + redirect_uri); // 본인이 설정한 주소

            if (codeRequest.provider().equals(SocialType.NAVER)) {
                sb.append("&state=null");
            }

            sb.append("&code=" + authorize_code);
            bw.write(sb.toString());
            bw.flush();

            // 결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            // Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            System.out.println("access_token : " + access_Token);
            System.out.println("refresh_token : " + refresh_Token);

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return access_Token;
    }

    public LoginResponse userSocialLogin(SocialLoginRequest request) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(request);
        SocialType socialType = oAuthInfoResponse.getSocialType();
        String socialId = oAuthInfoResponse.getId();
        String socialEmail = oAuthInfoResponse.getEmail();

        Long id = findOrSaveUser(socialType, socialId, socialEmail); // User id 반환

        String role = String.valueOf(userRepository.findById(id)
                .map(UserEntity::getRole)
                .orElseThrow(() -> new BadRequestException(USER_NOT_FOUND)));

        String roleName = "USER";
        String accessToken = jwtService.createAccessToken(id, roleName);
        String refreshToken = jwtService.createRefreshToken(id, roleName);
//        jwtService.updateRefreshToken(roleName, id, refreshToken);

        return LoginResponse.of(role, accessToken, refreshToken);
    }

    private Long findOrSaveUser(SocialType socialType, String socialId, String email) {

        Optional<UserEntity> userEntity = userRepository.findBySocialTypeAndSocialId(socialType, socialId);

        if (userEntity.isPresent()) {
            return userEntity.get().getUserId();
        } else {
            return saveUser(socialType, socialId, email);
        }
    }

    private Long saveUser(SocialType socialType, String socialId, String email) {
        UserEntity userEntity = new UserEntity(email, Role.USER, socialType, socialId);

        return userRepository.save(userEntity).getUserId();
    }
}
