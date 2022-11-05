# Sample-Spring-Graphql-Project (feat.Netflix DGS)

<img src="https://user-images.githubusercontent.com/37768791/200105064-f6463423-7594-474c-a8e5-3852fb1b7fb9.png"/>

## Background

: 2022.11월 기준으로 들어가는 사이드프로젝트에 사용할 기술스택으로 graphql이 선정되었고 기존에 spring에 대한 지식을 쌓는게 목적에 있었으므로 DGS를 체택 후 공부용으로 제작했습니다.
기획서가 나오기까지 3일 남은 시점에서 짧은 기간동안 빠르게 학습하다보니 코드의 가독성은 약간 떨어질 수 있는 점 양해바랍니다.(나머진 사프하면서 학습....)


## ERD
: https://github.com/terry960302/sample-spring-webflux-pattern 에서 사용했던 같은 스키마 구조를 ORM으로 녹였습니다. 최대한 시간을 절약하기 위해.ㅎㅎ

## Need to Study

- [x] Setup DGS
    - [Issue] ~~graphql.java library version conflict issue~~ => resolved
- [x] Relations (1:1, 1:N, N:N) in JPA
    - Implement N:N relation using double OneToMany in (Posting & PostingImage & Image) if want to set extra column in
      JoinTable
- [x] Apply DataFetcher
- [x] Apply DataLoader in join column
    - [x] [Performance Tested] getAllPostings : 2 posting each has 500 images => fast enough
    - [x] [Performance Tested] getAllPostings : 100 postings each has 500 images => 1300ms
    - [x] [Performance Tested] getAllPostings(+pagination) : 10 postings each has 100 images(normal use case in
      production) => 111ms
- [x] Aggregate(Count) field
  - aggregate용 쿼리를 따로 제작(기존 쿼리안에 _count 필드로 녹이는 법은 추가 학습 필요)
- [x] Pagination(Pageable), OrderBy(Sort) etc.. Filter input
- [ ] Security
- [x] Instrumentation(logging, metrics) => check turnaround

## ENV
```yml
dgs:
  graphql:
    graphiql:
      enabled: true
    path: /graphql/**

spring:
  server:
    host: localhost
    port: 8000
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://localhost:5432/postgres
    username: postgres
    password: postgres
  jpa:
    open-in-view: true
    hibernate:
      ddl-auto: validate # create-drop, create, update, validate, none
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
      use-new-id-generator-mappings: false
    show-sql: true
    properties:
      hibernate:
        format_sql: true
      dialect: org.hibernate.dialect.PostgreSQLDialect

logging:
  level:
    org.hibernate.SQL: debug
    org.hibernate.type: trace # 
```

## References

- 2021 DEVIEW 쿠팡의 DGS 도입 배경
  - https://deview.kr/data/deview/session/attach/2_Domain%20Graph%20Service%EB%A5%BC%20%ED%99%9C%EC%9A%A9%ED%95%9C%20%EA%B4%91%EA%B3%A0%20%EC%84%9C%EB%B9%84%EC%8A%A4%EC%9D%98%20GraphQL%20API%20%EA%B5%AC%ED%98%84%20%EC%82%AC%EB%A1%80.pdf
- 공식문서
  - https://netflix.github.io/dgs/getting-started/
- 샘플 프로젝트
    - https://github.com/Netflix/dgs-examples-kotlin
    - https://github.com/nothingprogram/instagram-graphql
- 쿼리 필드 셀렉션 분기처리
  - https://piotrminkowski.com/2021/04/08/an-advanced-graphql-with-spring-boot-and-netflix-dgs/