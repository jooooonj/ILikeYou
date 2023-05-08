package com.ll.gramgram.boundedContext.notification.service;

import com.ll.gramgram.boundedContext.likeablePerson.service.LikeablePersonService;

import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import com.ll.gramgram.boundedContext.notification.entity.Notification;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class NotificationServiceTests {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("호감 표현을 받은 인스타멤버에게 알람이 제대로 생성되는지")
    void t001() throws Exception {

        Member memberUser3 = memberService.findByUsername("user3").orElseThrow();
        Member memberUser5ByKakao = memberService.findByUsername("KAKAO__2733211417").orElseThrow();

        List<Notification> notifications = notificationService.findByToInstaMember(memberUser5ByKakao.getInstaMember());
        assertThat(notifications.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("호감 표현을 받은 인스타멤버에게 알람에 정보가 제대로 들어가는지")
    void t002() throws Exception {

        Member memberUser3 = memberService.findByUsername("user3").orElseThrow();
        Member memberUser5ByKakao = memberService.findByUsername("KAKAO__2733211417").orElseThrow();

        List<Notification> notifications = notificationService.findByToInstaMember(memberUser5ByKakao.getInstaMember());
        Notification notification = notifications.get(0);

        assertThat(notification.getFromInstaMember().getId()).isEqualTo(memberUser3.getInstaMember().getId());

    }
}