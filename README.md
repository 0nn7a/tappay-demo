# TapPay Sandbox Demo

TapPay Direct Pay（信用卡）的 Sandbox 範例，包含付款、查詢交易、退款。

- 前端：Vue 3 + Vite + Tailwind CSS（`fe/`）
- 後端：Spring Boot 4（`be/`，Java 21）

## 架構

```
瀏覽器（TapPay Fields 取得 prime）
   │
   ▼
Spring Boot 後端（持有 Partner Key）
   │
   ▼
TapPay Server APIs（Pay by Prime / Record / Refund）
```

前端只負責取得 prime，所有需要 Partner Key 的呼叫都由後端代為發送。

## 環境設定

### 後端環境變數

| 變數 | 說明 |
|---|---|
| `TAPPAY_MERCHANT_ID` | 來自 Portal 測試用商家（需非分期 + 非 3D 驗證） |
| `TAPPAY_PARTNER_KEY` | 來自 Portal 公司資訊的帳號金鑰（Partner Key） |

### 前端環境變數

在 `fe/` 目錄建立 `.env.local`（已被 `.gitignore` 排除，不會進版控）：

```
VITE_TAPPAY_APP_ID=你的 App ID
VITE_TAPPAY_APP_KEY=你的 App Key
VITE_API_BASE_URL=http://localhost:8080
```

App ID 與 App Key 同樣來自 Portal 的開發人員內容。

## 啟動方式

後端（port 8080）：

```bash
cd be
./mvnw spring-boot:run
```

前端（port 5173）：

```bash
cd fe
npm install
npm run dev
```

開啟 http://localhost:5173 即可使用。

> 後端的 CORS 目前只允許 `http://localhost:5173`。

## 功能與 API

頁面分為左右兩個區塊：

1. **送出交易**：輸入測試卡片資料後付款（固定 NT$ 100，金額由後端決定）
2. **交易查詢與退款**：輸入交易編號（`rec_trade_id`），可查詢交易紀錄或全額退款

### 後端 API

| Method | 路徑 | 請求內容 | 對應的 TapPay API |
|---|---|---|---|
| POST | `/api/payments` | `{ "prime": "..." }` | Pay by Prime |
| POST | `/api/records/query` | `{ "recordId": "..." }` | Record |
| POST | `/api/records/refund` | `{ "recordId": "..." }` | Refund |

後端不轉換格式，直接回傳 TapPay 的原始 JSON，由前端依 `status`（0 代表成功）判斷結果。

`recordId` 就是付款成功後回傳的 `rec_trade_id`。

### Timeout

後端對 TapPay 的請求設有逾時（`application.yaml`）：

```yaml
spring:
  http:
    clients:
      connect-timeout: 5s
      read-timeout: 30s
```

讀取逾時 30 秒是依官方建議，因為尖峰時段銀行處理可能較久。

## 注意事項

- 退款不代表立即成功：TapPay 當日向銀行發出退款，實際結果請於隔天用 Record API 或 Portal 確認
- 部分銀行在請款期間不允許退款，剛付款完立即退款可能失敗，請查看回傳的 `msg`
- 本專案僅供 Sandbox 測試，不可直接用於正式環境

## 相關參考

[TapPay 開發者文件](https://docs.tappaysdk.com/tutorial/zh/home.html)
[測試用卡號及其他參數](https://docs.tappaysdk.com/tutorial/zh/reference.html#what-is-it243)
[後端 API Error Code](https://docs.tappaysdk.com/tutorial/zh/error.html#api-error-code)
[後端 API 文件（Pay by Prime / Refund / Record）](https://docs.tappaysdk.com/tutorial/zh/back.html)
[TapPay Portal](https://accounts.tappaysdk.com/login)
[TapPay/tappay-web-example](https://github.com/TapPay/tappay-web-example)