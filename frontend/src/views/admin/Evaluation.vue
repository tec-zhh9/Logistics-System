<template>
  <PageCard title="评价管理">
    <SearchBar>
      <template #search>
        <el-input v-model="query.orderId" placeholder="按订单号搜索" clearable style="width: 200px" @keyup.enter="onSearch" @clear="onSearch" />
        <el-button type="primary" @click="onSearch">搜索</el-button>
      </template>
    </SearchBar>

    <DataTable :data="list" :loading="loading" :total="total" v-model:page="query.pageNum" v-model:size="query.pageSize" @page-change="load" @size-change="load">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="orderId" label="订单号" width="100" />
      <el-table-column prop="userId" label="用户ID" width="90" />
      <el-table-column prop="courierId" label="配送员ID" width="100" />
      <el-table-column label="评分" width="160">
        <template #default="{ row }">
          <el-rate :model-value="row.score" disabled />
        </template>
      </el-table-column>
      <el-table-column prop="content" label="评价内容" min-width="220" show-overflow-tooltip />
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="100" fixed="right">
        <template #default="{ row }">
          <el-popconfirm title="确定删除该评价吗？" @confirm="remove(row.id)">
            <template #reference><el-button size="small" type="danger">删除</el-button></template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </DataTable>
  </PageCard>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import PageCard from '../../components/PageCard.vue'
import SearchBar from '../../components/SearchBar.vue'
import DataTable from '../../components/DataTable.vue'
import { pageEvaluation, deleteEvaluation } from '../../api'

const list = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ pageNum: 1, pageSize: 10, orderId: '' })

const load = async () => {
  loading.value = true
  try {
    const res = await pageEvaluation({ pageNum: query.pageNum, pageSize: query.pageSize })
    const data = res.data.list || []
    // 后端分页接口不支持订单号过滤，在当前页结果中筛选
    list.value = query.orderId ? data.filter(e => String(e.orderId).includes(query.orderId.trim())) : data
    total.value = res.data.total || 0
  } finally {
    loading.value = false
  }
}

const onSearch = () => {
  query.pageNum = 1
  load()
}

const remove = async id => {
  await deleteEvaluation(id)
  ElMessage.success('删除成功')
  load()
}

onMounted(load)
</script>
