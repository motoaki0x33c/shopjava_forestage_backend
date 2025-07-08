# ShopJava 電商前台-後端

這是一個基於 Spring Boot 開發的電商前台-後端的練習用系統，目標是練習完整的電商功能，包括商品管理、購物車、訂單處理、金流和物流整合等功能。

## 系統需求

- Java 17
- MySQL 8.0

## 功能清單

### 購物車功能
- [x] 購物車的創建和管理
- [x] 商品加入購物車
- [x] 購物車商品數量修改
- [x] 購物車商品刪除

### 訂單系統
- [ ] 訂單創建和管理
- [ ] 訂單狀態追蹤
- [ ] 訂單歷史記錄

### 金流整合
- [ ] 綠界金流整合
- [ ] 支援多種金流方式
- [ ] 金流狀態管理
- [ ] 手續費設定

### 物流整合
- [x] 綠界物流整合
  - [x] 超商電子地圖功能
  - [x] 超商資訊回傳處理
- [ ] 其他物流商整合
- [ ] 運費計算
- [ ] 物流狀態追蹤

## 資料庫結構

系統使用以下主要資料表：

1. `products` - 商品資料表
   - 包含商品基本資訊、價格、庫存等

2. `carts` - 購物車資料表
   - 管理購物車基本資訊

3. `cart_products` - 購物車商品資料表
   - 記錄購物車中的商品資訊

4. `payments` - 金流資料表
   - 管理金流方式和設定

5. `logistics` - 物流資料表
   - 管理物流方式和設定

## API 文件

本專案使用 Swagger 3 (OpenAPI) 來生成 API 文件。啟動專案後，可以通過以下網址訪問 API 文件：

```
http://localhost:8080/swagger-ui/index.html
```

## 安裝和運行

1. 克隆專案
```bash
git clone [專案網址]
```

2. 設定資料庫
- 創建 MySQL 資料庫
- 修改 `application.properties` 中的資料庫連線設定

3. 編譯專案
```bash
mvn clean install
```

4. 運行專案
```bash
mvn spring-boot:run
```

5. 訪問 API 文件
- 開啟瀏覽器，訪問 `http://localhost:8080/swagger-ui/index.html`

## 技術架構
- Spring Boot
- Spring Security
- JPA/Hibernate
- MySQL
- Swagger/OpenAPI
-  
## 開發團隊

- 開發者：motoaki
