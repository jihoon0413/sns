package com.study.sns.service;

import com.study.sns.exception.ErrorCode;
import com.study.sns.exception.SnsApplicationException;
import com.study.sns.model.entity.PostEntity;
import com.study.sns.model.entity.UserEntity;
import com.study.sns.repository.PostEntityRepository;
import com.study.sns.repository.UserEntityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;

    @Transactional
    public void create(String title, String body, String username) {
        UserEntity userEntity = userEntityRepository.findByUserName(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        PostEntity entity = postEntityRepository.save(PostEntity.of(title, body, userEntity));
    }

    @Transactional
    public void modify(String title, String body, String userName, Long postId) {
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() ->
            new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
    }

}
