<template>
  <div class="search-bar">
    <div class="search-left">
      <slot name="search" />
    </div>
    <div class="search-right">
      <slot name="actions" />
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.search-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
}
.search-left {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}
.search-right {
  display: flex;
  align-items: center;
  gap: 8px;
}
</style>
