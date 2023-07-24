package com.ll.gramgram.domain.likeablePerson.service;

import com.ll.gramgram.domain.likeablePerson.validator.LikeablePersonValidator;
import com.ll.gramgram.global.config.appConfig.AppConfig;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.domain.instaMember.entity.InstaMember;
import com.ll.gramgram.domain.instaMember.service.InstaMemberService;
import com.ll.gramgram.domain.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.domain.likeablePerson.repository.LikeablePersonRepository;
import com.ll.gramgram.domain.member.entity.Member;
import com.ll.gramgram.event.EventCanceledLike;
import com.ll.gramgram.event.EventAddLike;
import com.ll.gramgram.event.EventModifiedAttractiveType;
import com.ll.gramgram.global.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeablePersonService {
    private final LikeablePersonRepository likeablePersonRepository;
    private final InstaMemberService instaMemberService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final LikeablePersonValidator validator;
    @Transactional
    public RsData<LikeablePerson> like(Member member, String username, int attractiveTypeCode) {
        RsData<LikeablePerson> canLikeResult = validator.validateLike(member, username, attractiveTypeCode);
        // S- : 성공 , F-0 : 수정가능, F- : 실패

        //수정 코드 받은 경우
        if (canLikeResult.getResultCode().equals("F-0")){
            return modifyAttractiveTypeCode(attractiveTypeCode, canLikeResult.getData());
        }

        if (canLikeResult.isFail())
            return canLikeResult;

        InstaMember fromInstaMember = member.getInstaMember();
        InstaMember toInstaMember = instaMemberService.findByUsernameOrCreate(username).getData();

        LikeablePerson likeablePerson = createLikeablePerson(attractiveTypeCode, fromInstaMember, toInstaMember);
        likeablePersonRepository.save(likeablePerson); // 저장
        applicationEventPublisher.publishEvent(new EventAddLike(this, likeablePerson));
        return RsData.of("S-1", "입력하신 인스타유저(%s)를 호감상대로 등록되었습니다.".formatted(username), likeablePerson);
    }

    @Transactional
    public RsData<LikeablePerson> modifyAttractiveTypeCode(int newAttractiveTypeCode, LikeablePerson likeablePerson) {
        //기존 코드
        String originalAttract = likeablePerson.getAttractiveTypeDisplayName();
        int oldAttractiveTypeCode = likeablePerson.getAttractiveTypeCode();

        applicationEventPublisher.publishEvent(new EventModifiedAttractiveType(this,likeablePerson, oldAttractiveTypeCode, newAttractiveTypeCode));

        likeablePerson.modifyAttractiveTypeCode(newAttractiveTypeCode);
        likeablePerson.genModifyUnlockDate(AppConfig.genLikeModifyCoolTime());
        String newAttract = likeablePerson.getAttractiveTypeDisplayName();

        likeablePersonRepository.save(likeablePerson);
        return RsData.of("S-1", "인스타유저(%s)의 호감포인트를(%s)에서 (%s)로 변경했습니다."
                .formatted(likeablePerson.getToInstaMemberUsername(), originalAttract, newAttract), likeablePerson);
    }

    @Transactional
    public RsData<LikeablePerson> cancel(LikeablePerson likeablePerson, Member member) {
        InstaMember actorInstaMember = member.getInstaMember();
        InstaMember fromInstaMember = likeablePerson.getFromInstaMember();

        RsData result = validator.validateCancel(actorInstaMember, fromInstaMember, likeablePerson);
        if (result.isFail())
            return result;

        likeablePersonRepository.delete(likeablePerson);
        applicationEventPublisher.publishEvent(new EventCanceledLike(this, likeablePerson));

        return RsData.of("S-1", "삭제되었습니다.");
    }

    public List<LikeablePerson> findByIdFilteredAndSortedList(InstaMember instaMember, String gender, Integer attractiveTypeCode, int sortCode) {
        Long instaMemberId = instaMember.getId();

        if (StringUtils.isEmpty(gender))
            gender = null;

        if (attractiveTypeCode==0)
            attractiveTypeCode = null;

        return findByIdFilteredAndSortedList(instaMemberId, sortCode, gender, attractiveTypeCode);
    }

    public LikeablePerson getLikeablePerson(Long fromInstaId, Long toInstaId) {
        return likeablePersonRepository.findByFromInstaMemberIdAndToInstaMemberId(fromInstaId, toInstaId).orElse(null);
    }

    public LikeablePerson getLikeablePerson(Long id) {
        return likeablePersonRepository.findById(id).orElseThrow(() -> new DataNotFoundException("잘못된 접근입니다."));
    }

    public List<LikeablePerson> findByFromInstaMemberId(Long fromInstaMemberId) {
        return likeablePersonRepository.findByFromInstaMemberId(fromInstaMemberId);
    }



    //정렬 코드에 따라서 정렬된 결과를 반환
    private List<LikeablePerson> findByIdFilteredAndSortedList(Long instaMemberId, int sortCode, String gender, Integer attractiveTypeCode) {
        switch (sortCode) {
            case 1:
                return likeablePersonRepository.findByIdFilteredAndSortedOrderByCreateDateDesc(instaMemberId, gender, attractiveTypeCode);
            case 2:
                return likeablePersonRepository.findByIdFilteredAndSortedOrderByHotOfFromInstaMemberAsc(instaMemberId, gender, attractiveTypeCode);
            case 3:
                return likeablePersonRepository.findByIdFilteredAndSortedOrderByHotOfFromInstaMemberDesc(instaMemberId, gender, attractiveTypeCode);
            case 4:
                return likeablePersonRepository.findByIdFilteredAndSortedOrderByGenderOfFromInstaMemberAsc(instaMemberId, gender, attractiveTypeCode);
            case 5:
                return likeablePersonRepository.findByIdFilteredAndSortedOrderByAttractiveTypeCodeAsc(instaMemberId, gender, attractiveTypeCode);
            default:
                return likeablePersonRepository.findByIdFilteredAndSortedOrderByCreateDateAsc(instaMemberId, gender, attractiveTypeCode);
        }
    }

    private LikeablePerson createLikeablePerson(int attractiveTypeCode, InstaMember fromInstaMember, InstaMember toInstaMember) {
        LikeablePerson likeablePerson = LikeablePerson
                .builder()
                .fromInstaMember(fromInstaMember) // 호감을 표시하는 사람의 인스타 멤버
                .fromInstaMemberUsername(fromInstaMember.getUsername()) // 중요하지 않음
                .toInstaMember(toInstaMember) // 호감을 받는 사람의 인스타 멤버
                .toInstaMemberUsername(toInstaMember.getUsername()) // 중요하지 않음
                .attractiveTypeCode(attractiveTypeCode) // 1=외모, 2=능력, 3=성격
                .modifyUnlockDate(AppConfig.genLikeModifyCoolTime())
                .build();
        return likeablePerson;
    }
}
