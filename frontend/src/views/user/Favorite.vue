<template>
  <PageCard title="我的收藏">
    <div v-loading="loading" class="favorite-grid">
      <el-empty v-if="!list.length" description="暂无收藏的网点" />
      <el-card v-for="item in list" :key="item.id" class="favorite-card" shadow="hover">
        <div class="favorite-header">
          <span class="favorite-name">{{ item.name }}</span>
          <el-popconfirm title="确定取消收藏该网点吗？" @confirm="remove(item.networkPointId)">
            <template #reference>
              <el-button size="small" type="danger" plain>取消收藏</el-button>
            </template>
          </el-popconfirm>
        </div>
        <div class="favorite-info">
          <div class="info-item">
            <span class="info-label">地址：</span>
            <span>{{ item.address }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">电话：</span>
            <span>{{ item.phone }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">收藏时间：</span>
            <span>{{ item.createTime }}</span>
          </div>
        </div>
      </el-card>
    </div>
  </PageCard>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import PageCard from '../../components/PageCard.vue'
import { listMyFavorite, deleteFavorite } from '../../api'

const list = ref([])
const loading = ref(false)

const load = async () => {
  loading.value = true
  try {
    const res = await listMyFavorite()
    list.value = res.data.list || res.data
  } finally {
    loading.value = false
  }
}

const remove = async networkPointId => {
  await deleteFavorite(networkPointId)
  ElMessage.success('已取消收藏')
  load()
}

onMounted(load)
</script>

<style scoped>
.favorite-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 16px;
  min-height: 120px;
}
.favorite-card {
  border-radius: 8px;
}
.favorite-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}
.favorite-name {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}
.info-item {
  font-size: 14px;
  color: #606266;
  line-height: 1.8;
}
.info-label {
  color: #909399;
}
</style>
