package com.allo.server.domain.word.controller;

import com.allo.server.domain.word.dto.request.WordSaveRequest;
import com.allo.server.domain.word.dto.response.WordSearchResponse;
import com.allo.server.domain.word.service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/words")
public class WordController {

    private final WordService wordService;

    @GetMapping("/search/{word}")
    public ResponseEntity<WordSearchResponse> searchWord(@AuthenticationPrincipal UserDetails loginUser,
                                            @PathVariable String word) throws IOException {

        WordSearchResponse response = wordService.searchWord(loginUser.getUsername(), word);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/save")
    public ResponseEntity<Void> saveWord(@AuthenticationPrincipal UserDetails loginUser,
                                           @RequestPart(value = "WordSaveRequest") WordSaveRequest wordSaveRequest) throws IOException {

        wordService.saveWord(loginUser.getUsername(), wordSaveRequest);
        return ResponseEntity.noContent().build();
    }
}
