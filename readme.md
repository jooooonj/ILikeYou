## 프로젝트 소개


👤 개인프로젝트 (1명)

 **익명으로 호감을 표현하는 서비스**

인스타그램 계정을 연결하고, 인스타그램 계정을 기반으로 상대방에게 호감을 표시합니다.

익명을 보장하며 상대방과 나 사이에만 존재하는 특별한 ‘Jdenticon’ 으로 상대방을 식별합니다.

#### 🛠️ 기술스택

- JAVA, Gradle
- oauth2, thymleaf
- Spring Boot, Spring Security
- MySQL, JPA, Querydsl
- Git, Docker, Jenkins


#### 🛠️ 주요기능:

- 소셜 로그인 (카카오톡, 네이버, 구글)
- 인스타계정 연결
- 상대방에 호감 표현
- 내가 받은 호감 조회 (필터링, 정렬 가능)
- 알림 기능

![image](https://github.com/jooooonj/ILikeYou/assets/110995932/c5c74468-840d-4ea2-80f4-cabf5560ef47)
![image](https://github.com/jooooonj/ILikeYou/assets/110995932/2961345a-0d6f-4049-add8-1938d5d5abee)


## Todo

- [x] 회원가입 폼
    - [x] 로그인 상태에서 들어올 수 없다.
    - [x] 폼이 있어야 한다.
    - [x] input[name="username"] 필드가 있어야 한다.
    - [x] input[name="password"] 필드가 있어야 한다.
    - [x] 폼 체크
- [x] 회원가입 폼 처리
    - [x] 로그인 상태에서 들어올 수 없다.
    - [x] 유효성 체크를 해야 한다.
    - [x] member 테이블에 회원이 저장되어야 한다.
    - [x] 처리 후에 / 로 이동해야 한다. 302
    - [x] 회원가입이 완료되었습니다. /member/login 으로 302
- [x] 로그인 폼
    - [x] 로그인 상태에서 들어올 수 없다.
    - [x] 폼이 있어야 한다.
    - [x] input[name="username"] 필드가 있어야 한다.
    - [x] input[name="password"] 필드가 있어야 한다.
    - [x] 폼 체크
- [x] 로그인 폼 처리(스프링 시큐리티가 알아서 해줌)
    - [x] 세션에 데이터가 들어있는지 확인
- [x] 레이아웃 네비바 구현
    - [x] 로그인 버튼
    - [x] 회원가입 버튼
    - [x] 로그아웃 버튼
- [x] 로그인 후에는 내비바에 로그인된 회원의 username 이 보여야 한다.
- [x] 정적파일 정리
    - [x] 두루두루 사용되는 CSS 를 common.css 로 모으기
    - [x] 두루두루 사용되는 JS 를 common.js 로 모으기
- [x] toastMsg 에 ttl 기능 추가
- [x] 인스타그램 회원정보 입력
    - [x] 입력한 인스타그램 ID가 이미 존재하더라도, 그것의 성별이 아직 U 이면 연결가능
    - [x] 로그인한 사람만 가능
    - [x] 아이디
    - [x] 성별
- [x] 인스타그램 회원정보 입력 폼 처리
    - [x] 로그인한 사람만 가능
    - [x] 아이디
    - [x] 성별
    - [x] 회원과 인스타회원의 연결
    - [ ] 성공했을 때 호감표시 페이지로 이동
- [x] 본인이 좋아하는 사람 등록 폼
    - [x] 본인의 인스타그램 회원정보 입력을 완료한 사람만 가능
    - [x] 인스타그램 아이디
    - [x] 매력포인트(외모, 성격, 능력)
- [x] 본인이 좋아하는 사람 등록 폼 처리
    - [x] 아직 우리 서비스에 등록되지 않은 인스타 유저에게도 호감표시 가능
- [x] 젠킨스 자동 빌드 성공
- [x] 젠킨스 파이프라인 성공 (배포까지)

## 🚒 프로젝트를 진행하며 생긴 이슈 및 고민 정리

- [Querydsl을 사용해 동적쿼리 처리 (복잡도 제거, 가독성 향상)](https://developer-joon.tistory.com/204)
- [서비스 계층의 의존성을 낮추고, 주요 기능을 분리하기 위한 고민 (pub/sub)](https://developer-joon.tistory.com/162)
- [스프링 이벤트 트랜잭션 이슈 해결하기](https://developer-joon.tistory.com/205)
- [배포 과정에서 SSH 접속 오류 해결](https://developer-joon.tistory.com/172)
- [Docker와 Jenkins를 사용한 웹 애플리케이션 배포 과정](https://developer-joon.tistory.com/173)

## 💪 프로젝트 회고


지금까지 학습한 내용으로 만들었던 첫 프로젝트이다.

단순히 기능을 구현하기 위해서 코드를 작성한 것이 아닌, 연관 관계와 OOP의 관점에서 근거 있는 코드를 작성하기 위해 고민해보며 코드를 작성한 것이 개인적으로 만족스러웠던 프로젝트였다.

첫 프로젝트인 만큼 막막하기도 했고, 각종 오류를 만났지만 해결하기 위해 고민하고 검색한 과정들이 성장의 발판이 되었다고 생각한다.

그리고 개인적으로 다음 프로젝트를 진행하며 해보고 싶은 것이 생겼다.

이번 프로젝트는 서비스의 규모가 작고, 많은 사용자가 몰리지 않을 서비스라는 판단 하에 성능 부분을 크게 고려하지 않았다.

하지만 다음 프로젝트는 대규모 사용자가 이용할 것이라는 가정 하에 프로젝트를 진행해보고 성능 및 부하 테스트를 진행하며 코드를 개선하는 작업을 해야겠다는 목표가 생겼다.

계속해서 목표를 정하고 이뤄가는 성장형 개발자가 되기 위해 멈추지 않고 나아가야겠다.
