<template>
  <PageCard title="配送员认证申请">
    <div v-loading="loading">
      <!-- 已有申请（待审核/已通过）：展示详情卡片 -->
      <template v-if="cert">
        <div class="cert-status">
          <span class="cert-status-label">当前状态：</span>
          <el-tag :type="statusType(cert.status)" size="large">{{ cert.status }}</el-tag>
        </div>
        <el-descriptions :column="2" border class="cert-detail">
          <el-descriptions-item label="姓名">{{ cert.name }}</el-descriptions-item>
          <el-descriptions-item label="电话">{{ cert.phone }}</el-descriptions-item>
          <el-descriptions-item label="身份证号">{{ cert.idCard }}</el-descriptions-item>
          <el-descriptions-item label="申请区域">{{ regionName(cert.regionId) }}</el-descriptions-item>
          <el-descriptions-item label="审核意见">{{ cert.reason || '-' }}</el-descriptions-item>
          <el-descriptions-item label="申请时间">{{ cert.createTime }}</el-descriptions-item>
          <el-descriptions-item label="审核时间">{{ cert.auditTime || '-' }}</el-descriptions-item>
        </el-descriptions>
        <el-alert
          v-if="cert.status === '已通过'"
          type="success"
          title="恭喜，您已通过配送员认证，可使用配送员账号登录配送端。"
          :closable="false"
          style="margin-top: 16px"
        />
      </template>

      <!-- 无申请或已被驳回：展示申请表单 -->
      <template v-else>
        <el-alert
          v-if="rejected"
          type="error"
          :title="`上次申请已被驳回${rejected.reason ? '：' + rejected.reason : ''}，请修改后重新提交`"
          :closable="false"
          style="margin-bottom: 16px"
        />
        <el-form ref="formRef" :model="form" :rules="rules" label-width="90px" style="max-width: 520px">
          <el-form-item label="姓名" prop="name">
            <el-input v-model="form.name" placeholder="请输入真实姓名" />
          </el-form-item>
          <el-form-item label="电话" prop="phone">
            <el-input v-model="form.phone" placeholder="请输入联系电话" />
          </el-form-item>
          <el-form-item label="身份证号" prop="idCard">
            <el-input v-model="form.idCard" placeholder="请输入身份证号" />
          </el-form-item>
          <el-form-item label="申请区域" prop="regionId">
            <el-select v-model="form.regionId" placeholder="请选择申请负责的区域" style="width: 100%">
              <el-option v-for="r in regions" :key="r.id" :label="r.name" :value="r.id" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="submitting" @click="submit">提交认证申请</el-button>
          </el-form-item>
        </el-form>
      </template>
    </div>
  </PageCard>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import PageCard from '../../components/PageCard.vue'
import { getMyCert, addCourierCert, listRegion } from '../../api'

const loading = ref(false)
const submitting = ref(false)
const cert = ref(null)
const rejected = ref(null)
const regions = ref([])
const formRef = ref()

const form = reactive({ name: '', phone: '', idCard: '', regionId: null })
const rules = {
  name: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
  idCard: [{ required: true, message: '请输入身份证号', trigger: 'blur' }],
  regionId: [{ required: true, message: '请选择申请区域', trigger: 'change' }]
}

const regionName = id => regions.value.find(r => r.id === id)?.name || '-'
const statusType = s => (s === '已通过' ? 'success' : s === '已驳回' ? 'danger' : 'warning')

const load = async () => {
  loading.value = true
  try {
    const res = await getMyCert()
    const list = (res.data || []).slice().sort((a, b) => b.id - a.id)
    const latest = list[0] || null
    if (latest && latest.status !== '已驳回') {
      cert.value = latest
      rejected.value = null
    } else {
      cert.value = null
      rejected.value = latest
    }
  } finally {
    loading.value = false
  }
}

const submit = async () => {
  await formRef.value.validate()
  submitting.value = true
  try {
    await addCourierCert({ ...form })
    ElMessage.success('认证申请已提交，请等待审核')
    load()
  } finally {
    submitting.value = false
  }
}

onMounted(async () => {
  const res = await listRegion()
  regions.value = res.data || []
  load()
})
</script>

<style scoped>
.cert-status {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
}
.cert-status-label {
  font-size: 14px;
  color: #606266;
}
.cert-detail {
  max-width: 860px;
}
</style>
