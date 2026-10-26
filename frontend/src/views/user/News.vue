<template>
  <PageCard title="物流资讯">
    <SearchBar>
      <template #search>
        <el-input v-model="query.title" placeholder="按标题搜索" clearable style="width: 200px" @keyup.enter="load" @clear="load" />
        <el-button type="primary" @click="load">搜索</el-button>
      </template>
    </SearchBar>

    <div v-loading="loading" class="news-list">
      <el-empty v-if="!list.length" description="暂无资讯" />
      <div v-for="item in list" :key="item.id" class="news-item">
        <div class="news-main">
          <div class="news-title" @click="openDetail(item)">{{ item.title }}</div>
          <div class="news-summary">{{ summary(item.content) }}</div>
          <div class="news-time">发布时间：{{ item.createTime }}</div>
        </div>
      </div>
    </div>

    <el-pagination
      v-model:current-page="query.pageNum"
      v-model:page-size="query.pageSize"
      :total="total"
      :page-sizes="[10, 20, 50]"
      layout="total, sizes, prev, pager, next"
      style="margin-top: 12px; justify-content: flex-end"
      @current-change="load"
      @size-change="load"
    />

    <el-dialog v-model="detailVisible" :title="detail.title" width="760px">
      <div class="detail-time">发布时间：{{ detail.createTime }}</div>
      <div class="detail-content" v-html="detail.content"></div>
    </el-dialog>
  </PageCard>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import PageCard from '../../components/PageCard.vue'
import SearchBar from '../../components/SearchBar.vue'
import { pageNews } from '../../api'

const list = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ pageNum: 1, pageSize: 10, title: '' })
const detailVisible = ref(false)
const detail = reactive({ id: null, title: '', content: '', createTime: '' })

const summary = content => {
  if (!content) return ''
  const text = content.replace(/<[^>]+>/g, '')
  return text.length > 120 ? text.slice(0, 120) + '...' : text
}

const load = async () => {
  loading.value = true
  try {
    const res = await pageNews(query)
    list.value = res.data.list
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const openDetail = row => {
  detail.id = row.id
  detail.title = row.title
  detail.content = row.content
  detail.createTime = row.createTime
  detailVisible.value = true
}

onMounted(load)
</script>

<style scoped>
.news-list {
  min-height: 120px;
}
.news-item {
  padding: 16px 0;
  border-bottom: 1px solid #ebeef5;
}
.news-item:last-child {
  border-bottom: none;
}
.news-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  cursor: pointer;
  margin-bottom: 8px;
}
.news-title:hover {
  color: #409eff;
}
.news-summary {
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
  margin-bottom: 8px;
}
.news-time {
  font-size: 12px;
  color: #909399;
}
.detail-time {
  font-size: 12px;
  color: #909399;
  margin-bottom: 16px;
}
.detail-content {
  font-size: 14px;
  line-height: 1.8;
  color: #303133;
}
</style>
