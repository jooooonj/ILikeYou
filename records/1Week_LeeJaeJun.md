# NWeek_LeeJaeJun.md

## Title: [1Week] 이재준

### 미션 요구사항 분석 & 체크리스트
- [x] 호감목록 페이지에서 특정 항목에서 삭제버튼을 누르면, 해당 항목은 삭제되어야 한다.

- [x] 삭제를 처리하기 전에 해당 항목에 대한 소유권이 본인(로그인한 사람)에게 있는지 체크해야 한다.

- [x] 삭제 후 다시 호감목록 페이지로 돌아와야 한다.

- [x] rq.redirectWithMsg 함수 사용

### 1주차 미션 요약
-- 삭제 기능<br>
-- 구글 로그인

---

**[접근 방법]**

삭제 기능
1. @PathVariable을 이용해 id값을 받아올 수 있었다.
2. 받아온 id값을 통해 LikeablePerson을 얻고 로그인되어 있는 Member와 연결되어 있는 InstaMember와 LikeablePerson의 fromInstaMember가 동일한지 체크했다.
3. 기존에 만들어져 있던 redirectWithMsg로 다시 /likeablePerson/list로 redirect 시켰다.

구글 로그인 기능
1. 구글에서 client id, secret 받아오기
2. Oauth2 service 는 이미 구현되어 있기 때문에 냅둔다.
3. application.yml 에 구글 로그인에 필요한 설정을 해준다.
4. 민감한 정보는 application.oauth.yml로 따로 빼준 후에 gitIgnore.에 등록한다.

**[특이사항]**

1. controller에서 @PathVariable을 통해 id값을 받아오고 나서 service에게 특정 기능을 수행시킬때 id값을 그대로 보내서 서비스 계층이 직접 객체를 찾아서 삭제하게 하는 것과, 컨트롤러 계층에서 해당 객체를 찾아온 후에 객체를 파라미터로 보내서 삭제하는 것. 어떤게 더 효율적인지 궁금했다.
<br>--> 크게 상관이 없고, 이번 미션에서 수행한 삭제 기능같은 경우는 deleteById 라는 메서드를 JpaRepository에서 적용한다. 그래서 그걸 사용하는 것도 하나의 방법이 될 수 있다.
2. 삭제가 잘 되었는지 테스트해보는 과정에서 존재하지 않는 LikeablePerson을 찾으려고 하면 DataNotFound 예외가 발생하도록 했다. Junit5에서 예외가 잘 발생하는지 찾아보던 중 Assertions의 import를 어떤 것을 쓰는지에 따라서 다른 방법을 사용해야 한다는 것을 알게 되었다.

