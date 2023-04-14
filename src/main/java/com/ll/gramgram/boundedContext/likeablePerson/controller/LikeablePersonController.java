package com.ll.gramgram.boundedContext.likeablePerson.controller;

import com.ll.gramgram.base.rq.Rq;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.service.LikeablePersonService;
import com.ll.gramgram.boundedContext.member.entity.Member;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/likeablePerson")
@RequiredArgsConstructor
public class LikeablePersonController {
    private final Rq rq;
    private final LikeablePersonService likeablePersonService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/add")
    public String showAdd() {
        return "usr/likeablePerson/add";
    }

    @AllArgsConstructor
    @Getter
    public static class AddForm {
        private final String username;
        private final int attractiveTypeCode;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add")
    public String add(@Valid AddForm addForm) {

        // S- : 성공 , F-0 : 수정가능, F- : 실패
        RsData canLikeResult = likeablePersonService.canLike(rq.getMember(), addForm.getUsername(), addForm.getAttractiveTypeCode());

        //F-0 은 중복이지만 수정 가능한 상태
        if(canLikeResult.getResultCode().equals("F-0")){
            RsData<LikeablePerson> updateResult =
                    likeablePersonService.update(addForm.getAttractiveTypeCode(), (LikeablePerson) canLikeResult.getData());

            return rq.redirectWithMsg("/likeablePerson/list", updateResult);
        }

        if (canLikeResult.isFail()) {
            return rq.historyBack(canLikeResult);
        }

        RsData<LikeablePerson> likeResult = likeablePersonService.like(rq.getMember(), addForm.getUsername(), addForm.getAttractiveTypeCode());

        return rq.redirectWithMsg("/likeablePerson/list", likeResult);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String showList(Model model) {
        InstaMember instaMember = rq.getMember().getInstaMember();

        // 인스타인증을 했는지 체크
        if (instaMember != null) {
            List<LikeablePerson> likeablePeople = likeablePersonService.findByFromInstaMemberId(instaMember.getId());
            model.addAttribute("likeablePeople", likeablePeople);
        }

        return "usr/likeablePerson/list";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") Long id) {

        Member member = rq.getMember();
        LikeablePerson findLikeablePerson = likeablePersonService.getLikeablePerson(id);

        RsData<LikeablePerson> result = likeablePersonService.delete(findLikeablePerson, member);

        if (result.isFail()) {
            return rq.historyBack(result);
        }

        return rq.redirectWithMsg("/likeablePerson/list", result);
    }
}
