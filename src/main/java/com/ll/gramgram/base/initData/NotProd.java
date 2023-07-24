package com.ll.gramgram.base.initData;

import com.ll.gramgram.domain.instaMember.service.InstaMemberService;
import com.ll.gramgram.domain.likeablePerson.service.LikeablePersonService;
import com.ll.gramgram.domain.member.entity.Member;
import com.ll.gramgram.domain.member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev", "test"})
public class NotProd {
    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            InstaMemberService instaMemberService,
            LikeablePersonService likeablePersonService
    ) {
        return args -> {
            Member memberAdmin = memberService.join("admin", "1234").getData();
            Member memberUser1 = memberService.join("user1", "1234").getData();
            Member memberUser2 = memberService.join("user2", "1234").getData();
            Member memberUser3 = memberService.join("user3", "1234").getData();
            Member memberUser4 = memberService.join("user4", "1234").getData();

            Member memberUser5ByKakao = memberService.whenSocialLogin("KAKAO", "KAKAO__2733211417").getData();
            Member memberUser6ByNaver = memberService.whenSocialLogin("NAVER", "NAVER__qByBkW5B6a7NIJBAjNe6Wgi95pDYfEW1TwlMY1h4X6E").getData();
            Member memberUser6ByGoogle = memberService.whenSocialLogin("GOOGLE", "GOOGLE__106315992272868236901").getData();
            Member memberUser6ByFacebook = memberService.whenSocialLogin("FACEBOOK", "FACEBOOK__101843292909293").getData();


            instaMemberService.connect(memberUser2, "insta_user2", "M");
            instaMemberService.connect(memberUser3, "insta_user3", "W");
            instaMemberService.connect(memberUser4, "insta_user4", "M");
            instaMemberService.connect(memberUser5ByKakao, "insta_user99", "M");

            likeablePersonService.like(memberUser3, "insta_user4", 1);
            likeablePersonService.like(memberUser3, "insta_user100", 2);
            likeablePersonService.like(memberUser3, "insta_user99", 2);
            likeablePersonService.like(memberUser4, "insta_user99", 1);
        };
    }
}
