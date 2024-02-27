package com.example.otp.controller;

import com.example.otp.entity.Otp;
import com.example.otp.entity.User;
import com.example.otp.service.UserService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping(path = "/user/add")
    public ResponseEntity<Void> addUser(@RequestBody User user) {
        userService.addUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/user/auth")
    public ResponseEntity<RequestBodyContainer<String>> auth(@RequestBody User user) {
        String otpCode = userService.auth(user);
        log.info("otp code = {}", otpCode);
        return new ResponseEntity<>(new RequestBodyContainer<>(otpCode), HttpStatus.OK);
    }   //  ToDO otp code 를 response 한다.

    @PostMapping(path = "/otp/check")
    public ResponseEntity<Void> check(@RequestBody Otp otp) {

        if (userService.check(otp)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RequestBodyContainer<T> {
        private T requestBodyData;

        public RequestBodyContainer(T requestBodyData) {
            this.requestBodyData = requestBodyData;
        }
    }

}
