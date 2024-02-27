package com.example.otp.service;

import com.example.otp.entity.Otp;
import com.example.otp.entity.User;
import com.example.otp.repository.OtpRepository;
import com.example.otp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final OtpRepository otpRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void addUser(User user) {
        user.setPassword(passwordEncoded(user));
        userRepository.save(user);
    }

    @Override
    public void auth(User user) {
        User findUser = userRepository.findUserByUsername(user.getUsername()).orElseThrow(() -> badCredentials().get());
        log.info("findUser = {}", findUser.getUsername());
        log.info("findUser = {}", findUser.getPassword());
        if (passwordEncoder.matches(user.getPassword(), findUser.getPassword())) {
            renewOtp(findUser);
        } else {
            throw badCredentials().get();
        }
    }

    @Override
    public boolean check(Otp otpToValidate) {
        Optional<Otp> findOtp = otpRepository.findOtpByUsername(otpToValidate.getUsername());
        if (findOtp.isPresent()) {
            Otp otp = findOtp.get();
            return otpToValidate.getCode().equals(otp.getCode());
        }
        return false;
    }

    private Supplier<BadCredentialsException> badCredentials() {
        throw new BadCredentialsException("not found user. checked username or password");
    }

    private String passwordEncoded(User user) {
        return passwordEncoder.encode(user.getPassword());
    }

    private void renewOtp(User findUser) {
        String code = GenerateCodeUtil.generateCode();
        Optional<Otp> findOtp = otpRepository.findOtpByUsername(findUser.getUsername());

        if (findOtp.isPresent()) {
            Otp otp = findOtp.get();
            otp.setCode(code);
        } else {
            Otp otp = Otp.builder()
                    .username(findUser.getUsername())
                    .code(code)
                    .build();
            otpRepository.save(otp);
        }

    }

    private static final class GenerateCodeUtil {
        public GenerateCodeUtil() {
        }

        private static String generateCode() {
            String code = null;
            try {
                SecureRandom random = SecureRandom.getInstanceStrong(); //  난수 생성 방식이 예측하기 어려워서 보안적으로 유리하다.
                int c = random.nextInt(9000) + 1000;
                code = String.valueOf(c);
            } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                log.error("noSuchAlgorithmException = ", noSuchAlgorithmException);
            }
            return code;
        }
    }

}
