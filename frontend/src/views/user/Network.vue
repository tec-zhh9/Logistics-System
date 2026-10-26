<template>
  <PageCard title="物流网点">
    <SearchBar>
      <template #search>
        <el-select v-model="query.regionId" placeholder="按区域筛选" clearable style="width: 200px" @change="load" @clear="load">
          <el-option v-for="r in regions" :key="r.id" :label="r.name" :value="r.id" />
        </el-select>
        <el-button type="primary" @click="load">搜索</el-button>
      </template>
    </SearchBar>

    <div v-loading="loading" class="network-grid">
      <el-empty v-if="!list.length" description="暂无网点" />
      <el-card v-for="item in list" :key="item.id" class="network-card" shadow="hover">
        <div class="network-header">
          <span class="network-name">{{ item.name }}</span>
          <el-tag size="small" type="primary">{{ regionName(item.regionId) }}</el-tag>
        </div>
        <div class="network-info">
          <div class="info-item">
            <span class="info-label">地址：</span>
            <span>{{ item.address }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">电话：</span>
            <span>{{ item.phone }}</span>
          </div>
          <div v-if="item.description" class="info-item desc">
            <span class="info-label">简介：</span>
            <span>{{ item.description }}</span>
          </div>
        </div>
      </el-card>
    </div>
  </PageCard>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import PageCard from '../../components/PageCard.vue'
import SearchBar from '../../components/SearchBar.vue'
import { listNetworkPoint, listRegion } from '../../api'

const list = ref([])
const regions = ref([])
const loading = ref(false)
const query = reactive({ regionId: null })

const regionName = id => {
  const r = regions.value.find(item => item.id === id)
  return r ? r.name : '-'
}

const loadRegions = async () => {
  const res = await listRegion()
  regions.value = res.data
}

const load = async () => {
  loading.value = true
  try {
    const res = await listNetworkPoint(query)
    list.value = res.data
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  load()
  loadRegions()
})
</script>

<style scoped>
.network-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 16px;
  min-height: 120px;
}
.network-card {
  border-radius: 8px;
}
.network-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}
.network-name {
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
.desc {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
