# NWeek_LeeJaeJun.md

## Title: [1Week] 이재준

### 미션 요구사항 분석 & 체크리스트
- [x] 호감목록 페이지에서 특정 항목에서 삭제버튼을 누르면, 해당 항목은 삭제되어야 한다.

- [x] 삭제를 처리하기 전에 해당 항목에 대한 소유권이 본인(로그인한 사람)에게 있는지 체크해야 한다.

- [x] 삭제 후 다시 호감목록 페이지로 돌아와야 한다.

- [x] rq.redirectWithMsg 함수 사용


### 1주차 미션 요약
-- 삭제 기능

---

**[접근 방법]**

1. @PathVariable을 이용해 id값을 받아올 수 있었다.
2. 받아온 id값을 통해 LikeablePerson을 얻고 로그인되어 있는 Member와 연결되어 있는 InstaMember와 LikeablePerson의 fromInstaMember가 동일한지 체크했다.
3. 기존에 만들어져 있던 redirectWithMsg로 다시 /likeablePerson/list로 redirect 시켰다.



**[특이사항]**

1. controller에서 @PathVariable을 통해 id값을 받아오고 나서 service에게 특정 기능을 수행시킬때 id값을 그대로 보내서 서비스 계층이 직접 객체를 찾아서 삭제하게 하는 것과, 컨트롤러 계층에서 해당 객체를 찾아온 후에 객체를 파라미터로 보내서 삭제하는 것. 어떤게 더 효율적인지 궁금합니다.

