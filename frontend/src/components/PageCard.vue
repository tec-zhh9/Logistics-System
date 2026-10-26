<template>
  <el-card class="page-card" :body-style="{ padding: '16px' }">
    <template v-if="title || $slots.header" #header>
      <div class="page-card-header">
        <span v-if="title" class="page-title">{{ title }}</span>
        <slot name="header" />
      </div>
    </template>
    <slot />
  </el-card>
</template>

<script setup>
defineProps({
  title: { type: String, default: '' }
})
</script>

<style scoped>
.page-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.06);
}
.page-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.page-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}
</style>
