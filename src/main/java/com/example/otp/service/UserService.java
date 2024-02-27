package com.example.otp.service;

import com.example.otp.entity.Otp;
import com.example.otp.entity.User;

public interface UserService {
    void addUser(User user);

    void auth(User user);

    boolean check(Otp otpToValidate);
}
