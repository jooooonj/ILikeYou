package com.ll.gramgram.domain.likeablePerson.service;

import com.ll.gramgram.domain.likeablePerson.entity.dto.LikeablePersonResponse;
import com.ll.gramgram.domain.likeablePerson.validator.LikeablePersonValidator;
import com.ll.gramgram.global.config.appConfig.AppConfig;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.domain.instaMember.entity.InstaMember;
import com.ll.gramgram.domain.instaMember.service.InstaMemberService;
import com.ll.gramgram.domain.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.domain.member.entity.Member;
import com.ll.gramgram.domain.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class LikeablePersonServiceTests {
    @Autowired
    private LikeablePersonService likeablePersonService;

    @Autowired
    private LikeablePersonValidator likeablePersonValidator;

    @Autowired
    private MemberService memberService;
    @Autowired
    private InstaMemberService instaMemberService;


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

        RsData rsData = likeablePersonValidator.validateModify(memberUser3, likeablePerson);
        assertThat(rsData.getMsg()).isEqualTo("검증 실패, 쿨타임으로 인해 수정이 불가능합니다.");
    }

    @Test
    @DisplayName("쿨타임 때문에 삭제가 실패해야 된다.")
    void t006() throws Exception {

        Member memberUser3 = memberService.findByUsername("user3").orElseThrow();
        Member memberUser5ByKakao = memberService.findByUsername("KAKAO__2733211417").orElseThrow();

        LikeablePerson likeablePerson = likeablePersonService.getLikeablePerson(memberUser3.getInstaMember().getId(), memberUser5ByKakao.getInstaMember().getId());
        assertThat(likeablePerson).isNotNull();

        RsData rsData = likeablePersonService.cancel(likeablePerson, memberUser3);
        assertThat(rsData.getMsg()).isEqualTo("검증 실패, 쿨타임으로 인해 삭제가 불가능합니다.");
    }

    @Test
    @DisplayName("성별 : 전체, 호감사유 : 전체, 최신순 했을 때 2개가 나와야 한다.")
    void t007() throws Exception {
//findByIdByCondition(InstaMember instaMember, String gender, Integer attractiveTypeCode, int sortCode) {

        InstaMember instaMember = instaMemberService.findById(4);

        List<LikeablePersonResponse> likeablePeople = likeablePersonService.findByIdFilteredAndSortedList(instaMember, "", null, null);
        //전체라서 두 개 나와야 한다.
        assertThat(likeablePeople.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("성별 : M, 호감사유 : 전체, 최신순 했을 때 1개가 나와야 한다. (성별 필터링 테스트)")
    void t008() throws Exception {
//findByIdByCondition(InstaMember instaMember, String gender, Integer attractiveTypeCode, int sortCode) {

        InstaMember instaMember = instaMemberService.findById(4);

        List<LikeablePersonResponse> likeablePeople = likeablePersonService.findByIdFilteredAndSortedList(instaMember, "M", null, null);
        //성별 남성은 한개다.
        assertThat(likeablePeople.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("성별 : 전체, 호감사유 : 1, 최신순 했을 때 1개가 나와야 한다. (호감사유 필터링 테스트)")
    void t009() throws Exception {
//findByIdByCondition(InstaMember instaMember, String gender, Integer attractiveTypeCode, int sortCode) {

        InstaMember instaMember = instaMemberService.findById(4);

        List<LikeablePersonResponse> likeablePeople = likeablePersonService.findByIdFilteredAndSortedList(instaMember, null, 1, 0);

        //호감사유 1 은 한개다.
        assertThat(likeablePeople.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("값이 두개 밖에 없는 결과를 조회했을 때 최신순 정렬 결과의 첫번째 값과 오래된순 정렬 결과의 마지막 값은 같아야 한다.  (정렬 테스트)")
    void t010() throws Exception {

        InstaMember instaMember = instaMemberService.findById(4);

        //최신순
        List<LikeablePersonResponse> likeablePeopleOrderByAsc = likeablePersonService.findByIdFilteredAndSortedList(instaMember, "", null, null);
        LikeablePersonResponse likeablePersonOrderByAsc = likeablePeopleOrderByAsc.get(0);
        //오래된순
        List<LikeablePersonResponse> likeablePeopleOrderByDesc = likeablePersonService.findByIdFilteredAndSortedList(instaMember, "", null, 1);
        LikeablePersonResponse likeablePersonOrderByDesc = likeablePeopleOrderByDesc.get(1);

        assertThat(likeablePersonOrderByAsc.getId()==likeablePersonOrderByDesc.getId()).isEqualTo(true);
    }

    @Test
    @DisplayName("값이 두개 밖에 없는 결과를 조회했을 때 인기많은순 정렬 결과의 첫번째 값과 인기적은순 정렬 결과의 마지막 값은 같아야 한다. (정렬 테스트)")
    void t011() throws Exception {

        InstaMember instaMember = instaMemberService.findById(4);

        //인기많은순
        List<LikeablePersonResponse> likeablePeopleOrderByAsc = likeablePersonService.findByIdFilteredAndSortedList(instaMember, "", null, 2);
        LikeablePersonResponse likeablePersonOrderByAsc = likeablePeopleOrderByAsc.get(0);
        //인기적은순
        List<LikeablePersonResponse> likeablePeopleOrderByDesc = likeablePersonService.findByIdFilteredAndSortedList(instaMember, "", null, 3);
        LikeablePersonResponse likeablePersonOrderByDesc = likeablePeopleOrderByDesc.get(1);

        assertThat(likeablePeopleOrderByAsc.size()==likeablePeopleOrderByDesc.size()).isEqualTo(true);
    }




}
