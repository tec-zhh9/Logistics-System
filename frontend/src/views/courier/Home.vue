<template>
  <div>
    <!-- 收入统计卡片 -->
    <el-row :gutter="16">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ stats.todayOrderCount ?? 0 }}</div>
          <div class="stat-label">今日接单数量</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value money">¥ {{ stats.todayIncome ?? 0 }}</div>
          <div class="stat-label">今日收入</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ stats.totalOrderCount ?? 0 }}</div>
          <div class="stat-label">总接单数量</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value money">¥ {{ stats.totalIncome ?? 0 }}</div>
          <div class="stat-label">总收入</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 系统公告 -->
    <el-card style="margin-top: 16px">
      <template #header><b>系统公告</b></template>
      <el-timeline>
        <el-timeline-item
          v-for="item in announcements"
          :key="item.id"
          :timestamp="item.createTime"
          placement="top"
        >
          <el-card shadow="hover">
            <h4>{{ item.title }}</h4>
            <div v-html="item.content" class="content"></div>
          </el-card>
        </el-timeline-item>
      </el-timeline>
      <el-empty v-if="!announcements.length" description="暂无公告" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCourierHome, listAnnouncement } from '../../api'

const stats = ref({})
const announcements = ref([])

onMounted(async () => {
  const [statsRes, annRes] = await Promise.all([getCourierHome(), listAnnouncement({})])
  stats.value = statsRes.data
  announcements.value = annRes.data
})
</script>

<style scoped>
.stat-card {
  text-align: center;
}
.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
}
.stat-value.money {
  color: #e6a23c;
}
.stat-label {
  margin-top: 8px;
  color: #999;
}
.content :deep(p) {
  color: #666;
  line-height: 1.6;
}
</style>
