package com.ll.gramgram.boundedContext.likeablePerson.controller;

import com.ll.gramgram.base.rq.Rq;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.service.LikeablePersonService;
import com.ll.gramgram.boundedContext.member.entity.Member;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/likeablePerson")
@RequiredArgsConstructor
public class LikeablePersonController {
    private final Rq rq;
    private final LikeablePersonService likeablePersonService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/like")
    public String showLike() {
        return "usr/likeablePerson/like";
    }

    @AllArgsConstructor
    @Getter
    public static class AddForm {
        @NotBlank
        @Size(min = 3, max = 30)
        private final String username;
        @NotNull
        @Min(1)
        @Max(3)
        private final int attractiveTypeCode;
    }

    @AllArgsConstructor
    @Getter
    public static class ModifyForm {
        private final int attractiveTypeCode;
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/like")
    public String like(@Valid AddForm addForm) {

        RsData<LikeablePerson> likeResult = likeablePersonService.like(rq.getMember(), addForm.getUsername(), addForm.getAttractiveTypeCode());

        if (likeResult.isFail())
            return rq.historyBack(likeResult);

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
    @DeleteMapping("/{id}")
    public String cancel(@PathVariable(value = "id") Long id) {

        Member member = rq.getMember();
        LikeablePerson findLikeablePerson = likeablePersonService.getLikeablePerson(id);

        RsData<LikeablePerson> result = likeablePersonService.cancel(findLikeablePerson, member);

        if (result.isFail()) {
            return rq.historyBack(result);
        }

        return rq.redirectWithMsg("/likeablePerson/list", result);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String showModify(Model model, @PathVariable("id") Long id) {

        LikeablePerson likeablePerson = likeablePersonService.getLikeablePerson(id);
        RsData result = likeablePersonService.canModify(rq.getMember(), likeablePerson);

        if (result.isFail()) rq.historyBack(result);

        model.addAttribute("likeablePerson", likeablePerson);
        return "usr/likeablePerson/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id, @Valid ModifyForm modifyForm) {

        LikeablePerson likeablePerson = likeablePersonService.getLikeablePerson(id);
        RsData canModifyResult = likeablePersonService.canModify(rq.getMember(), likeablePerson);

        if (canModifyResult.isFail()) rq.historyBack(canModifyResult);

        RsData<LikeablePerson> modifyResult = likeablePersonService.modify(id, modifyForm.getAttractiveTypeCode());

        return rq.redirectWithMsg("/likeablePerson/list", modifyResult);
    }
}
