<div align="center">
  <img src="https://github.com/soonhankwon/gold-digger-api/assets/113872320/e053fc62-976c-4890-9db8-de6c526cb902" alt="logo" align="center" width="60%" />  
</div>

# 예산 관리 애플리케이션 백엔드 API
- 본 서비스는 사용자들이 개인 재무를 관리하고 지출을 추적하는 데 도움을 주는 애플리케이션 입니다.
- 이 앱은 사용자들이 예산을 설정하고 지출을 모니터링하며 재무 목표를 달성하는 데 도움이 됩니다.
<br/>

## Table Of Contents
- [유저스토리](#유저스토리)
- [기술스택](#기술스택)
- [아키텍처](#아키텍처)
- [API 명세서](#api-명세서)
- [ERD](#erd)
- [프로젝트 진행 및 이슈관리](#프로젝트-진행-및-이슈관리)
- [구현과정(설계 및 의도)](#구현과정(설계-및-의도))
- [핵심문제 해결과정 및 전략](#핵심문제-해결과정-및-전략)
<br/>

## 유저스토리
- 유저는 본 사이트에 들어와 회원가입을 통해 서비스를 이용합니다. 
- 예산 설정 및 설계 서비스
  - `월별` 총 예산을 설정합니다.
  - 본 서비스는 `카테고리 별` 예산을 `설계(=추천)`하여 사용자의 과다 지출을 방지합니다.
- 지출 기록
  - 사용자는 `지출`을  금액, 카테고리, 메모, 지출일시를 지정하여 `등록` 합니다. 언제든지 `수정` 및 `삭제` 할 수 있습니다.
- 지출 컨설팅
  - `월별` 설정한 예산을 기준으로 오늘 소비 가능한 `지출` 을 알려줍니다.
  - 매일 발생한 `지출` 을 `카테고리` 별로 안내받습니다.
- 지출 통계
  - `지난 달 대비` , `지난 요일 대비`,  `다른 유저 대비` 등 여러 기준 `카테고리 별` 지출 통계를 확인 할 수 있습니다.
<br/>

## 아키텍처
![gold-digger-arch drawio](https://github.com/soonhankwon/gold-digger-api/assets/113872320/79cc3e47-bc68-4579-b7a7-5148971c79f4)

## 기술스택
### 언어 및 라이브러리
- Java 17 Amazon Corretto
- SpringBoot 3.1.5
- Spring Data JPA 3.1.5
- Spring Validation 3.1.5
- Querydsl 5.0.0
- Spring Data Redis 3.1.5
- Redisson 3.24.3
- Spring WebFlux 6.0.13
- Spring Security 6.1.5
- JJWT 0.12.3
- Swagger v3 2.2.9
### 데이터베이스
- MySQL 8.0.33
- Redis 7.0.8
### DevOps
- AWS EC2
  - 서버 비용문제로 인스턴스는 `중지`시켜놓은 상태입니다. `배포`스크린샷 첨부합니다.
  - ![deploy-ubuntu](https://github.com/soonhankwon/gold-digger-api/assets/113872320/73638ffe-ec04-42ef-9f96-55b1ad425bea)   
- AWS RDS
- GitHub Actions
- Docker
<br/>

## api 명세서
- Swagger : http://localhost:8080/swagger-ui/index.html#/
  - 애플리케이션 구동 후 위 링크로 스웨거 api명세서 확인가능합니다.
- `로그인 API`는 시큐리티에서 제공하도록 구현, 스웨거로 문서화되지 않아 아래에 표기했습니다.
  ```plain
  - url: /sign-in
  - method: POST
  - body: { username: "tester123", password: "password1!" }
  ```  
<br/>

## erd
![erd](https://github.com/soonhankwon/gold-digger-api/assets/113872320/4ea3f7a3-7e02-4149-ab24-2e7972325af0)
<br/>

## 프로젝트 진행 및 이슈관리
- 프로젝트 진행시 `전체적인 진행 현황` 과 `시간을 효율적으로 관리`하기 위해서 `깃허브 프로젝트`를 활용했습니다.
  - [프로젝트 링크 - Click!](https://github.com/users/soonhankwon/projects/5)
  <img src="https://github.com/soonhankwon/gold-digger-api/assets/113872320/acbb7af1-e1eb-4566-ac3a-9335d1d25df3" align="center" height=250px width="40%" />
  <img src="https://github.com/soonhankwon/gold-digger-api/assets/113872320/171abedb-bab7-4de4-9526-392d591191cd" align="center" height=250px width="40%" />
- 프로젝트에 필요한 구현해야할 기능 등 을 `이슈`발행하여 `시작 & 데드라인` 기간을 설정, `로드맵`에서 한눈에 파악할 수 있도록 했습니다.
  - 이슈형식이 달라 알아보기 힘든 경우를 방지하도록 `이슈템플릿`을 등록하여 통일된 형식으로 이슈를 관리했습니다.
- `칸반보드`도 연동되기 때문에 TODO, IN PROGRESS, DONE으로 프로젝트의 `티켓`들을 관리했습니다.  
- 개인 프로젝트이기 때문에 브랜치 관리전략은 Master(Production) - Dev로 간단하게 가져갔습니다.
- Dev 브랜치를 소스로 `이슈브랜치`에서 작업 & PR & MERGE 과정으로 진행했습니다.
- 하루에 한번 기준으로 Dev to Master(Production)로 PR & MERGE를 진행했습니다.
<br/>

## 구현과정(설계 및 의도)
### 사용자 회원가입 & 로그인 고려사항
---
1. 회원가입 API
<details>
<summary><strong> 서비스에 필요한 회원 정보를 잘 모델링했나? - Click! </strong></summary>
<div markdown="1">
  
- 본 서비스는 유저 고유 정보가 크게 사용되지 않아 간단히 구현되어 있습니다.
- 계정명(username): 구글 가이드라인에 따라 `6자이상 ~ 30자이하`로 설정하였습니다.
- 패스워드(password): 패스워드는 최소 `8글자이상` `숫자, 문자, 특수문자를 포함`하도록 설정하였습니다.
- 디스코드웹훅url(discordUrl): 알람서비스를 위한 `개인 디스코드 웹훅 url`입니다.
  - 유저가 입력하지 않는 경우 기본값으로 `NONE`으로 DB에 저장되며 이후 알람서비스에서 NONE이 아니면서 알람설정을한 유저에게만 알람서비스를 제공합니다.
  - DB에 null로 넣지 않은 이유는 기본적으로 MySQL의 null은 `unknown` 이며 비교/논리연산시 결과로 true, false, unknown을 가지기때문에 예상하지 못한 연산결과를 피하고 싶었습니다. 
- 알람수신여부(subscribeNotification): 알람서비스 수신 여부
  - 서비스에 디스코드 웹훅을 통한 `알람서비스`가 포함되어있습니다.
  - 해당 수신여부를 꼭 설정해야 불필요한 DB스캔 및 사용자 경험을 개선시킬수 있다고 생각했습니다.
</div>
</details>

<details>
<summary><strong> 회원가입 요청 Validation CODE - Click! </strong></summary>
<div markdown="1">       

````java
@Schema(description = "회원 가입 요청 DTO")
public record UserSignupRequest(

        @NotBlank(message = "계정명은 하나 이상의 공백이 아닌 문자를 포함해야 합니다.")
        @Size(min = 6, max = 30, message = "계정명은 6자이상 30자이하로 작성해야합니다.")
        @Schema(description = "계정명", example = "username")
        String username,

        @NotBlank(message = "패스워드는 하나 이상의 공백이 아닌 문자를 포함해야 합니다.")
        @Size(min = 8, message = "패스워드는 최소 8글자 이상이어야 합니다.")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!]).*$",
                message = "패스워드는 숫자, 문자, 특수 문자를 포함해야 합니다."
        )
        @Schema(description = "패스워드", example = "password1!")
        String password,

        @Schema(description = "알람수신여부", example = "true")
        @NotNull(message = "알람수신여부는 true 또는 false 여야 합니다.")
        Boolean subscribeNotification,

        @Schema(description = "디스코드url", example = "/api/webhooks/...")
        String discordUrl
) {
}
````
</div>
</details>

<details>
<summary><strong> 패스워드는 잘 암호화 되어있나? - Click! </strong></summary>
<div markdown="1">    
  
- 인증과정에서 가장 중점을 뒀던 점은 시큐리티에서 제공하는 흐름과 기능을 최대한 맞춰서 이용하는 것이었습니다.
- 이유는 시큐리티는 `보안에 전문적`인 라이브러리이기 때문에 `보안성`을 기본적으로 보장해주기 때문입니다.
- 시큐리티에서 제공하는 `PasswordEncoderFactories`의 createDelegatingPasswordEncoder 메서드를 통해 인코더를 빈으로 등록했습니다.
  - 인증, 인가에 대해서는 최대한 시큐리티에서 제공하는 흐름대로 구현하는것이 `안정성`면에서 좋다고 생각했습니다.
- 시큐리티에 위임하는 인코더를 통해 `패스워드를 암호화`하고 로그인시에도 시큐리티가 인증절차에서 해당 인코더로 패스워드 검증을 수행합니다.
</div>
</details>

<details>
<summary><strong> 패스워드 인코딩 CODE - Click! </strong></summary>
<div markdown="1">       

````java
    @Transactional
    public String createUser(UserSignupRequest request) {
        if(isExistsUsername(request.username())) {
            throw new ApiException(CustomErrorCode.USERNAME_ALREADY_EXISTS);
        }
        // 등록된 패스워드 인코더의 encode를 사용하는 함수생성
        Function<String, String> encodeFunction = passwordEncoder::encode;
        // 함수를 생성자 파라미터로 넘겨 요청의 패스워드를 인코딩합니다.
        User user = new User(request, encodeFunction);
        userRepository.save(user);
        return "created";
    }
````
</div>
</details>

2. 로그인 API
<details>
<summary><strong> 로그인시 JWT가 잘 발급이 되나? - Click! </strong></summary>
<div markdown="1">    
  
- 시큐리티의 `AuthenticationManager`와 `JwtAuthenticationFilter`를 통해 로그인을 수행하도록 했습니다.
- JwtAuthenticationFilter 인증과정을 통해 로그인이 정상적이라면 `헤더`에 `AccessToken`과 `RefreshToken`을 발급합니다.
</div>
</details>

<details>
<summary><strong> AccessToken과 RefreshToken 발급 CODE - Click! </strong></summary>
<div markdown="1">       

````java
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {
        UserPrincipal principal = (UserPrincipal) authResult.getPrincipal();

        String username = principal.getUsername();
        String accessToken = jwtProvider.generateAccessToken(principal.getId(), username);
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        String refreshToken = jwtProvider.generateRefreshToken(username);
        redisTokenRepository.saveRefreshToken(refreshToken, username);
        response.setHeader("Refresh", "Bearer " + refreshToken);
    }
````
</div>
</details>

- 이후 헤더의 AccessToken 검증을 통해 url `인가`가 이루어지며, AccessToken 만료시 RefreshToken을 통해 재발급을 받습니다.
- RefreshToken은 DB/IO를 줄이기위해 `캐싱`을 해놓고 사용 & `만료시간`이 지나면 삭제하기위해 `Redis`를 활용했습니다.

<details>
<summary><strong> JWT 재발급 CODE - Click! </strong></summary>
<div markdown="1">       

````java
    // 토큰 재발급 서비스 레이어
    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        // 유저의 리프레시 토큰 검증 후 기존 리프레시토큰 캐싱 삭제 & user 객체를 가져옵니다.
        User user = verifyRefreshTokenExists(request);
        String username = user.getUsername();
        
        String newAccessToken = jwtProvider.generateAccessToken(user.getId(), username);
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken);

        String newRefreshToken = jwtProvider.generateRefreshToken(username);
        // 새로운 리프레시 토큰을 캐싱합니다.
        redisTokenRepository.saveRefreshToken(newRefreshToken, username);
        response.addHeader("Refresh", newRefreshToken);
    }
````
</div>
</details>
<br/>

### 예산설정 및 설계 API 고려사항
---
1. 지출 카테고리
- 지출 카테고리는 `식비`, `쇼핑`, `교통`, `오락`, `자기계발`, `의료`, `경조사`, `투자`, `여행`, `기타`로 구성하였습니다.
- Daily Pay, Daak 가계부(App Store)를 리서칭한 결과 `공통적으로 겹치는 카테고리`였고, 생산성을 위해 초기에는 큰 카테고리로 구현 & 니즈에 따라 세부 카테고리를 추가하는 방향으로 설정했습니다.
2. 지출 카테고리 목록 조회 API
- 카테고리 목록은 `서비스측에서 설정한 지출 카테고리`를 제공합니다. 따라서 DB에는 사측의 결정에 의해서만 카테고리가 업데이트됩니다.
- 따라서 지출 카테고리 목록 조회 API는 초기에 조회결과를 Redis에 `캐싱`을 해두고 이후에는 캐싱된 결과를 조회하도록 하여 `불필요한 DB I/O를 개선`했습니다.

<details>
<summary><strong> 카테고리 목록조회 Redis 캐싱 CODE - Click! </strong></summary>
<div markdown="1">       

````java
    @Cacheable(value = "expenditure:category:1:collections", cacheManager = "cacheManager")
    public List<ExpenditureCategoryResponse> getExpenditureCategories() {
        List<ExpenditureCategory> categories = expenditureCategoryRepository.findAll();

        return categories.stream()
                .map(ExpenditureCategoryResponse::toResponse)
                .collect(Collectors.toList());
    }
````
</div>
</details>

3. 유저 예산설정 API  
- 유저예산 설정(POST): `월단위`로 `예산` 을 설정합니다. 예산은 `카테고리` 를 필수로 지정합니다.
  - `유저예산 테이블`은 `유저`와 `지출카테고리`를 `FK`로 가지고 필요한 경우 JOIN해서 로직을 수행할 수 있도록 `1 : N 연관관계`를 설정했습니다. 
  - 예산은 액수, 년, 월을 요청받아 만들어집니다.
- 유저예산 수정(PATCH): 사용자는 예산의 액수, 년, 월, 지출 카테고리를 `변경`할 수 있도록 구현했습니다.
- 년, 월, 지출 카테고리를 변경했을 경우 `기존 예산과 중복`된다면?
  - 유저예산 테이블에 설정년월을 의미하는 `plannedYearMonth`라는 datetime 데이터 타입의 컬럼을 만들었습니다.
  - plannedYearMonth는 ex)`2023-11-01 00:00:00.000000`로 DB에 저장됩니다.
  - 예산을 변경하고 유저에게 이미 plannedYearMonth와 카테고리가 같은 설정예산이 있다면 `예외처리`합니다.
<details>
<summary><strong> 기존 예산과 중복되는 경우 예외처리 CODE - Click! </strong></summary>
<div markdown="1">       

````java
    @Transactional
    public String updateUserBudget(String username, Long userBudgetId, UserBudgetUpdateRequest request) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ApiException(CustomErrorCode.USER_NOT_FOUND_DB));

        UserBudget userBudget = userBudgetRepository.findById(userBudgetId)
                .orElseThrow(() -> new ApiException(CustomErrorCode.USER_BUDGET_NOT_FOUND_DB));

        ExpenditureCategory category = expenditureCategoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ApiException(CustomErrorCode.CATEGORY_NOT_FOUND_DB));

        // 유저 예산의 카테고리, 년, 월, 예산총액을 수정할 수 있다.
        // 카테고리 ID가 같은 경우는 예외처리 검증 X
        if (userBudget.isCategoryIdSameAsRequestCategoryId(request)) {
            userBudget.update(request, category);
            return "updated";
        }

        userBudget.update(request, category);
        // 업데이트후 중복된 월 and 카테고리 유저 예산이 DB에 있다면 예외처리한다.
        validateDuplicatedUserBudget(user, userBudget, category);
        return "updated";
    }
````
</div>
</details>

4. 예산설계(=추천) API
- 카테고리 별 예산 설정에 어려움이 있는 사용자를 위해 `예산 비율 추천 기능` API입니다.
- 카테고리 지정 없이 총액 (ex. 100만원) 을 입력하면, 카테고리 별 예산을 자동 생성합니다.
- 총액입력은 `PathVariable`로 url을 활용했습니다.
  - url path: api/budget/{budget}/recommend
#### 예산추천 조회(GET):
- 유저들이 설정한 카테고리 별 예산을 통계하여, 평균적으로 40% 를 `식비`에, 30%를 `주거` 에 설정 하였다면 이에 맞게 추천합니다.
- 10% 이하의 카테고리들은 모두 묶어 `기타` 로 제공합니다.(8% 문화, 7% 레져 라면 15% 기타로 표기) 
- 원하는 타입으로 통계결과를 조회할 수 있도록 `@QueryProjection`을 사용하여 QFile을 생성해서 `select절`에 사용했습니다.
- Querydsl의 JPAExpressions `Subquery`를 활용하여 유저들의 설정예산 총합에서 카테고리 별로 `groupBy`된 유저들의 카테고리별 예산의 합을 나누어서 비율을 구합니다.
- Having절에서 `10프로 이하인 카테고리는 제외`해주고 비즈니스로직에서 기타 카테고리를 따로 추가해주도록 구현했습니다.
  - `1.0D - 조회한 통계결과의 비율의 합`의 결과가 곧 `기타 카테고리`로 들어갈 값이기 때문입니다. 
<details>
<summary><strong> 유저예산 카테고리 별 평균 설정 비율 통계 쿼리 & 비즈니스 로직 CODE - Click! </strong></summary>
<div markdown="1">       

````java
    // 유저예산 카테고리 별 평균 설정 비율 통계 응답 record
    public record UserBudgetAvgRatioByCategoryStatisticResponse(
            ExpenditureCategory category,
            Double avgRatio
    ) {
    // QueryProjection을 사용해서 QFile생성 후 select절에서 사용합니다.
    @QueryProjection
    public UserBudgetAvgRatioByCategoryStatisticResponse {
        }
    }

    // 쿼리 구현 - Querydsl
    @Override
    public List<UserBudgetAvgRatioByCategoryStatisticResponse> statisticUserBudgetAvgRatioByCategory() {
        return queryFactory.select(
                        new QUserBudgetAvgRatioByCategoryStatisticResponse(expenditureCategory,
                                userBudget.amount.sum().divide(
                                                JPAExpressions.select(userBudget.amount.sum())
                                                        .from(userBudget)
                                                        .where(userBudget.expenditureCategory.eq(expenditureCategory)))
                                        .doubleValue()))
                .from(userBudget)
                .join(userBudget.expenditureCategory).on(userBudget.expenditureCategory.eq(expenditureCategory))
                .groupBy(expenditureCategory)
                .having(userBudget.amount.sum().divide(
                                JPAExpressions.select(userBudget.amount.sum())
                                        .from(userBudget)
                                        .where(userBudget.expenditureCategory.eq(expenditureCategory)))
                        // 10프로 이하인 카테고리는 제외합니다.
                        .doubleValue().gt(0.10))
                .fetch();
    }

    // 서비스 레이어
    public List<UserBudgetRecommendation> getUserBudgetByRecommendation(Long budget) {
        List<UserBudgetAvgRatioByCategoryStatisticResponse> statisticResponses =
                userBudgetRepository.statisticUserBudgetAvgRatioByCategory();

        List<UserBudgetRecommendation> res = new ArrayList<>();

        // 루프를 돌면서 비율을 뺴줘서 남은 비율은 기타로 들어갑니다.
        AtomicReference<Double> totalRatio = new AtomicReference<>(1.0D);
        statisticResponses.forEach(i -> {
            UserBudgetRecommendation userBudgetRecommendation = new UserBudgetRecommendation(i.category(), Math.round(budget * i.avgRatio()));
            res.add(userBudgetRecommendation);
            totalRatio.set(totalRatio.get() - i.avgRatio());
        });

        // 비율이 남아있다면 기타로 들어갑니다.
        if (hasRemainingTotalRatio(totalRatio)) {
            ExpenditureCategory etcCategory = new ExpenditureCategory(10L, "기타");
            UserBudgetRecommendation userBudgetRecommendation = new UserBudgetRecommendation(etcCategory, Math.round(budget * totalRatio.get()));
            res.add(userBudgetRecommendation);
        }
        return res;
    }
````
</div>
</details>

#### 추천예산 설정(POST): 유저는 예산 추천 기능으로 입력 된 값들을 필요에 따라 수정(화면에서) 한 뒤 이를 저장(=추천 예산설정 API)합니다.
- `중복된 해당 월 & 카테고리의 유저 예산`이 존재할 경우?
  - `추천된 List`로 요청을 받는 추천 예산설정API의 비즈니스 로직 루프에서 유저예산 객체생성후 `기존 중복된 예산이 있는지 검증`하여 `예외처리`하도록 했습니다.  
<details>
<summary><strong> 기존 예산과 중복되는 경우 예외처리 CODE - Click! </strong></summary>
<div markdown="1">       

````java
    @Transactional
    public String createUserBudgetByRecommendation(String username, List<UserBudgetRecommendation> request) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ApiException(CustomErrorCode.USER_NOT_FOUND_DB));

        request.forEach(i -> {
            UserBudget userBudget = new UserBudget(user, i.category(), i.amount());
            // 중복되는 예산이 있는지 검증하여 예외처리합니다.
            validateDuplicatedUserBudget(user, userBudget, i.category());
            userBudgetRepository.save(userBudget);
        });

        return "created by recommendation";
    }
````
</div>
</details>

### 지출기록 고려사항
---
1. 지출
<details>
<summary><strong> 서비스에 필요한 지출 정보를 잘 모델링했나? - Click! </strong></summary>
<div markdown="1">       

- `지출 일시`, `지출 금액`, `카테고리` 와 `메모` 를 입력하여 생성합니다.
- 지출금액(amount): 지출액은 1보다 작을수 없도록 설정했습니다.
- 지출일시(expenditureDateTime): 지출일시로 `yyyy-MM-dd HH:00` 패턴으로 요청을 받고 DB에 `datetime`으로 저장합니다.
  - 프론트에서 `yyyy-MM-dd HH:00`로 `지출일시`를 생성해서 보낸다고 가정했습니다. 
- 메모(memo): 지출액에 대한 메모로 `간단한 메모`임의 특성상 `100자 이하`로 제한을 두었습니다.
- 지출상태(expenditureStatus): 서비스에 `지출을 합계에서 제외`하는 API가 있어 `ENUM`으로 `상태`를 정의하였습니다. 
- `지출 테이블`은 `유저`와 `지출카테고리`를 `FK`로 가지고 필요한 경우 `JOIN`해서 로직을 수행할 수 있도록 `연관관계`를 설정했습니다.

</div>
</details>

<details>
<summary><strong> 지출요청 Validation CODE - Click! </strong></summary>
<div markdown="1">       

````java
@Schema(description = "지출 요청 DTO")
public record ExpenditureRequest(
        //'yyyy-MM-dd HH:00' 형식 1차 validation -> ssss-wm-ei 2m:00 으로
        @Schema(description = "지출 일시", example = "2023-11-14 19:30")
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:00", message = "yyyy-MM-dd HH:00 형식이어야 합니다.")
        String dateTime,

        @Schema(description = "지출액", example = "15000")
        @Min(value = 1, message = "지출액은 1보다 작을수 없습니다.")
        Long amount,

        @Schema(description = "메모", example = "점심")
        @Size(max = 100, message = "메모는 100자 이하로 작성되어야 합니다.")
        @NotNull(message = "메모는 null 일수 없습니다.")
        String memo
) {
}
````
</div>
</details>

2. 지출 CRUD
- 지출을 `생성`, `수정`, `읽기(상세)`, `읽기(목록)`, `삭제` , `합계제외` 할 수 있습니다.
- CRUD 공통 고려사항 : `권한`
  - 서비스에서 지출에 대한 `읽기, 수정, 삭제 권한`은 `해당 유저`만 가지고있도록 합니다. 따라서 유저의 지출인지 검증하여 다르다면 `예외처리`합니다.
  - 해당 `인가` 절차는 `JWT(AccessToken)`을 검증하여 SecurityContextHolder에 `전역적`으로 설정된 유저정보를 비즈니스 로직에서 `검증`하도록 했습니다.
#### 지출생성 API(POST)
- 유저와 지출 카테고리를 불러와 둘을 FK로 가지는 지출을 생성합니다.
#### 지출수정 API(PATCH)
- 대상 지출ID를 `PathVariable`로 받도록 설계했습니다.
- 지출 카테고리를 변경하는 경우?
  - 업데이트 요청에 카테고리ID를 넣도록 설계했습니다.
  - 카테고리ID가 `기존과 동일한 경우` & `다른 경우`로 판별하도록 구현했습니다.  
#### 지출상세 읽기 API(GET)
- 대상 지출ID를 `PathVariable`로 받도록 설계했습니다.
- 지출의 `상세 정보`를 모두 반환합니다.
#### 지출목록 읽기 API(GET)
- 하나의 엔드포인트에서 `Parameter`에 따라 `필터`를 적용한 결과를 조회할 수 있도록 구현했습니다.
<details>
<summary><strong> 지출목록 읽기 엔드포인트 CODE - Click! </strong></summary>
<div markdown="1">       

````java
    @Operation(summary = "지출 목록조회 API", description = "필수적으로 기간으로 조회, 모든 내용의 지출 합계, 카테고리별 지출 합계 반환 [특정 카테고리 ID 포함시 해당 카테고리로만 조회]")
    @GetMapping
    public ResponseEntity<ExpenditureByUserResponse> getExpendituresByUser(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                           @Parameter(description = "시작일", required = true)
                                                                           @RequestParam(value = "start") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
                                                                           @Parameter(description = "종료일", required = true)
                                                                           @RequestParam(value = "end") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
                                                                           @Parameter(description = "특정 카테고리 ID")
                                                                           @RequestParam(value = "categoryId", required = false) Long categoryId,
                                                                           @Parameter(description = "최소, 최대지출 포함여부")
                                                                           @RequestParam(value = "minAndMax", required = false) Boolean hasMinAndMax) {
        ExpenditureByUserRequest request = new ExpenditureByUserRequest(start, end, categoryId, hasMinAndMax);
        ExpenditureByUserResponse res = expenditureService.getExpendituresByUser(userPrincipal.getUsername(), request);
        return ResponseEntity.ok().body(res);
    }
````
</div>
</details>

- `읽기(목록)` 은 아래 기능을 가지고 있습니다.
  - 필수적으로 `기간` 으로 조회 합니다.
  - `yyyy-MM-dd` 패턴의 LocalDate를 RequestParam(required=true)로 받는 `start, end`로 기간을 설정합니다.
<details>
<summary><strong> 기간에 제한을 두었는지, 시작일과 종료일이 논리적으로 맞는지? - Click! </strong></summary>
<div markdown="1">       

- 기간에 `제한`을 두지 않는다면 DB를 풀스캔할 수도 있어 서버에 치명적인 영향을 줄 수 있다고 생각했습니다.
- `최대 30일`로 제한을 두었고 기간이 30일이 넘는다면 `예외처리`합니다.
- 종료일이 시작일보다 전이라면 `예외처리`합니다.
- 또한 시작일이 `서비스 시작일 전`이라면 `예외처리`하여 예상하지 못한 결과를 예방했습니다.
- 해당 기간 조회된 모든 내용의 `지출 합계` , `카테고리 별 지출 합계` 를 같이 반환합니다.
- 유효성 검사로직은 `Fail Fast`를 위해 RequestParam을 받아 생성되는 `DTO객체 내부`에서 하도록 캡슐화했습니다.
</div>
</details>
  
<details>
  <summary><strong> 유저 지출 요청 Validation CODE - Click! </strong></summary>
  <div markdown="1">       

  ````java
      public ExpenditureByUserRequest(LocalDate start, LocalDate end, Long categoryId, Boolean hasMinAndMax) {
          this.start = start.atStartOfDay();
          this.end = end.atStartOfDay();
          this.categoryId = categoryId;
          this.hasMinAndMax = Objects.requireNonNullElse(hasMinAndMax, false);
          validateDate(start, end);
      }

      private void validateDate(LocalDate start, LocalDate end) {
          LocalDate dayOfServiceStart = LocalDate.of(2023, Month.JANUARY, 2);
          if (start.isBefore(dayOfServiceStart) || end.isBefore(dayOfServiceStart)) {
              throw new ApiException(CustomErrorCode.INVALID_PARAMETER_DATE_NONE_SERVICE_DAY);
          }
          if (start.isAfter(end)) {
              throw new ApiException(CustomErrorCode.INVALID_PARAMETER_START_DATE);
          }
  
          long period = ChronoUnit.DAYS.between(start, end);
          if (period >= 30) {
              throw new ApiException(CustomErrorCode.INVALID_EXPENDITURES_GET_DURATION);
          }
      }
  ````
</div>
</details>

- 특정 `카테고리`로만 조회.
  - RequestParam(required=false)로 받는 `categoryId`가 있다면 `특정 카테고리`로만 조회합니다.
  - Querydsl을 사용 카테고리 ID가 있다면 특정 카테고리로만 결과 반환 없다면 전체 카테고리를 모두 반환하도록 구현했습니다.
<details>
<summary><strong> 카테고리 & 지출금액 통계 쿼리 CODE - Click! </strong></summary>
<div markdown="1">       

  ````java
      @Override
      public List<ExpenditureCategoryAndAmountResponse> statisticExpenditureCategoryAndAmount(User user, ExpenditureByUserRequest request) {
        if (request.getCategoryId() != null) {
            return queryFactory.select(new QExpenditureCategoryAndAmountResponse(expenditureCategory.id, expenditureCategory.name, expenditure.amount.sum()))
                    .from(expenditure)
                    .where(expenditure.user.eq(user)
                            .and(expenditureCategory.id.eq(request.getCategoryId()))
                            .and(expenditure.expenditureDateTime.between(request.getStart(), request.getEnd().plusDays(1L)))
                            .and(expenditure.expenditureStatus.eq(ExpenditureStatus.INCLUDED)))
                    .fetch();
        }
        return queryFactory.select(new QExpenditureCategoryAndAmountResponse(expenditureCategory.id, expenditureCategory.name, expenditure.amount.sum()))
                .from(expenditure)
                .where(expenditure.user.eq(user)
                        .and(expenditure.expenditureDateTime.between(request.getStart(), request.getEnd().plusDays(1L)))
                        .and(expenditure.expenditureStatus.eq(ExpenditureStatus.INCLUDED)))
                .groupBy(expenditureCategory.id)
                .fetch();
      }
  ````
</div>
</details>

- `최소` , `최대` 금액으로 조회.
  - RequestParam(required=false)로 받는 `hasMinAndMax(Boolean)`가 true라면 `최소, 최대금액 정보`를 넣어줍니다.

#### 지출삭제 API(DELETE)
- 대상 지출ID를 `PathVariable`로 받도록 설계했습니다.
- 소프트 삭제가 아닌 완전한 삭제입니다.
 
#### 지출합계 제외 API(PATCH)
- 대상 지출ID를 `PathVariable`로 받도록 설계했습니다.
- ExpenditureStatus를 `EXCLUDED`로 바꾸어, 지출합계에서 제외합니다.

### 지출 컨설팅 고려사항
---
1. 오늘 지출 추천 API
- 설정한 월별 예산을 만족하기 위해 `오늘 지출 가능한 금액`을 `총액` 과 `카테고리 별 금액`으로 제공합니다.
  - 오늘 지출 가능한 금액의 `총액`은 `카테고리 별 지출 가능 금액의 합` 이기 때문에 `지출 가능한 카테고리 별 금액`만 DB를 조회하면 되겠다고 생각했습니다. 
  - 유저의 이번달의 카테고리 별 설정예산에서 `이번달의 어제까지의 카테고리 별 지출`을 빼주는 쿼리를 Querydsl을 활용해 구현했습니다.
  - Querydsl의 `JPAExpressions(Subquery)`와 `groupBy`, `where`를 주로 사용합니다.
<details>
<summary><strong> 카테고리별 오늘 유저의 사용가능 예산 조회 쿼리 CODE - Click! </strong></summary>
<div markdown="1">       

  ````java
      @Override
      public List<UserBudgetCategoryAndAvailableExpenditure> getAvailableUserBudgetByCategoryByToday(User user) {
        LocalDateTime startOfMonth = YearMonth.now().atDay(1).atStartOfDay();
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        LocalDateTime endOfThisMonth = YearMonth.now().atEndOfMonth().atTime(23, 59, 59);

        long totalDaysThisMonth = ChronoUnit.DAYS.between(startOfMonth, endOfThisMonth) + 1;
        long daysPassed = ChronoUnit.DAYS.between(startOfMonth, yesterday) + 1;
        long remainingDays = totalDaysThisMonth - daysPassed;

        return queryFactory.select(new QUserBudgetCategoryAndAvailableExpenditure(expenditureCategory.id, expenditureCategory.name,
                        (userBudget.amount.sum().subtract(
                                JPAExpressions.select(expenditure.amount.sum())
                                        .from(expenditure)
                                        .where(expenditure.expenditureCategory.eq(expenditureCategory)
                                                .and(expenditure.user.eq(user))
                                                .and(expenditure.expenditureDateTime
                                                        .between(startOfMonth, yesterday))))
                        ).divide(remainingDays > 0 ? remainingDays : 1L)))
                .from(userBudget)
                .where(userBudget.user.eq(user)
                        .and(userBudget.plannedMonth.eq(startOfMonth)))
                .groupBy(expenditureCategory.id)
                .orderBy(expenditureCategory.id.asc())
                .fetch();
    }
  ````
</div>
</details>

- 고려사항 1. 앞선 일자에서 과다 소비하였다 해서 오늘 예산을 극히 줄이는것이 아니라, `이후 일자에 부담을 분배`한다.
  - 위의 `Subquery`에서 `이번달의 남은 일자`로 나눠주어 카테고리 별 지출 가능 금액을 조회하도록 구현했습니다.
- 고려사항 2. 기간 전체 예산을 초과 하더라도 `0원 또는 음수` 의 예산을 추천받지 않아야 한다.
  - 지속적인 소비 습관을 생성하기 위한 서비스이므로 예산을 초과하더라도 `적정한 금액을 추천`받아야 합니다.
  - 위의 결과에서는 0 또는 음수 결과를 가지는 결과를 가져옵니다. 이 결과를 `분석`해서 메세지 및 적정 최소금액을 `추천`하기위해서 `예산 컨설팅 서비스`를 `모듈화`했습니다.
  - budget_consulting 패키지의 `BudgetConsultingService`에서 `최소 적정 금액`과 `예산 분석 메세지`를 제공하도록 구현했습니다.
  - 구체적인 추천 로직을 해당 서비스에 구현함으로써 `서비스 레이어 코드 복잡도`를 낮추고 기능구현에 집중할 수 있었습니다.

<details>
<summary><strong> 오늘 지출 추천 서비스레이어 흐름 CODE - Click! </strong></summary>
<div markdown="1">  

  ````java
      public ExpenditureByTodayRecommendationResponse getExpenditureRecommendationByToday(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ApiException(CustomErrorCode.USER_NOT_FOUND_DB));

        // 이번달의 어제까지의 카테고리별 지출을 예산에 반영해야한다.
        // 유저의 오늘 지출 가능한 금액 총액, 카테고리별 금액 조회
        List<UserBudgetCategoryAndAvailableExpenditure> availableUserBudgetByCategoryByToday = userBudgetRepository.getAvailableUserBudgetByCategoryByToday(user);

        Long realAvailableExpenditure = availableUserBudgetByCategoryByToday.stream()
                .mapToLong(UserBudgetCategoryAndAvailableExpenditure::availableExpenditure).sum();
        // 예산 컨설팅 서비스에서 실제 예산 대비 지출액으로 구체적인 분석 담당
        String message = budgetConsultingService.analyzeBudgetStatus(realAvailableExpenditure);

        // 지속적인 소비 습관을 생성하기 위한 서비스이므로 예산을 초과하더라도 적정한 금액을 추천
        List<UserBudgetCategoryAndAvailableExpenditureRecommendation> res =
                availableUserBudgetByCategoryByToday.stream()
                        .map(i -> {
                            if (i.availableExpenditure() < 0) {
                                Long minimumAvailableExpenditure = budgetConsultingService.getMinimumAvailableExpenditure(i);
                                return UserBudgetCategoryAndAvailableExpenditureRecommendation.toMinimumRecommendation(i, minimumAvailableExpenditure);
                            }
                            return UserBudgetCategoryAndAvailableExpenditureRecommendation.toRecommendation(i);
                        })
                        .toList();

        Long availableTotalExpenditure = res.stream()
                .mapToLong(UserBudgetCategoryAndAvailableExpenditureRecommendation::availableExpenditure)
                .sum();

        return new ExpenditureByTodayRecommendationResponse(
                availableTotalExpenditure,
                message,
                res
        );
    }
  ````
