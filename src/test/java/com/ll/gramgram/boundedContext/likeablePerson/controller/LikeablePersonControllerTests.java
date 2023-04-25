package com.ll.gramgram.boundedContext.likeablePerson.controller;

import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.service.LikeablePersonService;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import com.ll.gramgram.standard.exception.DataNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class LikeablePersonControllerTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private MemberService memberService;
    @Autowired
    private LikeablePersonService likeablePersonService;

    @Test
    @DisplayName("등록 폼(인스타 인증을 안해서 폼 대신 메세지)")
    @WithUserDetails("user1")
    void t001() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/likeablePerson/add"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("showAdd"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("""
                        먼저 본인의 인스타그램 아이디를 입력해주세요.
                        """.stripIndent().trim())))
        ;
    }

    @Test
    @DisplayName("등록 폼")
    @WithUserDetails("user2")
    void t002() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/likeablePerson/add"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("showAdd"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("""
                        <input type="text" name="username"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="radio" name="attractiveTypeCode" value="1"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="radio" name="attractiveTypeCode" value="2"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="radio" name="attractiveTypeCode" value="3"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="submit" value="추가"
                        """.stripIndent().trim())));
        ;
    }

    @Test
    @DisplayName("등록 폼 처리(user2가 user3에게 호감표시(외모))")
    @WithUserDetails("user2")
    void t003() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/likeablePerson/add")
                        .with(csrf()) // CSRF 키 생성
                        .param("username", "insta_user3")
                        .param("attractiveTypeCode", "1")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("add"))
                .andExpect(status().is3xxRedirection());
        ;
    }

    @Test
    @DisplayName("등록 폼 처리(user2가 abcd에게 호감표시(외모), abcd는 아직 우리 서비스에 가입하지 않은상태)")
    @WithUserDetails("user2")
    void t004() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/likeablePerson/add")
                        .with(csrf()) // CSRF 키 생성
                        .param("username", "abcd")
                        .param("attractiveTypeCode", "2")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("add"))
                .andExpect(status().is3xxRedirection());
        ;
    }

    @Test
    @DisplayName("호감목록")
    @WithUserDetails("user3")
    void t005() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/likeablePerson/list"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("showList"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("""
                        data-test="toInstaMember_username=insta_user4"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        data-test="toInstaMember_attractiveTypeDisplayName=외모"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        data-test="toInstaMember_username=insta_user100"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        data-test="toInstaMember_attractiveTypeDisplayName=성격"
                        """.stripIndent().trim())));
        ;
    }

    @Test
    @DisplayName("호감 상대 삭제하기")
    @WithUserDetails("user3")
    void t006() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/likeablePerson/delete/2")
                        .with(csrf()))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("delete"))
                .andExpect(status().is3xxRedirection());


        Assertions.assertThrows(DataNotFoundException.class, () -> {
            likeablePersonService.getLikeablePerson(2L);
        });
    }

    @Test
    @DisplayName("호감 상대 삭제하기 (자기가 등록한 대상이 아닐때) -> 예외 발생해야 한다.")
    @WithUserDetails("user2")
    void t007() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/likeablePerson/delete/2")
                        .with(csrf()))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("delete"))
                .andExpect(status().is4xxClientError());

    }

    @Test
    @DisplayName("이미 호감상대로 등록되어 있는 인스타 아이디 호감 등록(매력 포인트 중복) -> 실패")
    @WithUserDetails("user3")
    void t008() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/likeablePerson/add")
                        .with(csrf()) // CSRF 키 생성
                        .param("username", "insta_user4")
                        .param("attractiveTypeCode", "1")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("add"))
                .andExpect(status().is4xxClientError());
        ;
    }

    @Test
    @DisplayName("이미 호감상대로 등록되어 있는 인스타 아이디 호감 등록(매력 포인트 틀림) -> 수정(성공)")
    @WithUserDetails("user3")
    void t009() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/likeablePerson/add")
                        .with(csrf()) // CSRF 키 생성
                        .param("username", "insta_user4")
                        .param("attractiveTypeCode", "2")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("add"))
                .andExpect(status().is3xxRedirection());
        ;

    }

    @Test
    @DisplayName("이미 호감상대로 등록되어 있는 인스타 아이디 호감 등록(매력 포인트 틀림) -> 수정(성공)")
    @WithUserDetails("user2")
    void t010() throws Exception {
        Member member = memberService.findByUsername("user2").get();

        //11번을 등록해도 최대 10개
        for (int i = 0; i < 10; i++) {
            likeablePersonService.like(member, "test_instaMember" + i, 1);
        }
        List<LikeablePerson> likeablePeople = likeablePersonService.findByFromInstaMemberId(member.getInstaMember().getId());


        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/likeablePerson/add")
                        .with(csrf()) // CSRF 키 생성
                        .param("username", "test_instaMember11")
                        .param("attractiveTypeCode", "1")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("add"))
                .andExpect(status().is4xxClientError());
        ;
    }

    @Test
    @DisplayName("수정 폼")
    @WithUserDetails("user3")
    void t011() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/likeablePerson/modify/2"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("showModify"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("""
                        <input type="radio" name="attractiveTypeCode" value="1"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="radio" name="attractiveTypeCode" value="2"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="radio" name="attractiveTypeCode" value="3"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        inputValue__attractiveTypeCode = 2;
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        id="btn-modify-like-1"
                        """.stripIndent().trim())));
        ;
    }


}
