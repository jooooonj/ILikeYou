# 4Week_LeeJaeJun.md

## Title: [4Week] 이재준

### 미션 요구사항 분석 & 체크리스트

- [x] 네이버클라우드플랫폼을 통한 배포, 도메인, HTTPS 까지 적용
- [x] 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 성별 필터링기능 구현
- [x] 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 호감사유 필터링기능 구현
- [x] 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 정렬기능
- [ ] 젠킨스를 통해서 리포지터리의 main 브랜치에 커밋 이벤트가 발생하면 자동으로 배포가 진행되도록
    
  

### 3주차 미션 요약

- 네이버클라우드플랫폼을 통한 배포, 도메인, HTTPS 까지 적용
- 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 성별 필터링기능 구현
- 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 호감사유 필터링기능 구현
- 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 정렬기능
- 젠킨스를 통해서 리포지터리의 main 브랜치에 커밋 이벤트가 발생하면 자동으로 배포가 진행되도록

---

**[접근 방법]**

네이버클라우드플랫폼을 통한 배포, 도메인, HTTPS 까지 적용
1. nginx 관리자 페이지에서 HTTPS 적용
2. iwantmyname에서 도메인 구매, dnszi에서 도메인 관리 (joooooj.com)
3. 가장 최신 커밋 push 후 pull -> checkout -> build -> 도커 이미지 생성 -> 실행

내가 받은 호감리스트(/usr/likeablePerson/toList)에서 성별 필터링기능 구현
&&
내가 받은 호감리스트(/usr/likeablePerson/toList)에서 호감사유 필터링기능 구현
1. LikeablePersonRepository에 @Query를 통해 jpql작성
2. 우선 attractiveTypeCode와 gender 값으로 필터링해서 List를 반환 (정렬 X)
---

내가 받은 호감리스트(/usr/likeablePerson/toList)에서 정렬기능
1. 처음엔 필터링과 같이 파라미터로 동적인 정렬을 구현하려고 했다.
2. 실패하고, 동적으로 필터링된 결과값에 정렬코드 수대로 쿼리메서드 구현 (중복이 발생했다..)
---

젠킨스를 통해서 리포지터리의 main 브랜치에 커밋 이벤트가 발생하면 자동으로 배포가 진행되도록
1.
---

**[특이사항]**
- 성별, 호감사유로 필터링을 구현할때 동적으로 쿼리를 짜기 위해서 JpaRepository가 기본적으로 제공해주는 기능이 아닌
Query 메서드를 jpql을 사용해서 구현해보았다. sql쿼리랑 거의 동일했지만 조건을 거는 과정이 낯설어서 살짝 헤맸다.
---
- 필터링은 생각보다 쉽게 구현했지만 정렬이 까다로웠다. 생각으로는 :sort 라는 Param을 선언해서 컨트롤러로부터 쿼리 파라미터로 넘겨받은 정렬 코드에 따라
서비스계층에서 정렬 조건을 String으로 구현해서 렌더링 시키면 쉽게 해결될 것이라고 생각했다.
하지만 계속 실패해서 찾아보니 보안때문에 안된다고 한다. 그래서 같은 결과를 반환하지만 정렬만 다르게 하는 쿼리 메서드를 정렬 코드 개수대로 만들어서 사용했다.
QueryDsl 을 배우면 해결될 것이라고 생각된다.
---





