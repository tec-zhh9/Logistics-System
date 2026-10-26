<template>
  <div>
    <el-row :gutter="16">
      <!-- 热门寄件网点 -->
      <el-col :span="14">
        <el-card>
          <template #header><b>🔥 热门寄件网点</b></template>
          <el-table :data="hotNetworkPoints" border stripe>
            <el-table-column type="index" label="排名" width="70" />
            <el-table-column prop="name" label="网点名称" min-width="150" />
            <el-table-column prop="address" label="地址" min-width="180" />
            <el-table-column prop="phone" label="电话" width="140" />
            <el-table-column prop="orderCount" label="寄件量" width="90" />
          </el-table>
          <el-empty v-if="!hotNetworkPoints.length" description="暂无数据" />
        </el-card>
      </el-col>

      <!-- 热门物流资讯 -->
      <el-col :span="10">
        <el-card>
          <template #header><b>📰 热门物流资讯</b></template>
          <div v-for="item in hotNews" :key="item.id" class="news-item">
            <div>
              <div class="news-title">{{ item.title }}</div>
              <div class="news-time">{{ item.createTime }}</div>
            </div>
            <el-tag type="info" size="small">{{ item.viewCount }} 浏览</el-tag>
          </div>
          <el-empty v-if="!hotNews.length" description="暂无资讯" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getUserHome } from '../../api'

const hotNetworkPoints = ref([])
const hotNews = ref([])

onMounted(async () => {
  const res = await getUserHome()
  hotNetworkPoints.value = res.data.hotNetworkPoints || []
  hotNews.value = res.data.hotNews || []
})
</script>

<style scoped>
.news-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px dashed #eee;
}
.news-title {
  font-weight: bold;
  color: #333;
}
.news-time {
  margin-top: 4px;
  font-size: 12px;
  color: #999;
}
</style>
