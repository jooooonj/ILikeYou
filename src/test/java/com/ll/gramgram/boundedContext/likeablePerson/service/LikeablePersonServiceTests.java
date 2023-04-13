package com.ll.gramgram.boundedContext.likeablePerson.service;

import com.ll.gramgram.base.appConfig.AppConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class LikeablePersonServiceTests {
    @Autowired
    private LikeablePersonService likeablePersonService;

    @Test
    @DisplayName("호감상대는 최대 10명까지 추가된다. AppConfig에 10명이 잘 들어가는지 확인")
    void t001() throws Exception {
        int maxFromLikeablePeople = AppConfig.getMaxFromLikeablePeople();
        Assertions.assertThat(maxFromLikeablePeople).isEqualTo(10);
    }
}
