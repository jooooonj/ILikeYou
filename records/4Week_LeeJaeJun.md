# 4Week_LeeJaeJun.md

## Title: [4Week] 이재준

### 미션 요구사항 분석 & 체크리스트

- [x] 네이버클라우드플랫폼을 통한 배포, 도메인, HTTPS 까지 적용
- [x] 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 성별 필터링기능 구현
- [x] 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 호감사유 필터링기능 구현
- [x] 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 정렬기능
- [x] 젠킨스를 통해서 리포지터리의 main 브랜치에 커밋 이벤트가 발생하면 자동으로 배포가 진행되도록
    
  

### 3주차 미션 요약

- 네이버클라우드플랫폼을 통한 배포, 도메인, HTTPS 까지 적용
- 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 성별 필터링기능 구현
- 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 호감사유 필터링기능 구현
- 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 정렬기능
- 젠킨스를 통해서 리포지터리의 main 브랜치에 커밋 이벤트가 발생하면 자동으로 배포가 진행되도록

---

**[접근 방법]**

- 네이버클라우드플랫폼을 통한 배포, 도메인, HTTPS 까지 적용
   - nginx 관리자 페이지에서 HTTPS 적용
   - iwantmyname에서 도메인 구매, dnszi에서 도메인 관리 (joooooj.com)
   - 가장 최신 커밋 push 후 pull -> checkout -> build -> 도커 이미지 생성 -> 실행


- 성별 && 호감사유 필터링 
  - LikeablePersonRepository에 쿼리메서드를 통해 jpql작성 (성별, 호감사유 필터링)

    ```java
    @Query("select L from LikeablePerson L inner join L.toInstaMember T inner join L.fromInstaMember F on T.id=:id where (:attractiveTypeCode is null or L.attractiveTypeCode=:attractiveTypeCode) and (:gender is null or F.gender=:gender) order by {정렬조건}
    ```

  - LikeablePersonController 에서는 FilterForm에 필터링 조건, 정렬 조건을 받아서 서비스계층에 전송
      ```java
      @PreAuthorize("isAuthenticated()")
      @GetMapping("/toList")
      public String showToList(Model model, FilterForm filterForm){

          InstaMember instaMember = rq.getMember().getInstaMember();

          // 인스타인증을 했는지 체크
          if (instaMember != null) {
              // 해당 인스타회원을 좋아하는 사람들 목록
              List<LikeablePerson> likeablePeople =
                      likeablePersonService.findByIdFilteredAndSortedList(instaMember ,filterForm.gender, filterForm.attractiveTypeCode, filterForm.sortCode);
              model.addAttribute("likeablePeople", likeablePeople);
          }

          return "usr/likeablePerson/toList";
      }
    ```

- 정렬
    - 필터링과 같이 파라미터로 동적인 정렬을 구현하려고 했다. (실패)
    - 정렬코드 수대로 쿼리메서드 구현 (코드중복)
    ```java
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
    ```
---

- 젠킨스 자동배포
    - 젠킨스의 작동 원리 공부
    - 도커 컨테이너 공부
    - IP와 port에 대한 이해
    - 큰 틀 : 리눅스서버 -> 도커 컨테이너 (jenkins, sql, gram, npm)
    - 서버의 접근권한(ACG) : 80,443(일반 npm 접근), 81(관리자 접근) 포트 (NPM에만 접근 가능)
    - NPM에서 설정해놓은 URL 통해 리다이렉트 포트를 식별하여 컨테이너에 접근
    - git과 jenkins web hook을 통해 커밋 이벤트 처리 (젠킨스 URL + /github/webhook)
    - Jenkins 커밋 이벤트를 받았을시 자동 실행할 쉘 명령어 등록 (파이프라인)

    - 파이프라인 코드
    ```shell
    pipeline {
    agent any
    
    tools {
        jdk 'openjdk-17-jdk'
    }
    
    stages {
        stage('Prepare') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/jooooonj/Mission_LeeJaeJun'
            }
            
            post {
                success { 
                    sh 'echo "Successfully Cloned Repository"'
                }
                failure {
                    sh 'echo "Fail Cloned Repository"'
                }
            }    
        }
        
        stage('Build Gradle Test') {
            
            steps {
                sh (script:'''
                    echo "Build Gradle Test Start"
                ''')

                dir('.') {
                    sh """
                    chmod +x gradlew
                    """
                }
                
                dir('.') {
                    sh """
                    ./gradlew clean build
                    """
                }
            }
            
            post {
                success { 
                    sh 'echo "Successfully Build Gradle Test"'
                }
                 failure {
                    sh 'echo "Fail Build Gradle Test"'
                }
            }    
        }
        
        stage('Docker Rm') {
            steps {
                sh 'echo "Docker Run Start"'
                sh """
                docker stop gram_1
                docker rm -f gram_1
                docker rmi -f gram
                """
            }
            
            post {
                success { 
                    sh 'echo "Docker Rm Success"'
                }
                failure {
                    sh 'echo "Docker Rm Fail"'
                }
            }
        }
        
        stage('Bulid Docker Image') {
            steps {
                sh 'echo " Image Bulid Start"'
                sh """
                docker build -t gram .
                """
            }
            
            post {
                success {
                    sh 'echo "Bulid Docker Image Success"'
                }

                failure {
                    sh 'echo "Bulid Docker Image Fail"'
                }
            }
        }
        
        stage('Docker Run') {
            steps {
                sh 'echo "Docker Run Start"'
                sh """
                docker run \
                  --name=gram_1 \
                  -p 8080:8080 \
                  --restart unless-stopped \
                  -e TZ=Asia/Seoul \
                  -d \
                  gram
                """
            }
            
            post {
                success {
                    sh 'echo "Docker Run Success"'
                }

                failure {
                    sh 'echo "Docker Run Fail"'
                }
            }
        }
    }
}
    ```
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
- 젠킨스를 통해 자동배포 할 때 의문점이 하나 있었다. 젠킨스가 자동으로 수행하게끔 설정해놓은 쉘 명령어는 깃의 최신 내용을
PULL 하고, Gradlew을 build 한 후에 기존 컨테이너를 내리고 새로 이미지를 만들어 컨테이너를 띄우는 것이다.
그런데 이 과정은 젠킨스 내부에서 이루어진다.
나의 의문점은 어떻게 리눅스에 있는 도커와 젠킨스 내부의 도커가 하나의 도커를 가르키고 있는지가 의문이었다.
```shell
  -v /var/run/docker.sock:/var/run/docker.sock
```
해답은 젠킨스를 소유한 컨테이너를 띠울때 위와 같은 볼륨 설정을 했기 때문이다.
위 볼륨설정은 젠킨스 내부의 docker 관련 데이터와 리눅스 서버의 docker 관련 데이터를 연결시켰음을 의미한다.
그래서 젠킨스 내부에서 도커를 이미지를 새로만들고 도커 컨테이너를 새로 띄움으로써 자동배포가 가능하게 된 것이다.







