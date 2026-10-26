<template>
  <div class="login-page">
    <el-card class="login-card">
      <h2 class="title">物流配送管理系统</h2>
      <el-form ref="formRef" :model="form" :rules="rules" size="large">
        <el-form-item>
          <el-radio-group v-model="form.role" style="width: 100%">
            <el-radio-button value="USER" style="width: 33.3%">普通用户</el-radio-button>
            <el-radio-button value="COURIER" style="width: 33.3%">配送员</el-radio-button>
            <el-radio-button value="ADMIN" style="width: 33.3%">管理员</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" :prefix-icon="User" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            show-password
            placeholder="请输入密码"
            :prefix-icon="Lock"
            @keyup.enter="onLogin"
          />
        </el-form-item>
        <el-button type="primary" size="large" style="width: 100%" @click="onLogin">登 录</el-button>
        <div class="tips">
          还没有账号？<router-link to="/register">立即注册</router-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { login } from '../api'

const router = useRouter()
const formRef = ref()
const form = reactive({ role: 'USER', username: '', password: '' })

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const onLogin = () => {
  formRef.value.validate(async valid => {
    if (!valid) return
    const res = await login(form)
    localStorage.setItem('token', res.data.token)
    localStorage.setItem('role', res.data.role)
    localStorage.setItem('name', res.data.name || res.data.username || '用户')
    ElMessage.success('登录成功')
    router.push('/' + res.data.role.toLowerCase())
  })
}
</script>

<style scoped>
.login-page {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1f6feb 0%, #3fb1e3 100%);
}
.login-card {
  width: 400px;
  padding: 12px 8px;
}
.title {
  text-align: center;
  margin-bottom: 24px;
  color: #333;
}
.tips {
  text-align: center;
  margin-top: 12px;
  color: #999;
  font-size: 14px;
}
</style>
