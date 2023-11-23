![Untitled](https://bow-hair-db3.notion.site/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F571a24a3-05f9-4ea5-b01f-cba1a3ac070d%2F77d8ee9c-7271-46f6-b4ea-02fda08cccf4%2Flogo.png?table=block&id=a9a2ec57-b655-45e4-be7d-a370c4649007&spaceId=571a24a3-05f9-4ea5-b01f-cba1a3ac070d&width=2000&userId=&cache=v2)
# GIS_Restaurant_Service

- 공공데이터를 활용한 지역 음식점 목록을 활용하여 사용자 위치에 맞는 메뉴를 추천해주는 서비스

# 목차

- [개요](#개요)
- [Skills](#skills)
- [프로젝트 진행 및 이슈 관리](#프로젝트-진행-및-이슈-관리)
- [API Swagger Docs](#api-swagger-docs)
- [Flow Chart](#flow-chart)
- [TIL(Today I Learn)](#til)
- [참여자](#참여자)

## 개요

- 본 서비스는 공공데이터를 활용하여, 지역 음식점 목록을 자동으로 업데이트 하고 이를 활용하여 사용자 위치에 맞게 맛집 및 메뉴를 추천하는 서비스입니다.
- 이를 통해 사용자에게 더 나은 다양한 음식 경험을 제공하고, 음식을 좋아하는 사람들 간의 소통과 공유를 촉진하려 합니다.

## Skills

- 언어 및 프레임워크: Java 17, Spring Boot 3.0
- 데이터베이스: MySQL
- 라이브러리 : Java-Mail-Sender, Bcrypt, Query DSL, Swagger, JWT

## 프로젝트 진행 및 이슈 관리

- 매일 Discord 팀 회의를 통해 이슈 및 진행 상황 공유
    - 기능 구현 정도, 요구사항 분석 등
    - 월, 수, 금 : 09:00 ~ 10:00
    - 화, 목 : 17:00 ~ 18:00
- 팀 Notion 페이지를 활용해 개발 하며 학습한 내용 공유

[Notion](https://www.notion.so/7243a00dd74d4cc995792bae6444678e?pvs=21)

## API Swagger Docs

![image](https://github.com/Teemo-Wanted/GIS_Restaurant_Service/assets/126079049/106d8ea9-4ba8-436d-bc32-96697255fcfe)

## Flow Chart

### 회원가입 & 로그인

![image](https://github.com/Teemo-Wanted/SNS_Feed_Service/assets/126079049/8cb32c42-b565-44dc-b9b1-743eddb8ed42) 

- ID/PW/Email로 회원가입 요청 및 JWT 발급
    - PW 제약 조건 3가지 적용 및 회원 Data 생성
        - 회원가입 기능 구현, PW 제약조건 3가지 설정
    - Email로 인증 코드 6자리 발송 및 JWT 발급 처리
        - 최초 로그인 시 인증코드 인증 필요

### 사용자 인증이 필요한 기능 동작 전 JWT 인증 방식

![image](https://github.com/Teemo-Wanted/SNS_Feed_Service/assets/126079049/32bf47f7-76a1-4d14-86d5-b43fb41af37b)

- 인증이 필요한 기능
    - 회원가입 / 로그인(JWT 발급)
- JWT를 활용한 화이트리스트 인증 방식 채택
    - 사용자 인증이 필요한 모든 요청 헤더에는 **JWT를 반드시 포함**해야 함
    - JWT가 유효한 토큰인지 확인
    - DB에 해당 사용자에 저장된 JWT가 맞는지 확인**(Refresh Token 방식이 아닌 화이트리스트 방식)**
        - JWT의  유효기간이 남았을 경우 탈취되었을 시 남은 일부 시간은 해당 사용자로 위장하여 사이트를 이용할 수 있는 단점 보안

### 사용자 설정 변경

![image](https://github.com/Teemo-Wanted/GIS_Restaurant_Service/assets/126079049/0e19bcf7-145c-4a6c-817d-94c1dfb0c463)

- 사용자 위치에 따른 위/경도, 알람 여부 업데이트
    - 사용자에게 도시명, 시군구 정보, 알람 여부를 받아 업데이트 합니다.
    - 도시와 시군구에 해당하는 위, 경도를 DB에서 조회한 후 업데이트 합니다.

### 사용자 정보 조회

![image](https://github.com/Teemo-Wanted/GIS_Restaurant_Service/assets/126079049/c19c6a94-0a06-4909-8a5c-6a23174be4ed)

- 사용자 정보를 조회합니다.
    - JWT 인증을 받은 사용자가 Spring Boot에 정보 조회 요청을 보내면 DB에서 사용자 정보를 찾아 반환합니다.
    - 반환 정보는 위와 같습니다.
    - [상세 설명 및 코드 - 사용자 정보 업데이트, 정보확인, 로그인 기능 구현](https://github.com/Teemo-Wanted/GIS_Restaurant_Service/pull/15)

### 데이터 수집 및 갱신

![image](https://github.com/Teemo-Wanted/GIS_Restaurant_Service/assets/126079049/7a2ed86c-e0d3-4595-bca7-9d135984c5d6)

- 공공데이터포탈에 API 요청을 보내 경기도 식당 정보를 수집합니다.
  - 매일 오전 9시 30분 스프링 스케줄러를 통해 요청을 보냅니다.
    - 오픈 API의 데이터 갱신 주기가 주간이기 때문 
  - 기존에 없는 데이터가 추가될 경우 새롭게 추가하고, 기존에 있던 데이터가 변경된 경우 더티체킹을 통해 update합니다.
    - 업데이트 속도 향상을 위해 종업원 수 등 업데이트가 크게 영향없는 데이터는 무시하고 중요 데이터(주소, 영업 상태(폐업인지 아닌지), 사업자명 등)만 변경합니다.
  - 중복 데이터를 막고자 businessPlaceNameAndAddress(가게명 + 주소)로 유니크키를 설정하였습니다. 
    - [상세 설명 및 코드 - 사용자 정보 업데이트, 정보확인, 로그인 기능 구현](https://github.com/Teemo-Wanted/GIS_Restaurant_Service/pull/14)
    - [상세 설명 및 코드 - 데이터 갱신 기능 구현](https://github.com/Teemo-Wanted/GIS_Restaurant_Service/pull/20)
   
### 리뷰(맛집 평가)
![image](https://github.com/Teemo-Wanted/GIS_Restaurant_Service/assets/126079049/b0e34037-349e-48f6-aa66-39792e6a55c4)

- 사용자가 식당 평가 데이터를 생성합니다.
  - 평가를 생성할 경우 식당의 평점 데이터가 update 됩니다.(평균 계산하여 업데이트)
  - [상세 설명 및 코드 - 맛집 평가 구현](https://github.com/Teemo-Wanted/GIS_Restaurant_Service/pull/21)

### 식당 목록 조회
![image](https://github.com/Teemo-Wanted/GIS_Restaurant_Service/assets/126079049/06149f45-765d-43b5-87cd-435a3345903a)

- 특정 위치부터 최대 거리 이내에 위치한 식당 목록을 페이지네이션하여 반환합니다.
  - 사용자로부터 위도, 경도, 거리, 정렬기준, 페이지 번호, 한 페이지당 개수 를 입력받습니다.
  - ST_Distance_Sphere 프로시저를 활용하여 사용자가 입력한 거리 이내의 식당들만 반환합니다.
  - 기본순은 거리순으로 정렬되지만, 평점 높은 순으로 정렬 기준을 둘 수도 있습니다.
  - [상세 설명 및 코드 - 식당 검색 기능](https://github.com/Teemo-Wanted/GIS_Restaurant_Service/pull/16)
  - [상세 설명 및 코드 - 식당 검색 쿼리 수정](https://github.com/Teemo-Wanted/GIS_Restaurant_Service/pull/23)
  - [상세 설명 및 코드 - 식당 평점 정렬 기준 추가](https://github.com/Teemo-Wanted/GIS_Restaurant_Service/pull/27)

### 식당 상세 정보 조회
![image](https://github.com/Teemo-Wanted/GIS_Restaurant_Service/assets/126079049/ba052eb2-3898-4c19-8d2a-69b4e542dfeb)

- 해당 식당의 상세 정보를 조회합니다.
  - 사용자로부터 식당 id를 URI에 포함하여 입력받습니다.
  - 식당에 포함된 모든 정보와, 리뷰 평점, 작성된 리뷰 목록을 보여줍니다.
  - [상세 설명 및 코드 - 식당 상세정보 조회](https://github.com/Teemo-Wanted/GIS_Restaurant_Service/pull/25)

### 디스코드 알림 메세지 발송 

![image](https://github.com/Teemo-Wanted/GIS_Restaurant_Service/assets/126079049/137a41be-0455-4c09-84cd-61242d9f8f89)

- 알람 설정한 사용자들에게 매일 11시 30분에 회원들이 설정한 위치의 1KM 근방 및 평점 높은 카테고리별 식당 3개씩 추천해줍니다.
  - 스프링 스케줄러를 활용해 11시 30분에 사용자 위치 1KM 이내이며, 평점 높은 순으로 각 식당 카테고리별 데이터를 3개씩 추출합니다.
  - 만일 데이터 추출에 실패할 경우에도 메세지를 발송합니다.

    ![image](https://github.com/Teemo-Wanted/GIS_Restaurant_Service/assets/126079049/c2bd84ac-6659-4c7a-af9d-1ec605c17d85)
    - case 1 : 사용자가 설정한 위도, 경도가 맞지 않은 경우
    - case 2 : 사용자가 설정한 위치 1KM 주변에 식당이 없는 경우
  - 데이터 추출에 성공한 경우 카테고리별로 발송합니다.  
![discord1](https://github.com/Teemo-Wanted/GIS_Restaurant_Service/assets/126079049/5143b69b-5702-45ea-8d5e-46651bf2f835)

- 실제로는 아래의 로직을 따라야 하나, 하나의 디스코드 채팅방을 활용하기에 경기도 안양시의 데이터를 활용하였습니다.
> 1. 알람 설정한 사용자 모두 추출
> 2. 반복문 돌며, 사용자가 설정한 위치의 각 카테고리별 식당 3개씩 추출(평점 높은순)
> 3. 각각의 사용자에게 메세지를 발송하는 Webhook 발송

- [상세 설명 및 코드 - 디스코드 채팅방 웹훅 보내어 자동으로 식당 추천기능](https://github.com/Teemo-Wanted/GIS_Restaurant_Service/pull/18)
- [상세 설명 및 코드 - [Refactor] discord webhook 카테고리별 평점 높은 3개씩 출력되도록 변경](https://github.com/Teemo-Wanted/GIS_Restaurant_Service/pull/29)

## TIL

| 키워드 | 링크 |
| :---: | :---: |
|  |  |
|  |  |
|  |  |

## 참여자

- [김태형](https://github.com/johan1103) : 식당 조회
- [박철현](https://github.com/CheorHyeon) : 사용자 설정 변경, 식당 상세 조회, 식당 조회 수정, 디스코드 웹훅 발송
- [정수현](https://github.com/walwaljj) : 데이터 파이프라인
- [이윤나](https://github.com/yoonnable) : 데이터 파이프라인
- [심형철](https://github.com/HyungcheolSim) : 식당 평가
