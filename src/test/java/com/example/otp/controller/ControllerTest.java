package com.example.otp.controller;

import com.example.otp.entity.Otp;
import com.example.otp.entity.User;
import com.example.otp.repository.OtpRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Rollback(value = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OtpRepository otpRepository;

    private User user;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
        user = User.builder()
                .username("first_tester")
                .password("1q2w3e4r!")
                .build();
    }


    @Test
    @Order(1)
    void addUser() throws Exception {
        mockMvc.perform(
                        post("/user/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(2)
    void auth() throws Exception {
        mockMvc.perform(
                        post("/user/auth")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(3)
    void check_validate_happy() throws Exception {
        Otp otp = findOtpHelper();
        ResultActions perform = mockMvc.perform(
                post("/otp/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(otp))
        );
        perform.andExpect(status().isOk());
    }

    @Test
    @Order(4)
    void check_validate_sad() throws Exception {
        Otp otp = findOtpHelper();
        otp.setCode("FAIL_CODE");
        ResultActions perform = mockMvc.perform(
                post("/otp/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(otp))
        );
        perform.andExpect(status().isForbidden());

    }

    private Otp findOtpHelper() {
        Optional<Otp> findOtp = otpRepository.findOtpByUsername(user.getUsername());
        Assertions.assertNotEquals(Optional.empty(), findOtp);
        return findOtp.orElse(null);
    }

}
