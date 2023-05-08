package com.ll.gramgram.boundedContext.likeablePerson.service;

import com.ll.gramgram.base.appConfig.AppConfig;
import com.ll.gramgram.base.rsData.RsData;
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
    @DisplayName("쿨타임 때문에 수정이 실패해야 된다.")
    void t005() throws Exception {

        Member memberUser3 = memberService.findByUsername("user3").orElseThrow();
        Member memberUser5ByKakao = memberService.findByUsername("KAKAO__2733211417").orElseThrow();

        LikeablePerson likeablePerson = likeablePersonService.getLikeablePerson(memberUser3.getInstaMember().getId(), memberUser5ByKakao.getInstaMember().getId());
        assertThat(likeablePerson).isNotNull();

        RsData rsData = likeablePersonService.canModify(memberUser3, likeablePerson);
        assertThat(rsData.getMsg()).isEqualTo("쿨타임으로 인해 수정이 불가능합니다.");
    }

    @Test
    @DisplayName("쿨타임이 때문에 삭제가 실패해야 된다.")
    void t006() throws Exception {

        Member memberUser3 = memberService.findByUsername("user3").orElseThrow();
        Member memberUser5ByKakao = memberService.findByUsername("KAKAO__2733211417").orElseThrow();

        LikeablePerson likeablePerson = likeablePersonService.getLikeablePerson(memberUser3.getInstaMember().getId(), memberUser5ByKakao.getInstaMember().getId());
        assertThat(likeablePerson).isNotNull();

        RsData rsData = likeablePersonService.cancel(likeablePerson, memberUser3);
        assertThat(rsData.getMsg()).isEqualTo("쿨타임으로 인해 삭제가 불가능합니다.");
    }

}
