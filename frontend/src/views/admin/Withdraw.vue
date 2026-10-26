<template>
  <PageCard title="提现记录管理">
    <SearchBar>
      <template #search>
        <el-select v-model="query.status" placeholder="按状态筛选" clearable style="width: 160px" @change="onSearch" @clear="onSearch">
          <el-option label="待审核" value="待审核" />
          <el-option label="已通过" value="已通过" />
          <el-option label="已驳回" value="已驳回" />
        </el-select>
      </template>
    </SearchBar>

    <DataTable :data="list" :loading="loading" :total="total" v-model:page="query.pageNum" v-model:size="query.pageSize" @page-change="applyPage" @size-change="applyPage">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="courierId" label="配送员ID" width="100" />
      <el-table-column prop="amount" label="提现金额(元)" width="120" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusType(row.status)">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="reason" label="审核意见" min-width="180" show-overflow-tooltip />
      <el-table-column prop="createTime" label="申请时间" width="180" />
      <el-table-column prop="auditTime" label="审核时间" width="180" />
      <el-table-column label="操作" width="100" fixed="right">
        <template #default="{ row }">
          <el-button v-if="row.status === '待审核'" size="small" type="primary" @click="openAudit(row)">审核</el-button>
        </template>
      </el-table-column>
    </DataTable>

    <FormDialog v-model="auditVisible" title="提现审核" :model="auditForm" :rules="auditRules" width="480px" @submit="submitAudit">
      <el-form-item label="审核结果" prop="status">
        <el-radio-group v-model="auditForm.status">
          <el-radio value="已通过">通过</el-radio>
          <el-radio value="已驳回">驳回</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="审核意见" prop="reason">
        <el-input v-model="auditForm.reason" type="textarea" :rows="3" placeholder="请输入审核意见（选填）" />
      </el-form-item>
    </FormDialog>
  </PageCard>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import PageCard from '../../components/PageCard.vue'
import SearchBar from '../../components/SearchBar.vue'
import DataTable from '../../components/DataTable.vue'
import FormDialog from '../../components/FormDialog.vue'
import { listWithdraw, auditWithdraw } from '../../api'

const all = ref([])
const list = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ pageNum: 1, pageSize: 10, status: '' })
const auditVisible = ref(false)
const auditId = ref(null)
const auditForm = reactive({ status: '已通过', reason: '' })
const auditRules = {
  status: [{ required: true, message: '请选择审核结果', trigger: 'change' }]
}

const statusType = status => {
  if (status === '已通过') return 'success'
  if (status === '已驳回') return 'danger'
  return 'warning'
}

// listWithdraw 返回全量列表（不分页），前端筛选 + 前端分页
const applyPage = () => {
  const filtered = query.status ? all.value.filter(r => r.status === query.status) : all.value
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
    const res = await listWithdraw()
    all.value = res.data || []
    applyPage()
  } finally {
    loading.value = false
  }
}

const openAudit = row => {
  auditId.value = row.id
  auditForm.status = '已通过'
  auditForm.reason = ''
  auditVisible.value = true
}

const submitAudit = async () => {
  await auditWithdraw(auditId.value, { status: auditForm.status, reason: auditForm.reason })
  ElMessage.success('审核完成')
  auditVisible.value = false
  load()
}

onMounted(load)
</script>
