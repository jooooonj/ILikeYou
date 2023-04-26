package com.ll.gramgram.boundedContext.likeablePerson.service;

import com.ll.gramgram.base.appConfig.AppConfig;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.event.EventCanceledLike;
import com.ll.gramgram.event.EventLiked;
import com.ll.gramgram.event.EventModifiedAttractiveType;
import com.ll.gramgram.standard.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeablePersonService {
    private final LikeablePersonRepository likeablePersonRepository;
    private final InstaMemberService instaMemberService;
    private final ApplicationEventPublisher applicationEventPublisher;
    @Transactional
    public RsData<LikeablePerson> like(Member member, String username, int attractiveTypeCode) {
        RsData<LikeablePerson> canLikeResult = canLike(member, username, attractiveTypeCode);
        // S- : 성공 , F-0 : 수정가능, F- : 실패

        //수정 코드 받은 경우
        if (canLikeResult.getResultCode().equals("F-0")){
            return modifyAttractiveTypeCode(attractiveTypeCode, (LikeablePerson) canLikeResult.getData());
        }

        if (canLikeResult.isFail())
            return canLikeResult;

        InstaMember fromInstaMember = member.getInstaMember();
        InstaMember toInstaMember = instaMemberService.findByUsernameOrCreate(username).getData();

        LikeablePerson likeablePerson = createLikeablePerson(attractiveTypeCode, fromInstaMember, toInstaMember);
        likeablePersonRepository.save(likeablePerson); // 저장
        applicationEventPublisher.publishEvent(new EventLiked(this, likeablePerson));
        return RsData.of("S-1", "입력하신 인스타유저(%s)를 호감상대로 등록되었습니다.".formatted(username), likeablePerson);
    }

    private boolean canAddFromLikeablePersonBySize(Member member) {
        InstaMember instaMember = member.getInstaMember();
        return instaMember.getFromLikeablePeople().size() < AppConfig.getMaxFromLikeablePeople();
    }

    private LikeablePerson createLikeablePerson(int attractiveTypeCode, InstaMember fromInstaMember, InstaMember toInstaMember) {
        LikeablePerson likeablePerson = LikeablePerson
                .builder()
                .fromInstaMember(fromInstaMember) // 호감을 표시하는 사람의 인스타 멤버
                .fromInstaMemberUsername(fromInstaMember.getUsername()) // 중요하지 않음
                .toInstaMember(toInstaMember) // 호감을 받는 사람의 인스타 멤버
                .toInstaMemberUsername(toInstaMember.getUsername()) // 중요하지 않음
                .attractiveTypeCode(attractiveTypeCode) // 1=외모, 2=능력, 3=성격
                .build();
        return likeablePerson;
    }

    @Transactional
    public RsData<LikeablePerson> modifyAttractiveTypeCode(int newAttractiveTypeCode, LikeablePerson likeablePerson) {
        //기존 코드
        String originalAttractiveTypeCode = likeablePerson.getAttractiveTypeDisplayName();
        int oldAttractiveTypeCode = likeablePerson.getAttractiveTypeCode();

        applicationEventPublisher.publishEvent(new EventModifiedAttractiveType(this,likeablePerson, oldAttractiveTypeCode, newAttractiveTypeCode));

        likeablePerson.modifyAttractiveTypeCode(newAttractiveTypeCode);

        likeablePersonRepository.save(likeablePerson);
        return RsData.of("S-1", "입력하신 인스타유저(%s)의 호감포인트를(%s)에서 (%s)로 변경했습니다."
                .formatted(likeablePerson.getAttractiveTypeDisplayName(), originalAttractiveTypeCode, newAttractiveTypeCode), likeablePerson);
    }

    public List<LikeablePerson> findByFromInstaMemberId(Long fromInstaMemberId) {
        return likeablePersonRepository.findByFromInstaMemberId(fromInstaMemberId);
    }


    @Transactional
    public RsData<LikeablePerson> cancel(LikeablePerson likeablePerson, Member member) {


        InstaMember actorInstaMember = member.getInstaMember();
        InstaMember fromInstaMember = likeablePerson.getFromInstaMember();

        if (actorInstaMember.getId() != fromInstaMember.getId()) {
            return RsData.of("F-1", "삭제 권한이 없습니다.");
        }


        InstaMember toInstaMember = likeablePerson.getToInstaMember();

        likeablePersonRepository.delete(likeablePerson);

        //삭제되면 실행되어야 될 것들

        applicationEventPublisher.publishEvent(new EventCanceledLike(this, likeablePerson));

        return RsData.of("S-1", "삭제되었습니다.");
    }

    public LikeablePerson getLikeablePerson(Long id) {
        return likeablePersonRepository.findById(id).orElseThrow(() -> new DataNotFoundException("잘못된 접근입니다."));
    }

    public LikeablePerson getLikeablePerson(Long fromInstaId, Long toInstaId) {
        return likeablePersonRepository.findByFromInstaMemberIdAndToInstaMemberId(fromInstaId, toInstaId).orElse(null);
    }

    public RsData<LikeablePerson> canLike(Member member, String username, int attractiveTypeCode) {

        if (!checkResisterInstaMember(member))
            return RsData.of("F-1", "먼저 본인의 인스타그램 아이디를 입력해야 합니다.");

        if (!checkSelfResister(member, username))
            return RsData.of("F-2", "본인을 호감상대로 등록할 수 없습니다.");

        //중복이면 내부에서 호감포인트 중복까지 확인해서 수정 가능인지 아니면 아예 실패인지 확인해준다.
        RsData checkDuplicateResult = checkDuplicate(member, username, attractiveTypeCode);
        if (checkDuplicateResult.isFail())
            return checkDuplicateResult;

        //호감 상대를 추가할 수 있는 사이즈가 되는지
        if (!canAddFromLikeablePersonBySize(member))
            return RsData.of("F-3", "호감 상대로 등록할 수 있는 10명이 가득 차있습니다.");

        return RsData.of("S-1", "검증 성공, 호감 상대 등록 가능합니다.");
    }

    private boolean checkSelfResister(Member member, String username) {
        InstaMember instaMember = member.getInstaMember();

        return !instaMember.getUsername().equals(username);
    }

    private boolean checkResisterInstaMember(Member member) {
        return member.hasConnectedInstaMember() == true;
    }

    private RsData checkDuplicate(Member member, String username, int attractiveTypeCode) {
        InstaMember fromInstaMember = member.getInstaMember();
        InstaMember toInstaMember = instaMemberService.findByUsername(username).orElse(null);

        if (toInstaMember == null)
            return RsData.of("S-1", "중복이 아닙니다.");

        LikeablePerson findLikeablePerson = getLikeablePerson(fromInstaMember.getId(), toInstaMember.getId());

        if (findLikeablePerson != null) {
            if (findLikeablePerson.getAttractiveTypeCode() == attractiveTypeCode)
                return RsData.of("F-4", "(%s)는 이미 등록하신 상대입니다.".formatted(toInstaMember.getUsername()));

            //이미 등록되어 있는데 매력 포인트가 다르다면 수정할 수 있도록 객체를 넣어서 전달
            return RsData.of("F-0", "수정가능", findLikeablePerson);
        }

        return RsData.of("S-1", "중복이 아닙니다.");
    }

    public RsData canModify(Member member, LikeablePerson likeablePerson) {
        if (!member.hasConnectedInstaMember())
            return RsData.of("F-1", "인스타 계정을 연결해주세요.");

        if (member.getInstaMember().getId() != likeablePerson.getFromInstaMember().getId())
            return RsData.of("F-1", "잘못된 접근입니다.");

        return RsData.of("S-1", "호감 표시 수정이 가능합니다.");
    }

    @Transactional
    public RsData<LikeablePerson> modifyAttract(Long id, int attractiveTypeCode) {
        LikeablePerson likeablePerson = getLikeablePerson(id);
        modifyAttractiveTypeCode(attractiveTypeCode, likeablePerson);
        return RsData.of("S-1", "매력 포인트 수정이 완료되었습니다.", likeablePerson);
    }
}
