<template>
  <PageCard title="配送员认证审核">
    <SearchBar>
      <template #search>
        <el-select
          v-model="query.status"
          placeholder="按状态筛选"
          clearable
          style="width: 160px"
          @change="onSearch"
          @clear="onSearch"
        >
          <el-option label="待审核" value="待审核" />
          <el-option label="已通过" value="已通过" />
          <el-option label="已驳回" value="已驳回" />
        </el-select>
        <el-button type="primary" @click="onSearch">搜索</el-button>
      </template>
    </SearchBar>

    <DataTable
      :data="list"
      :loading="loading"
      :total="total"
      v-model:page="query.pageNum"
      v-model:size="query.pageSize"
      @page-change="applyPage"
      @size-change="applyPage"
    >
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="userId" label="申请人ID" width="90" />
      <el-table-column prop="name" label="姓名" min-width="100" />
      <el-table-column prop="phone" label="电话" min-width="120" />
      <el-table-column prop="idCard" label="身份证号" min-width="170" show-overflow-tooltip />
      <el-table-column label="申请区域" min-width="110">
        <template #default="{ row }">{{ regionName(row.regionId) }}</template>
      </el-table-column>
      <el-table-column label="状态" width="90">
        <template #default="{ row }">
          <el-tag :type="statusType(row.status)">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="reason" label="审核意见" min-width="140" show-overflow-tooltip />
      <el-table-column prop="createTime" label="申请时间" width="170" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" :disabled="row.status !== '待审核'" @click="openAudit(row)">
            审核
          </el-button>
          <el-popconfirm title="确定删除该申请吗？" @confirm="remove(row.id)">
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </DataTable>

    <FormDialog v-model="auditVisible" title="认证审核" :model="auditForm" :rules="auditRules" @submit="doAudit">
      <el-form-item label="申请人">
        <el-input :model-value="`${auditForm.name}（${auditForm.phone}）`" disabled />
      </el-form-item>
      <el-form-item label="审核结果" prop="status">
        <el-radio-group v-model="auditForm.status">
          <el-radio value="已通过">通过</el-radio>
          <el-radio value="已驳回">驳回</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="审核意见" prop="reason">
        <el-input
          v-model="auditForm.reason"
          type="textarea"
          :rows="3"
          placeholder="请输入审核意见（驳回时必填）"
        />
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
import { listCourierCert, auditCourierCert, deleteCourierCert, listRegion } from '../../api'

const allList = ref([])
const list = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ pageNum: 1, pageSize: 10, status: '' })
const regions = ref([])

const auditVisible = ref(false)
const auditForm = reactive({ id: null, name: '', phone: '', status: '已通过', reason: '' })
const auditRules = {
  status: [{ required: true, message: '请选择审核结果', trigger: 'change' }],
  reason: [
    {
      validator: (rule, value, callback) => {
        if (auditForm.status === '已驳回' && (!value || !value.trim())) {
          callback(new Error('驳回时请填写审核意见'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const regionName = id => regions.value.find(r => r.id === id)?.name || '-'
const statusType = s => (s === '已通过' ? 'success' : s === '已驳回' ? 'danger' : 'warning')

const applyPage = () => {
  const filtered = query.status ? allList.value.filter(item => item.status === query.status) : allList.value
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
    const res = await listCourierCert()
    allList.value = res.data || []
    applyPage()
  } finally {
    loading.value = false
  }
}

const openAudit = row => {
  auditForm.id = row.id
  auditForm.name = row.name
  auditForm.phone = row.phone
  auditForm.status = '已通过'
  auditForm.reason = ''
  auditVisible.value = true
}

const doAudit = async () => {
  await auditCourierCert(auditForm.id, { status: auditForm.status, reason: auditForm.reason })
  ElMessage.success('审核完成')
  auditVisible.value = false
  load()
}

const remove = async id => {
  await deleteCourierCert(id)
  ElMessage.success('删除成功')
  load()
}

onMounted(async () => {
  const res = await listRegion()
  regions.value = res.data || []
  load()
})
</script>
