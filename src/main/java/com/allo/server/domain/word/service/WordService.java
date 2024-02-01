package com.allo.server.domain.word.service;

import com.allo.server.domain.user.entity.Language;
import com.allo.server.domain.user.entity.UserEntity;
import com.allo.server.domain.user.repository.UserRepository;
import com.allo.server.domain.word.dto.request.WordSaveRequest;
import com.allo.server.domain.word.dto.response.WordGetResponse;
import com.allo.server.domain.word.dto.response.WordSearchResponse;
import com.allo.server.domain.word.entity.Word;
import com.allo.server.domain.word.openapi.GetExampleRequest;
import com.allo.server.domain.word.openapi.GetExampleResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));
        Optional<Word> existWord = wordRepository.findByUserEntityAndWord(userEntity, word);

        if (existWord.isPresent()) {
            WordSearchResponse wordSearchResponse = new WordSearchResponse(existWord.get().getWord(), existWord.get().getMeaning(), existWord.get().getPos(), existWord.get().getTrans_word(), existWord.get().getExample(), Boolean.TRUE);
            return wordSearchResponse;
        }
        else {
            Language language = userEntity.getLanguage();

            String baseUrl = "https://krdict.korean.go.kr/api/search";

            String encodedWord = URLEncoder.encode(word, StandardCharsets.UTF_8);
            GetMeanRequest getMeanRequest = new GetMeanRequest(key, encodedWord, language);
            StringBuilder meanResult = new StringBuilder();

            // URL 설정
            URL meanUrl = new URL(baseUrl + getMeanRequest.getParameter());
            HttpURLConnection meanUrlConnection = (HttpURLConnection) meanUrl.openConnection();

            // 접속
            meanUrlConnection.setRequestMethod("GET");
            meanUrlConnection.connect();

            BufferedInputStream meanBufferedInputStream = new BufferedInputStream(meanUrlConnection.getInputStream());
            BufferedReader meanBufferedReader = new BufferedReader(new InputStreamReader(meanBufferedInputStream, StandardCharsets.UTF_8));
            String meanReturnLine;
            while ((meanReturnLine = meanBufferedReader.readLine()) != null) {
                meanResult.append(meanReturnLine);
            }

            // JSON으로 변환
            JSONObject meanJsonObject = XML.toJSONObject(meanResult.toString());

            ObjectMapper meanMapper = new ObjectMapper().configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
            meanMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            meanMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

            // GetExampleResponse에 매핑
            GetMeanResponse getMeanResponse = meanMapper.readValue(meanJsonObject.toString(), GetMeanResponse.class);
            System.out.println(getMeanResponse);

            if (getMeanResponse.getChannel().getItem().isEmpty())
                throw new BadRequestException(UNKNOWN_WORD);

            GetExampleRequest getExampleRequest = new GetExampleRequest(key, encodedWord);
            StringBuilder exampleResult = new StringBuilder();

            // URL 설정
            URL exampleUrl = new URL(baseUrl + getExampleRequest.getParameter());
            HttpURLConnection exampleUrlConnection = (HttpURLConnection) exampleUrl.openConnection();

            // 접속
            exampleUrlConnection.setRequestMethod("GET");
            exampleUrlConnection.connect();

            BufferedInputStream exampleBufferedInputStream = new BufferedInputStream(exampleUrlConnection.getInputStream());
            BufferedReader exmapleBufferedReader = new BufferedReader(new InputStreamReader(exampleBufferedInputStream, StandardCharsets.UTF_8));
            String exampleReturnLine;
            while ((exampleReturnLine = exmapleBufferedReader.readLine()) != null) {
                exampleResult.append(exampleReturnLine);
            }

            // JSON으로 변환
            JSONObject exampleJsonObject = XML.toJSONObject(exampleResult.toString());

            ObjectMapper exampleMapper = new ObjectMapper().configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
            exampleMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            exampleMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

            // GetMeanResponse에 매핑
            GetExampleResponse getExampleResponse = exampleMapper.readValue(exampleJsonObject.toString(), GetExampleResponse.class);
            System.out.println(getExampleResponse);

            if (getExampleResponse.getChannel().getItem().isEmpty())
                throw new BadRequestException(UNKNOWN_WORD);

            // 단어 정보
            String meaning = getMeanResponse.getChannel().getItem().get(0).getSense().get(0).getDefinition();
            String pos = getMeanResponse.getChannel().getItem().get(0).getPos();
            String trans_word = getMeanResponse.getChannel().getItem().get(0).getSense().get(0).getTranslation().get(0).getTrans_word();
            String example = getExampleResponse.getChannel().getItem().get(0).getExample();

            WordSearchResponse wordSearchResponse = new WordSearchResponse(word, meaning, pos, trans_word, example, Boolean.FALSE);

            return wordSearchResponse;
        }
    }

    public void saveWord(String email, WordSaveRequest wordSaveRequest){

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));

        Optional<Word> existWord = wordRepository.findByUserEntityAndWord(userEntity, wordSaveRequest.word());
        if (existWord.isPresent()) {
            throw new BadRequestException(ALREADY_EXIST_WORD);
        }
        else {
            Word saveWord = WordSaveRequest.wordToEntity(userEntity, wordSaveRequest);
            wordRepository.save(saveWord);
        }
    }

    public Page<WordGetResponse> getWord(String email, Pageable pageable){

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));

        int pageLoc = pageable.getPageNumber() - 1; // page 위치에 있는 값은 0부터 시작한다.
        int pageLimit = 3; // 한페이지에 보여줄 글 개수

        PageRequest pageRequest = PageRequest.of(pageLoc, pageLimit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Word> pages = wordRepository.findAllByUserEntity(userEntity, pageRequest);
        Page<WordGetResponse> wordGetResponses = pages.map(
                page -> new WordGetResponse(page.getWord(), page.getMeaning(), page.getPos(), page.getTrans_word(), page.getExample()));


        return wordGetResponses;
    }


}
