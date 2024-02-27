package com.example.otp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Otp {

    @Id
    @Column(name = "username")
    private String username;

    @Setter
    @Column(name = "code")
    private String code;

    @Builder
    public Otp(String username, String code) {
        this.username = username;
        this.code = code;
    }

}