</div>
</details>

2. 오늘 지출 안내 API
- `오늘 지출한 내용`을 `총액` 과 `카테고리 별 금액`을 알려줍니다.
- 데이터를 처리할때 최대한 DB/IO를 줄일 수 있도록 `자바로직`을 최대한 활용하고자 했습니다.
- 유저의 `카테고리별 오늘 지출 통계 결과`를 `between`, `groupBy`를 통해 조회하도록 구현했습니다.
- 카테고리별 지출의 총합은 `카테고리별 오늘 지출 통계`의 합을 `stream`을 활용하여 구했습니다.
- `월별`설정한 예산 기준 `카테고리 별` 통계 제공
    - 일자기준 오늘 `적정 금액` : 오늘 기준 사용했으면 적절했을 금액
    - 적정 금액은 유저의 `이번달 카테고리별 설정예산`을 모두 조회하고 `예산의 총합`에서 `이번달의 날짜`로 나누어줍니다.
    - 일자기준 오늘 `지출 금액` : 오늘 기준 사용한 금액
    - `위험도` : 카테고리 별 적정 금액, 지출금액의 차이를 위험도로 나타내며 %(퍼센테이지) 입니다.
        - ex) 오늘 사용하면 적당한 금액 10,000원/ 사용한 금액 20,000원 이면 200%

