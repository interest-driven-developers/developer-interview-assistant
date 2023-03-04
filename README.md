# developer-interview-assistant
[토이 프로젝트] DIA - 개발자 면접 도우미
___

# 설명

## 핵심 가치

- 주니어 개발자들이 최대한 실전 같은 면접 연습을 할 수 있게 돕는다.

## 핵심 기능
- 일정 시간동안 랜덤하게 문제를 출제해주는 ‘실전 면접’ 기능
  - 글자가 아닌 음성으로 출제되며, 답변도 구두로 하여 실전성을 높임

## 기술 스택

- 어플리케이션
  - Kotlin, Spring Boot 2, Gradle
  - Spring data jpa
  - Oracle
  - Docker, Docker-compose
  - AWS EC2
- 테스트
  - Kotest
  - H2 database
  
## 아키텍쳐

헥사고날 아키텍쳐를 중심으로 설계 (의존성이 한쪽으로만 흐르도록 설계)
크게 `application`과 `infra`로 나눌 수 있음.

- `application` : 순수한 비즈니스 로직을 표현하며 캡슐화된 영역이고 기능적 요구사항에 따라 설계
- `infra` : 내부 영역에서 기술을 분리하여 구성한 영역이고 내부 영역 설계 이후 설계
 
#### 웹 요청 흐름
```
- `infra.web`의 컨트롤러는 웹 요청을 받는다.
- `application.service`에게 비지니스 로직 수행을 요구한다.
- 로직을 수행한 후에, `application.dto`로 데이터를 옮겨담아 반환한다.
```

## 개발 버전
- MVP (현재)

## 레퍼런스
- [Oracle Database 18c XE docker 설치](https://solo5star.tistory.com/17)
- [헥사고날(Hexagonal) 아키텍처 in 메쉬코리아](https://mesh.dev/20210910-dev-notes-007-hexagonal-architecture/)
- [우아한 테크코스 지원 서버 소스 코드](https://github.com/woowacourse/service-apply)
