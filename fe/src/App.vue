<script setup>
import PostPanel from './components/PostPanel.vue'
import RecordPanel from './components/RecordPanel.vue'
</script>

<template>
  <main class="h-full w-full p-10 flex flex-col items-center gap-3 overflow-hidden">
    <h1 class="font-semibold text-2xl">TapPay Sandbox 測試</h1>

    <section class="grow flex justify-center gap-10">
      <PostPanel />
      <RecordPanel />
    </section>
  </main>
</template>

<style>
@reference './style.css';

.tp-title {
  @apply font-semibold text-xl;
}

.tp-lbl {
  @apply mt-3 mb-1;
}

.tp-field {
  @apply h-10 p-2 w-60 placeholder-[darkgray] border border-gray-400 outline-0 rounded ;
}

.tp-btn {
  @apply mt-2 p-2 bg-gray-200 rounded;

  &:first-of-type {
    @apply mt-4;
  }
}

.tp-scrollbar {
  @apply grow h-0 overflow-scroll; 
}

pre {
  @apply text-xs;
}
</style>