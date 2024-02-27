package com.example.otp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @Column(name = "username")
    private String username;

    @Setter
    @Column(name = "password")
    private String password;

    @Builder
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
