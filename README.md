# 29CM 백엔드 포지션 과제

## 사용 기술
- Java 17
- Spring Boot 3.2.4
- Gradle
- H2 Database
- Hibernate
- Junit5

## 실행 및 테스트
### 실행
* Run `HomeworkApplication`
  * schema.sql와 data.sql 로드 후 H2 DB에 데이터 자동 초기화. 이후 커맨드라인 실행
  * [SQL Scripts를 사용한 H2 Database 데이터 초기화 과정](https://bokyoung89.github.io/spring/0410/)

### 통합테스트
1. `OrderServiceTest` : order 메서드의 통합적인 기능 테스트. order 메서드는 하나의 트랜잭션 안에서 상품 조회, 재고 감소, 주문 생성 등 
다양한 기능을 수행하므로 통합테스트를 진행함. DB와 연결된 상태에서 데이터 무결성을 체크함.

![image](https://github.com/bokyoung89/Pharmacy-Recommendation-Version-Management/assets/58727604/e17d9708-b7ec-4512-8717-fbe2c0dd6c97)

2. `ItemServiceTest` : 상품 조회 기능 테스트

![image](https://github.com/bokyoung89/Pharmacy-Recommendation-Version-Management/assets/58727604/1a6aa8f8-2885-472e-8b53-00184da000a8)

## Architecture
* 레이어드 아키텍쳐 구조 - Application, Domain, Infrastructure, Presentation
* 도메인 주도 설계를 따름

### Appication
* ItemService : 상품 단건 조회, 상품 전체 조회 담당
* OrderService : 주문 생성, 주문 조회 담당

### Domain
* entity
  * Item : 재고 감소, 재고 증가 로직
  * Order : 주문상품 생성, 재고 감소, 재고 증가 로직
  * OrderItem : 주문상품 전체 금액 계산 로직
  * OrderStatus : 주문상태 enum
* repository
  * ItemRepository : 상품 단건 조회, 전체 조회 쿼리
  * OrderRepository : 상품 조회 쿼리
* exception 
  * ItemNotFoundException : 상품이 존재하지 않을 때 예외 처리
  * SoldOutException : 재고가 부족할 때 예외 처리

### Infrastructure
* config
  * AppConfig : BufferedReader 객체를 빈으로 등록하는 클래스
* persistence : 개발/운영 repository를 분리해 사용하기 위한 클래스. 리팩토링 시 사용 예정

### Presentation
* controller, dto, view : 클라이언트로부터 들어온 요청을 받고 응답을 반환하는 역할

## ERD 설계
![29CM_상품주문_ERD drawio (2)](https://github.com/bokyoung89/Pharmacy-Recommendation-Version-Management/assets/58727604/334e5b08-42f6-4690-b9c1-8f43b23729d5)
* 주문상품(order_item)
  * 상품과 주문은 다대다 관계이므로 다대일 관계로 풀어주기 위한 매핑 테이블
  * 주문 시 상품번호(item_id), 주문수량(order_id)을 입력받아 주문가격(price)을 계산함
* 상품(item)
  * 하나의 상품 번호로 여러 개의 주문이 생성될 수 있으므로 주문상품과 다대일 단방향 관계
* 주문(Orders)
  * 한번에 여러 개의 상품을 같이 주문할 수 있어야 하므로 주문과 주문상품(OrderProduct)은 일대다 관계
  * 주문상태는 주문(ORDER), 취소(CANCEL)로 표현함. 주문 시 생성일시가 insert되고, 취소 시 삭제일시가 update됨
  * 전체주문가격(total_price)을 필드로 두고 역정규화함
  * 배송비(delivery_fee) 컬럼을 따로 두어 정보를 저장하고 추후 활용할 수 있게 함

## 멀티 스레드에서 동시성 문제 해결 - 비관적 락 적용
### 적용
* `ItemRepository`

![image](https://github.com/bokyoung89/Pharmacy-Recommendation-Version-Management/assets/58727604/7f041efe-fadf-49af-bb43-020bacbd7237)

* LockModeType.PESSIMISTIC_WRITE는 비관적 잠금 모드를 의미합니다. 이를 사용해 해당 트랜잭션에서 데이터를 업데이트하기 위해 선택한 레코드를 다른 트랜잭션이 읽지 못하도록 방지하였습니다.

### 선택 이유
* 현재 상품주문 서비스는 상품 조회, 재고 확인 및 감소, 주문 생성까지 하나의 트랜잭션 안에서 이루어지도록 설계돼있어 데이터 무결성이 중요합니다.
또한 동시에 A라는 상품을 주문하는 멀티스레드 테스트를 통과해야 하므로 충돌이 빈번하게 일어나는 상황에 대비해야 합니다.
* 비관적 락은 자원에 미리 락을 걸어 다른 스레드의 접근을 막기 때문에 데이터 무결성을 보장할 수 있고, 충돌로 인해 발생할 수 있는 문제를 방지할 수 있으므로 선택하게 되었습니다.

### 개선점
* 재고는 재고 수량 변화가 빈번하므로 상품 조회시 트랜잭션으로 인해 성능저하가 발생할수 있습니다. 
따라서 트래픽이 증가하는 상황을 가정하면, 재고 서비스는 DB를 따로 구성하여 redis 분산락을 사용하면 성능 효율에 도움이 될 것입니다.