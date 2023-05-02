package com.ll.gramgram.boundedContext.likeablePerson.service;

import com.ll.gramgram.base.appConfig.AppConfig;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class LikeablePersonServiceTests {
    @Autowired
    private LikeablePersonService likeablePersonService;

    @Autowired
    private MemberService memberService;


    @Test
    @DisplayName("호감상대는 최대 10명까지 추가된다. AppConfig에 10명이 잘 들어가는지 확인")
    void t001() throws Exception {
        int maxFromLikeablePeople = AppConfig.getMaxFromLikeablePeople();
        assertThat(maxFromLikeablePeople).isEqualTo(10);
    }


    @Test
    @DisplayName("수정 쿨타임 yml에서 잘 받아오는지 확인")
    void t002() throws Exception {
        int modifyCoolTime = AppConfig.getModifyCoolTime();
        assertThat(modifyCoolTime).isGreaterThan(0);
    }

    @Test
    @DisplayName("좋아요 생성시 쿨타임이 지정되어야 한다.")
    void t003() throws Exception {
        LocalDateTime coolTime = AppConfig.genLikeModifyCoolTime();

        Member memberUser3 = memberService.findByUsername("user3").orElseThrow();
        LikeablePerson likeablePersonToBts = likeablePersonService.like(memberUser3, "bts", 3).getData();

        assertThat(
                likeablePersonToBts.getModifyUnlockDate().isEqual(coolTime)
        );
    }

    @Test
    @DisplayName("좋아요 변경시 쿨타임이 지정되어야 한다.")
    void t004() throws Exception {

        Member memberUser3 = memberService.findByUsername("user3").orElseThrow();
        LikeablePerson likeablePersonToBts = likeablePersonService.like(memberUser3, "bts", 3).getData();
        likeablePersonToBts.genModifyUnlockDate(LocalDateTime.now());


        likeablePersonService.modifyAttractiveTypeCode(2, likeablePersonToBts);

        assertThat(
                likeablePersonToBts.getModifyUnlockDate().isEqual(AppConfig.genLikeModifyCoolTime()));
    }

    @Test
    @DisplayName("몇시 몇분부터 수정,삭제를 할 수 있는지 잘 나오는지 확인")
    void t005() throws Exception {

        Member memberUser3 = memberService.findByUsername("user3").orElseThrow();
        LikeablePerson likeablePersonToBts = likeablePersonService.like(memberUser3, "bts", 3).getData();

        String modifyUnlockDateTime = likeablePersonToBts.getModifyUnlockDateRemainStrHuman();
        System.out.println(modifyUnlockDateTime);

    }

}