<details>
<summary><strong> 오늘 지출 안내 서비스레이어 흐름 CODE - Click! </strong></summary>
<div markdown="1"> 
  
  ````java
      public ExpenditureByTodayResponse getExpenditureByToday(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ApiException(CustomErrorCode.USER_NOT_FOUND_DB));
        // 유저의 카테고리별 오늘 지출 통계 결과를 가져온다.
        List<ExpenditureCategoryAndAmountResponse> expenditureCategoryAndAmountResponses = expenditureRepository.statisticExpenditureCategoryAndAmountByTodayByUser(user);

        // 유저의 오늘 지출 총합
        Long sum = expenditureCategoryAndAmountResponses.stream()
                .mapToLong(ExpenditureCategoryAndAmountResponse::sum)
                .sum();

        // 유저의 이번달 설정 예산을 가져온다.
        List<UserBudget> userBudgetsInNowMonth = userBudgetRepository.findUserBudgetsByUserAndPlannedMonth(user, YearMonth.now().atDay(1).atStartOfDay());

        // 유저의 이번달 설정 예산 총합
        long plannedBudget = userBudgetsInNowMonth
                .stream()
                .mapToLong(UserBudget::getAmount)
                .sum();

        // 유저의 이번달 하루 적절 지출 금액 (총 예산 기준)
        Long reasonableExpenditurePerDay = plannedBudget / YearMonth.now().lengthOfMonth();

        // 기존 카테고리별 통계자료에 일자기준 오늘 적정 지출 금액과 위험도를 분석해서 응답을 만든다.
        // 유저 설정 예산이 없는 경우 0, 0 분석결과를 반환한다.
        List<ExpenditureByTodayByCategoryStatisticsResponse> expenditureByTodayByCategoryStatisticsResponses = new ArrayList<>();
        expenditureCategoryAndAmountResponses.stream()
                .map(i -> {
                    Optional<UserBudget> optionalUserBudget =
                            userBudgetRepository.findUserBudgetByUserAndExpenditureCategory_IdAndPlannedMonth(user, i.categoryId(), YearMonth.now().atDay(1).atStartOfDay());
                    return optionalUserBudget.map(userBudget ->
                                    ExpenditureByTodayByCategoryStatisticsResponse.toResponse(i, userBudget.analyzeReasonableExpenditureSumAndRisk(i.sum())))
                            .orElseGet(() -> ExpenditureByTodayByCategoryStatisticsResponse.toResponse(i, new ExpenditureAnalyze(0L, 0L)));
                })
                .forEach(expenditureByTodayByCategoryStatisticsResponses::add);

        return new ExpenditureByTodayResponse(
                sum,
                reasonableExpenditurePerDay,
                expenditureByTodayByCategoryStatisticsResponses);
    }
  ````
