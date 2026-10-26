<template>
  <PageCard title="提现记录">
    <div class="balance-card">
      <div class="balance-info">
        <div class="balance-label">当前可提现余额（元）</div>
        <div class="balance-value">¥ {{ balance.toFixed(2) }}</div>
      </div>
      <el-button type="success" @click="openDialog">发起提现申请</el-button>
    </div>

    <DataTable :data="list" :loading="loading" :show-pagination="false">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="amount" label="提现金额(元)" width="130" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusType(row.status)">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="reason" label="审核意见" min-width="200" show-overflow-tooltip />
      <el-table-column prop="createTime" label="申请时间" width="170" />
      <el-table-column prop="auditTime" label="审核时间" width="170" />
    </DataTable>

    <FormDialog v-model="dialogVisible" title="发起提现申请" :model="form" :rules="rules" @submit="save">
      <el-form-item label="提现金额" prop="amount">
        <el-input-number v-model="form.amount" :min="0.01" :max="balance" :precision="2" :step="10" style="width: 100%" placeholder="请输入提现金额" />
      </el-form-item>
    </FormDialog>
  </PageCard>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import PageCard from '../../components/PageCard.vue'
import DataTable from '../../components/DataTable.vue'
import FormDialog from '../../components/FormDialog.vue'
import { listMyWithdraw, addWithdraw, getCourierHome } from '../../api'

const list = ref([])
const loading = ref(false)
const balance = ref(0)
const dialogVisible = ref(false)
const form = reactive({ amount: null })
const rules = {
  amount: [{ required: true, message: '请输入提现金额', trigger: 'blur' }]
}

const statusType = status => {
  if (status === '待审核') return 'warning'
  if (status === '已通过') return 'success'
  if (status === '已驳回') return 'danger'
  return ''
}

const load = async () => {
  loading.value = true
  try {
    const res = await listMyWithdraw()
    list.value = res.data.list || res.data
  } finally {
    loading.value = false
  }
}

const loadBalance = async () => {
  try {
    const res = await getCourierHome()
    balance.value = res.data.balance ?? res.data.amount ?? 0
  } catch {
    balance.value = 0
  }
}

const openDialog = () => {
  form.amount = null
  dialogVisible.value = true
}

const save = async () => {
  if (form.amount > balance.value) {
    ElMessage.warning('提现金额不能超过可提现余额')
    return
  }
  await addWithdraw({ amount: form.amount })
  ElMessage.success('提现申请已提交')
  dialogVisible.value = false
  load()
  loadBalance()
}

onMounted(() => {
  load()
  loadBalance()
})
</script>

<style scoped>
.balance-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  margin-bottom: 16px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 8px;
  color: #fff;
}
.balance-label {
  font-size: 14px;
  opacity: 0.9;
  margin-bottom: 6px;
}
.balance-value {
  font-size: 28px;
  font-weight: 600;
}
</style>
