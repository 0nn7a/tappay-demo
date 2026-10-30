<script setup>
import { ref } from 'vue'

const recordId = ref('')
const loading = ref(false)
const result = ref(null)

async function call(action) {
  if (!recordId.value.trim() || loading.value) return

  loading.value = true
  result.value = null
  try {
    const res = await fetch(
      `${import.meta.env.VITE_API_BASE_URL}/api/records/${action}`,
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ recordId: recordId.value.trim() }),
      },
    )
    result.value = await res.json()
  } catch (error) {
    result.value = { error: error.message }
  } finally {
    loading.value = false
  }
}

function refund() {
  if (confirm('確定要退款嗎？')) call('refund')
}
</script>

<template>
  <section class="my-6 flex flex-col items-center gap-2">
    <h2 class="tp-title">交易查詢與退款</h2>

    <form @submit.prevent class="flex flex-col">
        <label class="tp-lbl">交易編號</label>
        <input
        v-model="recordId"
        placeholder="請輸入交易編號"
        class="tp-field"
        />

        <button
            type="button"
            :disabled="loading || !recordId.trim()"
            class="tp-btn"
            :class=" loading || !recordId.trim() ? 'cursor-not-allowed': 'cursor-pointer'"
            @click="call('query')"
        >
            查詢
        </button>
        <button
            type="button"
            :disabled="loading || !recordId.trim()"
            class="tp-btn"
            :class=" loading || !recordId.trim() ? 'cursor-not-allowed': 'cursor-pointer'"
            @click="refund"
        >
            退款
        </button>
    </form>

    <div v-if="result" class="mt-4 tp-scrollbar">
        <pre >{{ JSON.stringify(result, null, 2) }}</pre>
    </div>
  </section>
</template>