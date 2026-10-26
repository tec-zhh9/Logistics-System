<template>
  <PageCard title="充值记录管理">
    <SearchBar>
      <template #search>
        <el-input v-model="query.userId" placeholder="按用户ID搜索" clearable style="width: 200px" @keyup.enter="onSearch" @clear="onSearch" />
        <el-button type="primary" @click="onSearch">搜索</el-button>
      </template>
    </SearchBar>

    <DataTable :data="list" :loading="loading" :total="total" v-model:page="query.pageNum" v-model:size="query.pageSize" @page-change="applyPage" @size-change="applyPage">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="userId" label="用户ID" width="120" />
      <el-table-column prop="amount" label="充值金额(元)" width="140" />
      <el-table-column prop="createTime" label="充值时间" width="200" />
    </DataTable>
  </PageCard>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import PageCard from '../../components/PageCard.vue'
import SearchBar from '../../components/SearchBar.vue'
import DataTable from '../../components/DataTable.vue'
import { listRecharge } from '../../api'

const all = ref([])
const list = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ pageNum: 1, pageSize: 10, userId: '' })

// listRecharge 返回全量列表（不分页），前端筛选 + 前端分页
const applyPage = () => {
  const keyword = query.userId.trim()
  const filtered = keyword ? all.value.filter(r => String(r.userId).includes(keyword)) : all.value
  total.value = filtered.length
  const start = (query.pageNum - 1) * query.pageSize
  list.value = filtered.slice(start, start + query.pageSize)
}

const onSearch = () => {
  query.pageNum = 1
  applyPage()
}

const load = async () => {
  loading.value = true
  try {
    const res = await listRecharge()
    all.value = res.data || []
    applyPage()
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>
