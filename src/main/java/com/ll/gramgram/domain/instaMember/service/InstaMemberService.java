package com.ll.gramgram.domain.instaMember.service;

import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.domain.instaMember.entity.InstaMember;
import com.ll.gramgram.domain.instaMember.repository.InstaMemberRepository;
import com.ll.gramgram.domain.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.domain.member.entity.Member;
import com.ll.gramgram.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InstaMemberService {
    private final InstaMemberRepository instaMemberRepository;
    private final MemberService memberService;

    public Optional<InstaMember> findByUsername(String username) {
        return instaMemberRepository.findByUsername(username);
    }

    @Transactional
    // member : 현재 로그인한 회원
    // username : 입력한 본인 인스타 username
    // gender : 입력한 본인의 성별
    public RsData<InstaMember> connect(Member member, String username, String gender) {
        Optional<InstaMember> opInstaMember = findByUsername(username); // 혹시 다른 회원이 이미 입력하신 인스타 ID와 연결되었는지

        // 등록이 되어있고
        if (opInstaMember.isPresent()) {
            // 계정 연결까지 되어 있으면 실패
            if(opInstaMember.get().hasConnected())
                return RsData.of("F-1", "해당 인스타그램 아이디는 이미 다른 사용자와 연결되었습니다.");
            //계정 연결은 안되어 있으면
            InstaMember instaMember = opInstaMember.get();
            instaMember.updateGender(gender);

            return connect(member, instaMember);
        }

        //계정이 아예 없으면 만들어서 연결
        RsData<InstaMember> instaMemberRsData = create(username, gender);
        InstaMember instaMember = instaMemberRsData.getData();

        return connect(member, instaMember);
    }

    @Transactional
    public RsData<InstaMember> connect(Member actor, String gender, String oauthId, String username, String accessToken) {
        Optional<InstaMember> opInstaMember = instaMemberRepository.findByOauthId(oauthId);

        if (opInstaMember.isPresent()) {
            InstaMember instaMember = opInstaMember.get();
            instaMember.setUsername(username);
            instaMember.setAccessToken(accessToken);
            instaMember.setGender(gender);
            instaMemberRepository.save(instaMember);

            actor.connectInstaMember(instaMember);

            return RsData.of("S-3", "인스타계정이 연결되었습니다.", instaMember);
        }

        opInstaMember = findByUsername(username);
        if (opInstaMember.isPresent()) {
            InstaMember instaMember = opInstaMember.get();
            instaMember.setOauthId(oauthId);
            instaMember.setAccessToken(accessToken);
            instaMember.setGender(gender);
            instaMemberRepository.save(instaMember);

            memberService.connectInstaMember(actor, instaMember);

            return RsData.of("S-4", "인스타계정이 연결되었습니다.", instaMember);
        }

        InstaMember instaMember = connect(actor, username, gender).getData();
        instaMember.setOauthId(oauthId);
        instaMember.setAccessToken(accessToken);
        return RsData.of("S-5", "인스타계정이 연결되었습니다.", instaMember);
    }

    private RsData<InstaMember> connect(Member member, InstaMember instaMember){
        memberService.connectInstaMember(member, instaMember);
        instaMember.connectedByMember(member);
        instaMemberRepository.save(instaMember);

        return RsData.of("S-1", "인스타 계정이 연결되었습니다.", instaMember);
    }

    @Transactional
    public RsData<InstaMember> findByUsernameOrCreate(String username) {
        Optional<InstaMember> opInstaMember = findByUsername(username);

        if (opInstaMember.isPresent())
            return RsData.of("S-2", "인스타계정이 등록되었습니다.", opInstaMember.get());

        // 아직 성별을 알 수 없으니, 언노운의 의미로 U 넣음
        return create(username, "U");
    }

    @Transactional
    public RsData<InstaMember> findByUsernameOrCreate(String username, String gender) {
        Optional<InstaMember> opInstaMember = findByUsername(username);

        // 찾았다면
        if (opInstaMember.isPresent()) {
            InstaMember instaMember = opInstaMember.get();
            instaMember.setGender(gender); // 성별세팅
            instaMemberRepository.save(instaMember); // 저장

            // 기존 인스타회원이랑 연결
            return RsData.of("S-2", "인스타계정이 등록되었습니다.", instaMember);
        }

        // 생성
        return create(username, gender);
    }


    @Transactional(readOnly = true)
    public InstaMember findById(long id){
        return instaMemberRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException(id+"는 가입되지 않은 인스타계정입니다.")
        );
    }

    public void eventCanceledLike(LikeablePerson likeablePerson){
        InstaMember fromInstaMember = likeablePerson.getFromInstaMember();
        InstaMember toInstaMember = likeablePerson.getToInstaMember();

        //나를 좋아하는 목록에서 삭제, 통계 감소, 내가 좋아하는 목록에서 삭제
        toInstaMember.delToLikeablePerson(likeablePerson);
        toInstaMember.decreaseLikesCount(fromInstaMember.getGender(), likeablePerson.getAttractiveTypeCode());
        fromInstaMember.delFromLikeablePerson(likeablePerson);
    }

    public void eventLiked(LikeablePerson likeablePerson){
        InstaMember fromInstaMember = likeablePerson.getFromInstaMember();
        InstaMember toInstaMember = likeablePerson.getToInstaMember();

        // 너가 좋아요한거 목록에 추가
        fromInstaMember.addFromLikeablePerson(likeablePerson);

        // 너를 좋아요한거 목록에 추가
        toInstaMember.addToLikeablePerson(likeablePerson);
        toInstaMember.increaseLikesCount(fromInstaMember.getGender(), likeablePerson.getAttractiveTypeCode());

        //읽지 않은 알람 1 추가
        toInstaMember.increaseUnreadNotificationCount();
    }

    public void eventModifiedAttractiveType(LikeablePerson likeablePerson, int oldAttractiveTypeCode, int newAttractiveTypeCode){
        InstaMember fromInstaMember = likeablePerson.getFromInstaMember();
        InstaMember toInstaMember = likeablePerson.getToInstaMember();

        toInstaMember.increaseLikesCount(fromInstaMember.getGender(), newAttractiveTypeCode);
        toInstaMember.decreaseLikesCount(fromInstaMember.getGender(), oldAttractiveTypeCode);

        //읽지 않은 알람 1 추가
        toInstaMember.increaseUnreadNotificationCount();
    }

    public void clearUnreadNotification(InstaMember instaMember) {
        instaMember.clearUnreadNotificationCount();
    }

    @Transactional
    public RsData<InstaMember> disconnect(Member member, Long instaId) {
        if (!member.hasConnectedInstaMember()) {
            return RsData.of("F-1", "연결된 인스타 계정이 없습니다.");
        }

        if(member.getInstaMember().getId() != instaId) {
            return RsData.of("F-2", "잘못된 접근입니다.");
        }

        InstaMember instaMember = findById(instaId);
        memberService.disconnectInstaMember(member);
        instaMember.disConnected();
        instaMemberRepository.save(instaMember);

        return RsData.of("S-1", "인스타계정(%s)과 연결이 헤제되었습니다.".formatted(instaMember.getUsername()));
    }

    // InstaMember 생성
    private RsData<InstaMember> create(String username, String gender) {
        InstaMember instaMember = InstaMember
                .builder()
                .username(username)
                .gender(gender)
                .build();

        instaMemberRepository.save(instaMember);

        return RsData.of("S-1", "인스타계정이 등록되었습니다.", instaMember);
    }
}
