package com.example.otp.controller;

import com.example.otp.entity.Otp;
import com.example.otp.entity.User;
import com.example.otp.service.UserService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<Void> auth(@RequestBody User user) {
        userService.auth(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/otp/check")
    public ResponseEntity<Void> check(@RequestBody Otp otp) {

        if (userService.check(otp)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

}
