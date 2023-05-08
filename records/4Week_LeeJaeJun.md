# 4Week_LeeJaeJun.md

## Title: [4Week] 이재준

### 미션 요구사항 분석 & 체크리스트

- [ ] 네이버클라우드플랫폼을 통한 배포, 도메인, HTTPS 까지 적용
- [ ] 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 성별 필터링기능 구현
- [ ] 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 호감사유 필터링기능 구현
- [ ] 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 정렬기능   
- [ ] 젠킨스를 통해서 리포지터리의 main 브랜치에 커밋 이벤트가 발생하면 자동으로 배포가 진행되도록
  

### 3주차 미션 요약

- 네이버클라우드플랫폼을 통한 배포, 도메인, HTTPS 까지 적용
- 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 성별 필터링기능 구현
- 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 호감사유 필터링기능 구현
- 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 정렬기능
- 젠킨스를 통해서 리포지터리의 main 브랜치에 커밋 이벤트가 발생하면 자동으로 배포가 진행되도록
---

**[접근 방법]**

- 네이버클라우드플랫폼을 통한 배포, 도메인, HTTPS 까지 적용
- 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 성별 필터링기능 구현
- 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 호감사유 필터링기능 구현
    - 성별과 호감사유 필터링 기능 @Query 사용해서 구현 
    - select L.* from likeable_person AS L
      join insta_member I
      on L.to_insta_member_id = I.id
      and I.id=4
      and attractive_type_code=2
      and I.gender='M'
- 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 정렬기능
- 젠킨스를 통해서 리포지터리의 main 브랜치에 커밋 이벤트가 발생하면 자동으로 배포가 진행되도록

**[특이사항]**




