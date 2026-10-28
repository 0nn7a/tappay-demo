<script setup>
import { onMounted, onBeforeUnmount, ref } from 'vue'

const loading = ref(false)
const message = ref('請填寫測試卡片資料')
const cardReady = ref(false)
const result = ref(null)

let TPDirect = null;
let updateHandler = null

onMounted(() => {
  TPDirect = window.TPDirect;
  
  if (!TPDirect) {
    message.value = 'TapPay SDK 載入失敗，請檢查網路'
    return
  }

  const appId = Number(import.meta.env.VITE_TAPPAY_APP_ID)
  const appKey = import.meta.env.VITE_TAPPAY_APP_KEY

  if (!appId || !appKey) {
    message.value = '請先設定 .env.local 的 App ID 與 App Key'
    return
  }

  TPDirect.setupSDK(appId, appKey, 'sandbox')

  TPDirect.card.setup({
    fields: {
      number: {
        element: '#card-number',
        placeholder: '**** **** **** ****',
      },
      expirationDate: {
        element: '#card-expiration-date',
        placeholder: 'MM / YY',
      },
      ccv: {
        element: '#card-ccv',
        placeholder: 'CCV',
      },
    },
    styles: {
      input: {
        color: 'black',
        'font-size': '16px',
      },
      '.valid': { color: 'green' },
      '.invalid': { color: 'red' },
    },
    isMaskCreditCardNumber: true,
    maskCreditCardNumberRange: {
        beginIndex: 6,
        endIndex: 11
    }
  })

  updateHandler = (update) => {
    cardReady.value = Boolean(update.canGetPrime)

    if (cardReady.value) {
      message.value = JSON.stringify(update, null, 2)
    } else {
      message.value = '請填寫測試卡片資料'
    }
  }

  TPDirect.card.onUpdate(updateHandler)
})

onBeforeUnmount(() => {
  // 本頁卸載時不再保留 Vue 端的狀態或事件處理器
  updateHandler = null
})

async function pay() {
  if (!TPDirect || loading.value || !cardReady.value) return

  loading.value = true
  result.value = null
  message.value = '正在取得付款 Token...'

  try {
    TPDirect.card.getPrime(async (primeResult) => {
      try {
        if (primeResult.status !== 0 || !primeResult.card?.prime) {
          message.value =
            primeResult.msg || '取得 Prime 失敗，請檢查卡片欄位'
          return
        }

        message.value = '正在送出 Sandbox 付款請求...'

        const response = await fetch(
          `${import.meta.env.VITE_API_BASE_URL}/api/payments`,
          {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
              prime: primeResult.card.prime,
              amount: 100 
            }),
          },
        )

        const data = await response.json()
        result.value = data
        message.value = response.ok
          ? '已收到 TapPay 回應，請確認交易結果'
          : '付款請求失敗，請查看錯誤訊息'
      } catch (error) {
        message.value = `請求失敗：${error.message}`
      } finally {
        loading.value = false
      }
    })
  } catch (error) {
    loading.value = false
    message.value = `取得 Prime 失敗：${error.message}`
  }
}
</script>

<template>
  <main class="mx-auto p-10 flex flex-col items-center gap-2">
    <h1 class="mb-2 font-semibold text-2xl">TapPay Sandbox 測試</h1>
    <p>測試商品：範例商品</p>
    <p>測試金額：NT$ 100</p>

    <form @submit.prevent="pay" class="my-6 flex flex-col">
      <label>信用卡卡號</label>
      <div id="card-number" class="tp-field"></div>

      <label>有效期限</label>
      <div id="card-expiration-date" class="tp-field"></div>

      <label>安全碼 CCV</label>
      <div id="card-ccv" class="tp-field"></div>

      <button 
        type="submit" 
        :disabled="loading || !cardReady" 
        class="mt-6 p-2 bg-gray-200 rounded "
        :class=" loading || !cardReady ? 'cursor-not-allowed': 'cursor-pointer'"
        >
        {{ loading ? '處理中...' : '測試付款 NT$ 100' }}
      </button>
    </form>

    <pre>{{ message }}</pre>

    <pre v-if="result">{{ JSON.stringify(result, null, 2) }}</pre>
  </main>
</template>

<style scoped>
@reference './style.css';

.tp-field {
  @apply h-10 mt-1 mb-3 p-2 border border-gray-400 rounded;
}
</style>