</div>
</details>

- 적정 금액과 위험도를 분석하는 로직은 어디에?
  - 유저예산에 대한 `프로퍼티`는 `유저예산 객체`가 가지고 있기 때문에 유저예산에 메세지를 보내 `적정 금액`과 `위험도`를 `분석`하도록 했습니다.
<details>
<summary><strong> 유저예산의 적정 금액, 위험도 분석 도메인 로직 CODE - Click! </strong></summary>
<div markdown="1">  

  ````java
      public ExpenditureAnalyze analyzeReasonableExpenditureSumAndRisk(Long expenditureSum) {
        Long reasonableExpenditureSum = this.amount / YearMonth.now().lengthOfMonth();
        Long risk = (expenditureSum / reasonableExpenditureSum) * 100;
        return new ExpenditureAnalyze(reasonableExpenditureSum, risk);
    }
  ````
</div>
</details>

3. 지출 추천 및 안내 `디스코드 웹훅` 전송
- 외부 API를 사용해서 사용자의 `디스코드 웹훅 url`에 웹훅을 보내는 서비스입니다.
- `08:00` 에는 `오늘의 지출 추천` 알람, `20:00` 에는 `오늘의 지출 안내 및 분석` 알람을 보냅니다.
- `스프링 스케줄러`와 `cron식`을 사용하여 구현했습니다.
<details>
<summary><strong> 웹훅 전송 스케쥴링 CODE - Click! </strong></summary>
<div markdown="1"> 

  ````java
    @Scheduled(cron = "0 0 8 * * *")
    public void sendExpenditureRecommendationByToday() {
        List<User> usersBySubscribeNotification = userRepository.findAllBySubscribeNotificationAndDiscordUrlNot(Boolean.TRUE, "NONE");
        usersBySubscribeNotification.forEach(i -> {
            ExpenditureByTodayRecommendationResponse expenditureRecommendationByToday = getExpenditureRecommendationByToday(i.getUsername());
            applicationEventPublisher.publishEvent(new ExpenditureRecommendationEvent(expenditureRecommendationByToday, i.getDiscordUrl()));
        });
    }

    @Scheduled(cron = "0 0 20 * * *")
    public void sendExpenditureByToday() {
        List<User> usersBySubscribeNotification = userRepository.findAllBySubscribeNotificationAndDiscordUrlNot(Boolean.TRUE, "NONE");
        usersBySubscribeNotification.forEach(i -> {
            ExpenditureByTodayResponse expenditureByToday = getExpenditureByToday(i.getUsername());
            applicationEventPublisher.publishEvent(new ExpenditureAnalyzeEvent(expenditureByToday, i.getDiscordUrl()));
        });
    }
  ````
