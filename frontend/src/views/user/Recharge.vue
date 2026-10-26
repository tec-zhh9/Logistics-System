<template>
  <PageCard title="充值记录">
    <div class="balance-card">
      <div class="balance-info">
        <div class="balance-label">当前账户余额（元）</div>
        <div class="balance-value">¥ {{ balance.toFixed(2) }}</div>
      </div>
      <el-button type="success" @click="openDialog">充值</el-button>
    </div>

    <DataTable
      :data="pagedList"
      :loading="loading"
      :total="list.length"
      v-model:page="pageNum"
      v-model:size="pageSize"
    >
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="amount" label="充值金额(元)" width="160" />
      <el-table-column prop="createTime" label="充值时间" min-width="180" />
    </DataTable>

    <FormDialog v-model="dialogVisible" title="账户充值" :model="form" :rules="rules" @submit="save">
      <el-form-item label="充值金额" prop="amount">
        <el-input-number v-model="form.amount" :min="0.01" :precision="2" :step="10" style="width: 100%" placeholder="请输入充值金额" />
      </el-form-item>
    </FormDialog>
  </PageCard>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import PageCard from '../../components/PageCard.vue'
import DataTable from '../../components/DataTable.vue'
import FormDialog from '../../components/FormDialog.vue'
import { listMyRecharge, addRecharge, getProfile } from '../../api'

const list = ref([])
const loading = ref(false)
const balance = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

const dialogVisible = ref(false)
const form = reactive({ amount: null })
const rules = {
  amount: [{ required: true, message: '请输入充值金额', trigger: 'blur' }]
}

const pagedList = computed(() => {
  const start = (pageNum.value - 1) * pageSize.value
  return list.value.slice(start, start + pageSize.value)
})

const load = async () => {
  loading.value = true
  try {
    const res = await listMyRecharge()
    list.value = res.data.list || res.data
    pageNum.value = 1
  } finally {
    loading.value = false
  }
}

const loadBalance = async () => {
  try {
    const res = await getProfile()
    balance.value = Number(res.data.balance ?? 0)
  } catch {
    balance.value = 0
  }
}

const openDialog = () => {
  form.amount = null
  dialogVisible.value = true
}

const save = async () => {
  await addRecharge({ amount: form.amount })
  ElMessage.success('充值成功')
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
