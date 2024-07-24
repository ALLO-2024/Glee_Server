package com.allo.server.domain.oauth.provider;

import com.allo.server.error.exception.custom.BadRequestException;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.allo.server.error.ErrorCode.FAIL_OAUTH_TOKEN;

@Component
public class NaverProvider implements OAuthProvider {
    private static final String TOKEN_URL = "https://nid.naver.com/oauth2.0/token";

    @Override
    public String getAccessToken(String clientId, String clientSecret, String redirectUri, String code) {
        return requestToken(TOKEN_URL, clientId, clientSecret, redirectUri, code);
    }

    private String requestToken(String reqURL, String clientId, String clientSecret, String redirectUri, String code) {
        try {
            URL url = new URL(reqURL);

            // POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Content-Type 헤더 추가
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");

            sb.append("&client_secret=" + clientSecret); //본인이 발급받은 key
            sb.append("&client_id=" + clientId); //본인이 발급받은 key
            sb.append("&redirect_uri=" + redirectUri); // 본인이 설정한 주소
            sb.append("&state=null");
            sb.append("&code=" + code);


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

            String access_Token = element.getAsJsonObject().get("access_token").getAsString();
            String refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            System.out.println("access_token : " + access_Token);
            System.out.println("refresh_token : " + refresh_Token);

            return element.getAsJsonObject().get("access_token").getAsString();

        } catch (IOException e) {
            throw new BadRequestException(FAIL_OAUTH_TOKEN);
        }
    }
}