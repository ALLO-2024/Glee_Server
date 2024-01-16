package com.allo.server.domain.user;

import com.allo.server.domain.user.dto.request.SignUpReq;
import com.allo.server.domain.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    @PostMapping("/login")
    public ResponseEntity<String> loginSuccess(@RequestBody Map<String, String> loginForm) {
        String token = service.login(loginForm.get("username"), loginForm.get("password"));
        return ResponseEntity.ok(token);
    }

    @PostMapping("/signup")
    public Long signup(@RequestBody SignUpReq signUpReq) {
        return service.signup(signUpReq);
    }

//    @GetMapping("/signup/check/{email}/exists")
//    public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable String email) {
//        return ResponseEntity.ok(service.checkEmailExists(email));
//    }

}