</div>
</details>

- `외부API` 사용시 `디커플링` 및 `scale-out`이 가능하도록 설계했는가?
  - 외부API 사용시 외부API 상황에 따라 서버에 미치는 영향을 최소화하는것이 중요하다고 생각합니다.
  - 이를 위해 `이벤트`구조로 웹훅서비스를 구현했습니다.
- notification 패키지로 알람 패키지를 분리했습니다.
- 스케줄러에 의해 `ApplicationEventPublisher`를 활용 웹훅 전송 이벤트가 발생하도록 구현했습니다.
- 이벤트가 발생하면 리스너에서 WebClient를 통해 디스코드 웹훅을 전송합니다.
<details>
<summary><strong> 알림 이벤트 리스너 CODE - Click! </strong></summary>
<div markdown="1">       

````java
@Component
public class NotificationEventListener {

    @Value("${discord.webhook.baseurl}")
    private String baseurl;

    @EventListener
    public void handleRecommendationNotificationEvent(ExpenditureRecommendationEvent event) {
        executeDiscordWebHook(Notification.toRecommendationWebHook(event));
    }

    @EventListener
    public void handleAnalyzeNotificationEvent(ExpenditureAnalyzeEvent event) {
        executeDiscordWebHook(Notification.toAnalyzeWebHook(event));
    }

