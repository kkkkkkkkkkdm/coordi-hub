# 상품 코디 정보 시스템

브랜드와 카테고리별 상품 코디 정보를 제공하는 시스템입니다.

사용자(고객)와 관리자 기능을 분리하여 API 및 웹페이지를 제공합니다.

성능 최적화를 위해 조회 기능에 로컬 캐싱이 적용되어 있습니다.

## 기술 스택
- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- Spring Cache
- H2 Database
- Gradle

## 시스템 구조

### 도메인 모델
- Brand: 브랜드 정보 (활성화/비활성화 관리 가능)
- Category: 카테고리 정보 (추가 확장 가능성 고려)
- Product: 상품 정보

### 주요 기능

#### 사용자 기능 (`/api/v1/coordi`) - 캐싱적용

1. 카테고리별 최저가격 상품 조회

2. 단일 브랜드로 구성된 최저가격 코디 조회 
 
3. 특정 카테고리의 최고가/최저가 상품 조회 

#### 관리자 기능 (`/api/v1/admin`)
1. 브랜드 관리
   - 목록 조회 (페이징)
   - 등록/수정/삭제 (캐시 무효화)
   - 활성화/비활성화 관리기능 제공

2. 상품 관리
   - 목록 조회 (페이징)
   - 등록/수정/삭제 (캐시 무효화)

## 빌드 및 실행 방법

### 요구사항
- JDK 17 이상
- Gradle 7.x 이상

### 빌드 및 실행
```bash
# 프로젝트 클론
git clone 

# 프로젝트 디렉토리로 이동 
cd musinsa

# 실행 (IDE or Terminal)
IDE Run
./gradlew bootRun
```

서버는 기본적으로 `http://localhost:8080`에서 실행됩니다.

## 데이터베이스 설정

H2 인메모리 데이터베이스를 사용합니다.
- Console: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: sa
- Password: (empty)

## API 상세

### 관리자 API
1. 브랜드 관리
   - 브랜드 목록 조회
     ```
     GET /api/v1/admin/brands?page={page}&size={size}
     ```
   - 브랜드 등록
     ```
     POST /api/v1/admin/brands
     {
       "name": "브랜드명",
       "enabled": false  // 기본값 false
     }
     ```
   - 브랜드 수정
     ```
     PUT /api/v1/admin/brands/{id}
     {
       "name": "수정된 브랜드명",
       "enabled": true
     }
     ```
   - 브랜드 삭제
     ```
     DELETE /api/v1/admin/brands/{id}
     ```

2. 상품 관리
   - 상품 목록 조회
     ```
     GET /api/v1/admin/products?page={page}&size={size}
     ```
   - 상품 등록
     ```
     POST /api/v1/admin/products
     {
       "brandName": "브랜드명",
       "categoryName": "카테고리명",
       "name": "상품명",
       "price": 10000
     }
     ```
   - 상품 수정
     ```
     PUT /api/v1/admin/products/{id}
     {
       "brandName": "수정된 브랜드명",
       "categoryName": "수정된 카테고리명",
       "name": "수정된 상품명",
       "price": 20000
     }
     ```
   - 상품 삭제
     ```
     DELETE /api/v1/admin/products/{id}
     ```

## API Response 형식

모든 API 응답은 다음 형식을 따릅니다:
```
{
  "success": true/false,
  "data": { ... },
  "error": {
    "code": "ERROR_CODE",
    "message": "에러 메시지"
  }
}
```
