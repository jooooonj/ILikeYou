package com.ll.gramgram.boundedContext.likeablePerson.service;

import com.ll.gramgram.base.appConfig.AppConfig;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.standard.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeablePersonService {
    private final LikeablePersonRepository likeablePersonRepository;
    private final InstaMemberService instaMemberService;

    @Transactional
    public RsData<LikeablePerson> like(Member member, String username, int attractiveTypeCode) {


        if (member.hasConnectedInstaMember() == false)
            return RsData.of("F-2", "먼저 본인의 인스타그램 아이디를 입력해야 합니다.");


        InstaMember fromInstaMember = member.getInstaMember();
        InstaMember toInstaMember = instaMemberService.findByUsernameOrCreate(username).getData();

        if (fromInstaMember.getUsername().equals(username))
            return RsData.of("F-1", "본인을 호감상대로 등록할 수 없습니다.");

        LikeablePerson findLikeablePerson = getLikeablePerson(fromInstaMember.getId(), toInstaMember.getId());

        //이미 자신이 등록한 인스타계정이라면 수정 또는 예외처리
        if (findLikeablePerson != null)
            return rejectOrUpdate(attractiveTypeCode, findLikeablePerson);

        //호감 상대를 추가할 수 있는 사이즈가 되는지
        if (canAddFromLikeablePersonBySize(fromInstaMember))
            return RsData.of("F-2", "호감 상대로 등록할 수 있는 10명이 가득 차있습니다.");

        LikeablePerson likeablePerson = createLikeablePerson(member, attractiveTypeCode, fromInstaMember, toInstaMember);
        likeablePersonRepository.save(likeablePerson); // 저장

        // 너가 좋아하는 호감표시 생겼어.
        fromInstaMember.addFromLikeablePerson(likeablePerson);

        // 너를 좋아하는 호감표시 생겼어.
        toInstaMember.addToLikeablePerson(likeablePerson);

        return RsData.of("S-1", "입력하신 인스타유저(%s)를 호감상대로 등록되었습니다.".formatted(username), likeablePerson);
    }

    private boolean canAddFromLikeablePersonBySize(InstaMember fromInstaMember) {
        return fromInstaMember.getFromLikeablePeople().size() >= AppConfig.getMaxFromLikeablePeople();
    }

    private LikeablePerson createLikeablePerson(Member member, int attractiveTypeCode, InstaMember fromInstaMember, InstaMember toInstaMember) {
        LikeablePerson likeablePerson = LikeablePerson
                .builder()
                .fromInstaMember(fromInstaMember) // 호감을 표시하는 사람의 인스타 멤버
                .fromInstaMemberUsername(member.getInstaMember().getUsername()) // 중요하지 않음
                .toInstaMember(toInstaMember) // 호감을 받는 사람의 인스타 멤버
                .toInstaMemberUsername(toInstaMember.getUsername()) // 중요하지 않음
                .attractiveTypeCode(attractiveTypeCode) // 1=외모, 2=능력, 3=성격
                .build();
        return likeablePerson;
    }

    private RsData<LikeablePerson> rejectOrUpdate(int attractiveTypeCode, LikeablePerson findLikeablePerson) {
        if (attractiveTypeCode == findLikeablePerson.getAttractiveTypeCode()) {
            return RsData.of("F-1", "이미 호감상대로 등록하신 회원입니다.");
        }

        findLikeablePerson.setAttractiveTypeCode(attractiveTypeCode);
        likeablePersonRepository.save(findLikeablePerson);
        return RsData.of("S-1", "입력하신 인스타유저(%s)를 호감포인트를 (%s)로 변경했습니다.".formatted(findLikeablePerson.getAttractiveTypeDisplayName(), findLikeablePerson.getAttractiveTypeCode()), findLikeablePerson);

    }

    public List<LikeablePerson> findByFromInstaMemberId(Long fromInstaMemberId) {
        return likeablePersonRepository.findByFromInstaMemberId(fromInstaMemberId);
    }


    @Transactional
    public RsData<LikeablePerson> delete(LikeablePerson likeablePerson, Member member) {

        long actorInstaId = member.getInstaMember().getId();
        long fromInstaId = likeablePerson.getFromInstaMember().getId();

        if (actorInstaId != fromInstaId) {
            return RsData.of("F-1", "삭제 권한이 없습니다.");
        }

        likeablePersonRepository.delete(likeablePerson);
        return RsData.of("S-1", "삭제되었습니다.");
    }

    public LikeablePerson getLikeablePerson(Long id) {
        return likeablePersonRepository.findById(id).orElseThrow(() -> new DataNotFoundException("잘못된 접근입니다."));
    }

    public LikeablePerson getLikeablePerson(Long fromInstaId, Long toInstaId) {
        return likeablePersonRepository.findByFromInstaMemberIdAndToInstaMemberId(fromInstaId, toInstaId).orElse(null);
    }
}