    private void executeDiscordWebHook(Notification notification) {
        WebClient.create(baseurl)
                .post()
                .uri(notification.targetDiscordUrl())
                .bodyValue(notification)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe();
    }
}
````
</div>
</details>

### 지출 통계
---
1. 지출 통계 API
- `지난 달` 대비 `총액`, `카테고리 별` 소비율(모든 기록 기준).
    - 오늘이 10일차 라면, 지난달 10일차 까지의 데이터를 대상으로 비교합니다.
    - ex) `식비` 지난달 대비 150%
- `지난 요일` 대비 소비율(모든 기록 기준)
    - 오늘이 `월요일` 이라면 지난 `월요일` 에 소비한 모든 기록 대비 소비율
    - ex) `월요일` 평소 대비 80%
- `다른 유저` 대비 소비율(로그인 유저 기준)
    - 오늘 기준 다른 `유저` 가 예산 대비 사용한 평균 비율 대비 나의 소비율
    - 오늘기준 다른 유저가 소비한 지출이 평균 50%(ex. 예산 100만원 중 50만원 소비중) 이고 나는 60% 이면 120%.
    - ex) `다른 사용자` 대비 120%
<details>
<summary><strong> 지출통계 서비스 레이어 CODE - Click! </strong></summary>
<div markdown="1">       

````java
public ExpenditureStatisticsResponse getExpenditureStatistics(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ApiException(CustomErrorCode.USER_NOT_FOUND_DB));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfLastMonth = YearMonth.now().minusMonths(1).atDay(1).atStartOfDay();
        LocalDateTime endOfLastMonth = now.minusMonths(1);
        LocalDateTime startOfThisMonth = YearMonth.now().atDay(1).atStartOfDay();

        // 지난 달 이 시점 대비 총액, 카테고리별 소비율 (얼마나 더 썼나?)
        Long previousMonthTotalPrice = expenditureRepository.sumAmountByExpenditureDateTimeBetween(startOfLastMonth, endOfLastMonth);
        Long thisMonthTotalPrice = expenditureRepository.sumAmountByExpenditureDateTimeBetween(startOfThisMonth, now);

        BiFunction<Long, Long, String> executeRatingToStringFunction = (a, b) -> String.valueOf((b / a) * 100) + '%';

        List<ConsumptionRateByCategoryStatistics> userBudgetConsumptionRateByCategoryCompareToPreviousMonth =
                expenditureRepository.getExpenditureConsumptionRateByCategoryCompareToPreviousMonth();

        List<ConsumptionRateByCategoryResponse> res = userBudgetConsumptionRateByCategoryCompareToPreviousMonth.stream()
                .map(ConsumptionRateByCategoryResponse::toResponse)
                .collect(Collectors.toList());
        // 지난 요일 대비 소비율 (얼마나 더 썼나?)
        // 현재 요일 & 현재 요일부터 7일 전의 요일
        DayOfWeek nowDay = now.getDayOfWeek();
        DayOfWeek previousDay = nowDay.minus(7);

        // 현재 요일의 시작과 끝
        LocalDateTime startOfToday = now.with(DayOfWeek.from(nowDay)).toLocalDate().atStartOfDay();
        LocalDateTime endOfToday = now.with(DayOfWeek.from(nowDay)).toLocalDate().atTime(23, 59, 59, 59);

        // 7일 전의 요일의 시작과 끝
        LocalDateTime startOfPreviousDay = now.with(DayOfWeek.from(previousDay)).toLocalDate().atStartOfDay();
        LocalDateTime endOfPreviousDay = now.with(DayOfWeek.from(previousDay)).toLocalDate().atTime(23, 59, 59, 59);

        Long previousDayTotalPrice = expenditureRepository.sumAmountByExpenditureDateTimeBetween(startOfPreviousDay, endOfPreviousDay);
        Long thisDayTotalPrice = expenditureRepository.sumAmountByExpenditureDateTimeBetween(startOfToday, endOfToday);

        // 다른 유저 대비 소비율 (오늘 기준 다른 유저가 예산 대비 사용한 평균 비율 대비 나의 비율)
        Long consumptionRateCompareByOtherUsers = userBudgetRepository.getUserBudgetConsumptionRateByUsers(user);
        Long consumptionRateByUser = userBudgetRepository.getUserBudgetConsumptionRateByUser(user);
        return new ExpenditureStatisticsResponse(
                executeRatingToStringFunction.apply(previousMonthTotalPrice, thisMonthTotalPrice),
                executeRatingToStringFunction.apply(previousDayTotalPrice, thisDayTotalPrice),
                executeRatingToStringFunction.apply(consumptionRateCompareByOtherUsers, consumptionRateByUser),
                res
        );
    }
