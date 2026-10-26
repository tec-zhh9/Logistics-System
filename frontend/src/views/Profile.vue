<template>
  <el-card style="max-width: 560px">
    <template #header><b>个人信息</b></template>
    <el-form :model="form" label-width="90px">
      <el-form-item label="用户名">
        <el-input v-model="form.username" disabled />
      </el-form-item>
      <el-form-item label="姓名">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item v-if="role !== 'ADMIN'" label="性别">
        <el-radio-group v-model="form.gender">
          <el-radio value="男">男</el-radio>
          <el-radio value="女">女</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="手机号">
        <el-input v-model="form.phone" />
      </el-form-item>
      <el-form-item v-if="role === 'ADMIN'" label="邮箱">
        <el-input v-model="form.email" />
      </el-form-item>
      <el-form-item v-if="role === 'USER'" label="账户余额">
        <el-tag type="warning" size="large">{{ form.balance }} 元</el-tag>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSave">保存修改</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getProfile, updateProfile } from '../api'

const role = localStorage.getItem('role')
const form = reactive({})
const loaded = ref(false)

onMounted(async () => {
  const res = await getProfile()
  Object.assign(form, res.data)
  loaded.value = true
})

const onSave = () => {
  updateProfile(form).then(() => {
    if (form.name) localStorage.setItem('name', form.name)
    ElMessage.success('保存成功')
  })
}
</script>
