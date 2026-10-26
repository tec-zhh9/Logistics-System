<template>
  <el-card>
    <template #header>
      <b>{{ route.meta.title }}</b>
    </template>
    <el-empty description="页面框架已就绪，功能待补充实现">
      <el-text type="info" size="small">
        后端接口已按统一模式提供（/api 下对应模块），前端页面可参考 Announcement.vue 的 CRUD 模式补充
      </el-text>
    </el-empty>
  </el-card>
</template>

<script setup>
import { useRoute } from 'vue-router'

const route = useRoute()
</script>