````
</div>
</details>

## 핵심문제 해결과정 및 전략
### 서비스 빈들간 의존성 & 결합도 이슈
---
1. 요구사항을 구현하고 보니 서비스 Bean에서 다른 서비스 Bean을 의존하고 있는 `의존성 & 결합도` 문제를 인식했습니다.
- 문제1: 무분별하게 DI해서 사용한 서비스 빈들이 거미줄처럼 꼬여 `스파게티`가 될 가능성
- 문제2: 서비스 빈들간 `순환참조` 가능성 내포 
2. `고차함수`와 `Handler 컴포넌트`를 활용한 리팩토링
- 후보1: Handler 컴포넌트에서 다른 서비스의 `결과`를 받아 타겟 서비스의 메서드 파라미터로 넣어주는 방법
- 후보2: Handler 컴포넌트에서 다른 서비스의 `함수`를 타겟서비스의 메서드에 `파라미터`로 넘겨주는 방법
3. `함수`를 넘겨주는 것이 불변성을 보장하고 테스트를 용이하게 합니다.
- 함수를 파라미터로 넘겨주는 방법을 선택함으로써 `불변성`을 보장 & `예측가능`하도록 개선했습니다.
4. Before & After
<details>
<summary><strong> Before ExpenditureService CODE - Click! </strong></summary>
<div markdown="1">       

````java
@Service
@RequiredArgsConstructor
public class ExpenditureService {

    private final ExpenditureRepository expenditureRepository;
    private final UserRepository userRepository;
    private final ExpenditureCategoryRepository expenditureCategoryRepository;
    private final UserBudgetRepository userBudgetRepository;
    private final BudgetConsultingService budgetConsultingService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final RedissonLockContext redissonLockContext;
    private final TransactionService transactionService;

		public ExpenditureByTodayRecommendationResponse getExpenditureRecommendationByToday(String username) {
		        ..............
		        List<UserBudgetCategoryAndAvailableExpenditure> availableUserBudgetByCategoryByToday = userBudgetRepository.getAvailableUserBudgetByCategoryByToday(user);
						
		        Long realAvailableExpenditure = availableUserBudgetByCategoryByToday.stream()
		                .mapToLong(UserBudgetCategoryAndAvailableExpenditure::availableExpenditure).sum();
		        
			// 예산 컨설팅 서비스에서 실제 예산 대비 지출액으로 구체적인 분석 담당
			// UserBudgetConsultingService를 의존하고 있는 포인트1!
		        String message = budgetConsultingService.analyzeBudgetStatus(realAvailableExpenditure);
		
		        // 지속적인 소비 습관을 생성하기 위한 서비스이므로 예산을 초과하더라도 적정한 금액을 추천
		        List<UserBudgetCategoryAndAvailableExpenditureRecommendation> res =
		                availableUserBudgetByCategoryByToday.stream()
		                        .map(i -> {
		                            if (i.availableExpenditure() < 0) {
						// UserBudgetConsultingService를 의존하고 있는 포인트2!
		                                Long minimumAvailableExpenditure = budgetConsultingService.getMinimumAvailableExpenditure(i);
		                                return UserBudgetCategoryAndAvailableExpenditureRecommendation.toMinimumRecommendation(i, minimumAvailableExpenditure);
		                            }
		                            return UserBudgetCategoryAndAvailableExpenditureRecommendation.toRecommendation(i);
		                        })
		                        .toList();
					...........
		    }
.......
````
</div>
</details>

<details>
<summary><strong> After ExpenditureServiceHandler & ExpenditureService CODE - Click! </strong></summary>
<div markdown="1">       

````java
@Component
public class ExpenditureServiceHandler {

    private final BudgetConsultingService budgetConsultingService;
    private final ExpenditureService expenditureService;
    private final RedissonLockContext redissonLockContext;

    public ExpenditureServiceHandler(final BudgetConsultingService budgetConsultingService,
                                     final ExpenditureService expenditureService,
                                     final RedissonLockContext redissonLockContext) {
        this.budgetConsultingService = budgetConsultingService;
        this.expenditureService = expenditureService;
        this.redissonLockContext = redissonLockContext;
    }

    public ExpenditureByTodayRecommendationResponse getExpenditureRecommendationByToday(String username) {
        return expenditureService.getExpenditureRecommendationByToday(
                username,
		// 함수를 파라미터로 넘긴다.
                budgetConsultingService::analyzeBudgetStatus,
		// 함수를 파라미터로 넘긴다.
                budgetConsultingService::getMinimumAvailableExpenditure
        );
    }
}

public ExpenditureByTodayRecommendationResponse getExpenditureRecommendationByToday(String username,
                                                                                    Function<Long, String> analyzeBudgetStatus,
                                                                                    Function<String, Long> getMinimumAvailableExpenditure) {
        .......
        List<UserBudgetCategoryAndAvailableExpenditure> availableUserBudgetByCategoryByToday = userBudgetRepository.getAvailableUserBudgetByCategoryByToday(user);
				
        Long realAvailableExpenditure = availableUserBudgetByCategoryByToday.stream()
                .mapToLong(UserBudgetCategoryAndAvailableExpenditure::availableExpenditure).sum();
        // 예산 컨설팅 서비스에서 실제 예산 대비 지출액으로 구체적인 분석 담당
	// UserBudgetConsultingService를 의존하고 있는 포인트1! - 함수 apply
        String message = analyzeBudgetStatus.apply(realAvailableExpenditure);

        // 지속적인 소비 습관을 생성하기 위한 서비스이므로 예산을 초과하더라도 적정한 금액을 추천
        List<UserBudgetCategoryAndAvailableExpenditureRecommendation> res =
                availableUserBudgetByCategoryByToday.stream()
                        .map(i -> {
                            if (i.availableExpenditure() < 0) {
				// UserBudgetConsultingService를 의존하고 있는 포인트2! - 함수 apply
                                Long minimumAvailableExpenditure = getMinimumAvailableExpenditure.apply(i.name());
                                return UserBudgetCategoryAndAvailableExpenditureRecommendation.toMinimumRecommendation(i, minimumAvailableExpenditure);
                            }
                            return UserBudgetCategoryAndAvailableExpenditureRecommendation.toRecommendation(i);
                        })
                        .toList();
		...................
    }
````
</div>
</details>

5. 결과
- 위 방법을 ExpenditureService, UserBudgetService에 적용시켜 `리팩토링`을 진행했습니다.
![refactor-function](https://github.com/soonhankwon/gold-digger-api/assets/113872320/40bd9f9f-7052-4124-a542-e00b5bd5a6d5)
- 이전에 비해 불필요한 `서비스 Bean간 의존성을 개선`시켰습니다.
- 리팩토링 진행을 하면서 분산락 적용과 기존 TransactionService를 전보다 깔끔하게 정리가 부수적으로 가능해졌습니다.
<details>
<summary><strong> Before 분산락 적용 코드와 After CODE - Click! </strong></summary>
<div markdown="1">       

````java
// Before
public String createExpenditure(String username, Long categoryId, ExpenditureRequest request) {
        redissonLockContext.executeLock(username, () ->
                // 락을 점유한 스레드만 트랜잭션 적용
                transactionService.executeAsTransactional(() -> {
                    User user = userRepository.findUserByUsername(username)
                            .orElseThrow(() -> new ApiException(CustomErrorCode.USER_NOT_FOUND_DB));

                    ExpenditureCategory category = expenditureCategoryRepository.findById(categoryId)
                            .orElseThrow(() -> new ApiException(CustomErrorCode.CATEGORY_NOT_FOUND_DB));

                    Expenditure expenditure = new Expenditure(user, category, request);
                    expenditureRepository.save(expenditure);
                    return null;
                }));
        return "created";
    }

// After
public class ExpenditureServiceHandler {

    private final BudgetConsultingService budgetConsultingService;
    private final ExpenditureService expenditureService;
    private final RedissonLockContext redissonLockContext;

    public ExpenditureServiceHandler(final BudgetConsultingService budgetConsultingService,
                                     final ExpenditureService expenditureService,
                                     final RedissonLockContext redissonLockContext) {
        this.budgetConsultingService = budgetConsultingService;
        this.expenditureService = expenditureService;
        this.redissonLockContext = redissonLockContext;
    }

    public String createExpenditure(String username, Long categoryId, ExpenditureRequest request) {
        // 락을 Handler에서 책임지도록 수정 - 락을 점유한 스레드만 지출서비스의 지출생성을 실행한다.
        redissonLockContext.executeLock(username,
                () -> expenditureService.createExpenditure(username, categoryId, request));
        return "created";
    }
...........

// ExpenditureService의 지출생성 메서드
    // 외부 클래스로 분리되었음으로 @Transactional 적용이 가능해져 Transaction 적용서비스 및 코드 삭제
    @Transactional
    public void createExpenditure(String username, Long categoryId, ExpenditureRequest request) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ApiException(CustomErrorCode.USER_NOT_FOUND_DB));

        ExpenditureCategory category = expenditureCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ApiException(CustomErrorCode.CATEGORY_NOT_FOUND_DB));

