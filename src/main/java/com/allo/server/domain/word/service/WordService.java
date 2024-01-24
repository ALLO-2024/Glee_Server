package com.allo.server.domain.word.service;

import com.allo.server.domain.user.entity.Language;
import com.allo.server.domain.user.entity.UserEntity;
import com.allo.server.domain.user.repository.UserRepository;
import com.allo.server.domain.word.dto.request.WordSaveRequest;
import com.allo.server.domain.word.dto.response.WordSearchResponse;
import com.allo.server.domain.word.entity.Word;
import com.allo.server.domain.word.openapi.GetMeanRequest;
import com.allo.server.domain.word.openapi.GetMeanResponse;
import com.allo.server.domain.word.repository.WordRepository;
import com.allo.server.error.exception.custom.BadRequestException;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static com.allo.server.error.ErrorCode.*;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class WordService {

    private final WordRepository wordRepository;
    private final UserRepository userRepository;

    @Value("${korean-basic-dictionary.key}")
    private String key;

    public WordSearchResponse searchWord(String email, String word) throws IOException {

        Optional<Word> existWord = wordRepository.findByWord(word);
        if (existWord.isPresent()) {
            WordSearchResponse wordSearchResponse = new WordSearchResponse(existWord.get().getWord(), existWord.get().getMeaning(), existWord.get().getPos(), existWord.get().getTrans_word(), existWord.get().getTrans_dfn(), Boolean.TRUE);
            return wordSearchResponse;
        }
        else {
            UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));
            Language language = userEntity.getLanguage();

            String baseUrl = "https://krdict.korean.go.kr/api/search";

            String encodedWord = URLEncoder.encode(word, StandardCharsets.UTF_8);
            GetMeanRequest getMeanRequest = new GetMeanRequest(key, encodedWord, language);
            StringBuilder result = new StringBuilder();

            // URL 설정
            URL url = new URL(baseUrl + getMeanRequest.getParameter());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // 접속
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            BufferedInputStream bufferedInputStream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream, StandardCharsets.UTF_8));
            String returnLine;
            while ((returnLine = bufferedReader.readLine()) != null) {
                result.append(returnLine);
            }

            // JSON으로 변환
            JSONObject jsonObject = XML.toJSONObject(result.toString());

            ObjectMapper mapper = new ObjectMapper().configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

            // GetMeanResponse에 매핑
            GetMeanResponse getMeanResponse = mapper.readValue(jsonObject.toString(), GetMeanResponse.class);
            System.out.println(getMeanResponse);

            if (getMeanResponse.getChannel().getItem().isEmpty())
                throw new BadRequestException(UNKNOWN_WORD);

            // 단어 정보
            String meaning = getMeanResponse.getChannel().getItem().get(0).getSense().get(0).getDefinition();
            String pos = getMeanResponse.getChannel().getItem().get(0).getPos();
            String trans_word = getMeanResponse.getChannel().getItem().get(0).getSense().get(0).getTranslation().get(0).getTrans_word();
            String trans_dfn = getMeanResponse.getChannel().getItem().get(0).getSense().get(0).getTranslation().get(0).getTrans_dfn();

            WordSearchResponse wordSearchResponse = new WordSearchResponse(word, meaning, pos, trans_word, trans_dfn, Boolean.FALSE);

            return wordSearchResponse;
        }
    }

    public void saveWord(String email, WordSaveRequest wordSaveRequest){

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));

        Optional<Word> existWord = wordRepository.findByWord(wordSaveRequest.word());
        if (existWord.isPresent()) {
            throw new BadRequestException(ALREADY_EXIST_WORD);
        }
        else {
            Word saveWord = WordSaveRequest.wordToEntity(userEntity, wordSaveRequest);
            wordRepository.save(saveWord);
        }

    }
}
