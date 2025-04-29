package com.study.sns.service;

import com.study.sns.exception.ErrorCode;
import com.study.sns.exception.SnsApplicationException;
import com.study.sns.model.Alarm;
import com.study.sns.model.User;
import com.study.sns.model.entity.UserEntity;
import com.study.sns.repository.AlarmEntityRepository;
import com.study.sns.repository.UserEntityRepository;
import com.study.sns.util.JwtTokenUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final BCryptPasswordEncoder encoder;
    private final AlarmEntityRepository alarmEntityRepository;

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    public User loadUserByUserName(String userName) {
        return userEntityRepository.findByUserName(userName).map(User::fromEntity).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
    }

    @Transactional
    public User join(String userName, String password) {

        userEntityRepository.findByUserName(userName).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is duplicated", userName));
        });

        UserEntity userEntity = userEntityRepository.save(UserEntity.of(userName,encoder.encode(password)));

        return User.fromEntity(userEntity);
    }

    public String login(String userName, String password) {

        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

        if(!encoder.matches(password, userEntity.getPassword())) {
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        return JwtTokenUtils.generateToken(userName, secretKey, expiredTimeMs);
    }

    public Page<Alarm> alarmList(String username, Pageable pageable) {
        UserEntity userEntity = userEntityRepository.findByUserName(username).orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        return alarmEntityRepository.findAllByUser(userEntity, pageable).map(Alarm::fromEntity);
    }
}
