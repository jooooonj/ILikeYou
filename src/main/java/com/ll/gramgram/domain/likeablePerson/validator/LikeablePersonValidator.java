package com.ll.gramgram.domain.likeablePerson.validator;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.domain.instaMember.entity.InstaMember;
import com.ll.gramgram.domain.instaMember.service.InstaMemberService;
import com.ll.gramgram.domain.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.domain.likeablePerson.repository.LikeablePersonRepository;
import com.ll.gramgram.domain.member.entity.Member;
import com.ll.gramgram.global.config.appConfig.AppConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
@RequiredArgsConstructor
@Configuration
public class LikeablePersonValidator {

    private final InstaMemberService instaMemberService;
    private final LikeablePersonRepository likeablePersonRepository;

    public RsData<LikeablePerson> validateLike(Member member, String username, int attractiveTypeCode) {

        if (!checkConnectInstaMember(member))
            return RsData.of("F-1", "검증 실패, 먼저 본인의 인스타그램 아이디를 입력해야 합니다.");

        if (!isSelfResister(member, username))
            return RsData.of("F-2", "검증 실패, 본인을 호감상대로 등록할 수 없습니다.");

        //중복이면 내부에서 호감포인트 중복까지 확인해서 수정 가능인지 아니면 아예 실패인지 확인해준다.
        RsData checkDuplicateResult = checkDuplicate(member, username, attractiveTypeCode);
        if (checkDuplicateResult.isFail())
            return checkDuplicateResult;

        //호감 상대를 추가할 수 있는 사이즈가 되는지
        if (!checkFromLikeablePersonCountLimit(member))
            return RsData.of("F-3", "검증 실패, 호감 상대로 등록할 수 있는 수가 가득 차있습니다.");

        return RsData.of("S-1", "검증 성공, 호감 상대 등록 가능합니다.");
    }

    public RsData validateCancel(InstaMember actorInstaMember, InstaMember fromInstaMember, LikeablePerson likeablePerson){
        if(!checkAuthority(actorInstaMember, fromInstaMember))
            return RsData.of("F-1", "검증 실패, 삭제 권한이 없습니다.");

        if(!likeablePerson.isModifyUnlocked())
            return RsData.of("F-3", "검증 실패, 쿨타임으로 인해 삭제가 불가능합니다.");

        return RsData.of("S-1", "검증 성공, 삭제가 가능합니다.");
    }

    public RsData validateModify(Member member, LikeablePerson likeablePerson) {
        if (!member.hasConnectedInstaMember())
            return RsData.of("F-1", "검증 실패, 인스타 계정을 연결해주세요.");

        if (!checkAuthority(member.getInstaMember(), likeablePerson.getFromInstaMember()))
            return RsData.of("F-2", "검증 실패, 잘못된 접근입니다.");

        if(!likeablePerson.isModifyUnlocked())
            return RsData.of("F-3", "검증 실패, 쿨타임으로 인해 수정이 불가능합니다.");

        return RsData.of("S-1", "호감 표시 수정이 가능합니다.");
    }


    private boolean checkConnectInstaMember(Member member) {
        return member.hasConnectedInstaMember() == true;
    }
    private boolean isSelfResister(Member member, String username) {
        return !member.getInstaMember().getUsername().equals(username);
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

    private boolean checkAuthority(InstaMember actorInstaMember, InstaMember fromInstaMember) {
        return actorInstaMember.getId() == fromInstaMember.getId();
    }

    private boolean checkFromLikeablePersonCountLimit(Member member) {
        InstaMember instaMember = member.getInstaMember();
        return instaMember.getFromLikeablePeopleCount() < AppConfig.getMaxFromLikeablePeople();
    }

    private LikeablePerson getLikeablePerson(Long fromInstaId, Long toInstaId) {
        return likeablePersonRepository.findByFromInstaMemberIdAndToInstaMemberId(fromInstaId, toInstaId).orElse(null);
    }
}