        Expenditure expenditure = new Expenditure(user, category, request);
        expenditureRepository.save(expenditure);
    }
````
</div>
</details>
      
### 동시성 제어 이슈
---
1. 지출, 유저예산 `업데이트`시 동시성 제어를 위해 `낙관적락` 적용
- 지출, 유저예산 업데이트시 동일 데이터에 같은 사용자가 동시에 접근하는 동시성 이슈가 발생했습니다.
- 비관적락은 성능에 이슈가 있고, 해당 업데이트시 동일 유저가 실수로 접근하는 경우라고 판단하여 낙관적락을 적용했습니다.
- JPA `@Version`을 적용하여 애플리케이션 단계에서 동시성 이슈를 개선했습니다.   
2. 지출, 유저예산, 추천 유저예산 `생성`시 동시성 제어를 위해 `분산락` 적용
- 업데이트의 경우와 달리 생성시에는 낙관적락으로 동시성 제어가 불가능했습니다.
- 비관적락은 성능에 이슈가 있어 `Redisson`을 활용한 분산락을 적용했습니다.
- @Transactional은 동일 클래스 내부 메서드 호출에는 적용되지 않기 때문에, 별도의 `TransactionSevice`를 구현하여 내부 메서드에 `트랜잭션`을 적용했습니다.
- 락을 username로 설정하여 동시에 요청이 들어 왔을 경우에도 한 개의 스레드만 락을 점유하여 요청을 수행하도록 구현했습니다.
- 분산락 적용 코드가 `템플릿화`되있는 점을 인식(try-catch 블럭) `템플릿 콜백(전략)패턴`을 활용해서 코드를 개선시켰습니다. 
<details>
<summary><strong> 분산락 및 내부 메서드 트랜잭션 적용 CODE - Click! </strong></summary>
<div markdown="1">       

````java
public String createExpenditure(String username, Long categoryId, ExpenditureRequest request) {
        redissonLockContext.executeLock(username, () ->
                // 락을 점유한 스레드만 트랜잭션 적용
                transactionService.executeAsTransactional(() -> {
                    User user = userRepository.findUserByUsername(username)
                            .orElseThrow(() -> new ApiException(CustomErrorCode.USER_NOT_FOUND_DB));

                    ExpenditureCategory category = expenditureCategoryRepository.findById(categoryId)
                            .orElseThrow(() -> new ApiException(CustomErrorCode.CATEGORY_NOT_FOUND_DB));

                    Expenditure expenditure = new Expenditure(user, category, request);
                    expenditureRepository.save(expenditure);
                    return null;
                }));
        return "created";
    }

public String createUserBudget(String username, Long categoryId, UserBudgetCreateRequest request) {
        redissonLockContext.executeLock(username, () ->
                // 락을 점유한 스레드만 트랜잭션 적용
                transactionService.executeAsTransactional(() -> {
                    User user = userRepository.findUserByUsername(username)
                            .orElseThrow(() -> new ApiException(CustomErrorCode.USER_NOT_FOUND_DB));

                    ExpenditureCategory category = expenditureCategoryRepository.findById(categoryId)
                            .orElseThrow(() -> new ApiException(CustomErrorCode.CATEGORY_NOT_FOUND_DB));

                    UserBudget userBudget = new UserBudget(user, category, request);
                    validateDuplicatedUserBudget(user, userBudget, category);
                    userBudgetRepository.save(userBudget);
                    return null;
                }));
        return "created";
    }
````
</div>
</details>

<details>
<summary><strong> 분산락 전략패턴 CODE - Click! </strong></summary>
<div markdown="1">       

````java
@Component
@RequiredArgsConstructor
public class RedissonLockContext {

    private final RedissonClient redissonClient;

    public void executeLock(String username, RedissonLockStrategy strategy) {
        RLock lock = redissonClient.getLock(username);
        try {
            // waitTime: 락 대기시간, leaseTime: 해당 시간이 지나면 락 해제
            boolean available = lock.tryLock(0, 1, TimeUnit.SECONDS);
            if(!available) {
                throw new ApiException(CustomErrorCode.CANT_GET_LOCK);
            }
            // 전략은 서비스레이어에서 구체적 구현
            strategy.call();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
````
</div>
</details>

### 캐싱전략
---
1. 카테고리 목록조회 API 캐싱
- 카테고리 목록은 `서비스측에서 설정한 지출 카테고리`를 제공합니다. 따라서 DB에는 사측의 결정에 의해서만 카테고리가 업데이트됩니다.
- 따라서 지출 카테고리 목록 조회 API는 초기에 조회결과를 Redis에 `캐싱`을 해두고 이후에는 캐싱된 결과를 조회하도록 하여 `불필요한 DB I/O를 개선`했습니다. 
2. 유저예산 추천 API의 카테고리별 유저예산 비율 `통계 쿼리 캐싱`
- 유저예산 추천시 전체 유저의 카테고리별 유저예산 비율을 통계하여 사용하기 때문에 많은 비용이 발생하는 문제를 겪었습니다.
- 카테고리별 유저예산 비율이 매우 정확한 수치를 요구하는 서비스가 아니기 때문에 해당 통계쿼리를 `새벽 2시 스케쥴러`를 통해 `캐싱`하도록 했습니다.
- 캐싱결과 기존 8.7sec 에서 57ms로 Latency 개선율 `99.34%` 및 불필요한 `DB/IO를 개선`시킬 수 있었습니다.

<details>
<summary><strong> 카테고리별 유저예산 비율 통계 쿼리 캐싱 CODE - Click! </strong></summary>
<div markdown="1">       

````java
@Scheduled(cron = "0 0 2 * * *")
    @CachePut(value = "user-budget:avg-ratio:1:collections", cacheManager = "cacheManager")
    public void cacheUserBudgetAvgRatioByCategoryStatistic() {
        userBudgetRepository.statisticUserBudgetAvgRatioByCategory();
    }
````
</div>
</details>
<br/>
