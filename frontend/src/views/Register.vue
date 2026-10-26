<template>
  <div class="register-page">
    <el-card class="register-card">
      <h2 class="title">注册账号</h2>
      <el-form ref="formRef" :model="form" :rules="rules" size="large">
        <el-form-item>
          <el-radio-group v-model="form.role" style="width: 100%">
            <el-radio-button value="USER" style="width: 50%">注册普通用户</el-radio-button>
            <el-radio-button value="COURIER" style="width: 50%">注册配送员</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" />
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input v-model="form.confirmPassword" type="password" show-password placeholder="请确认密码" />
        </el-form-item>
        <el-button type="primary" size="large" style="width: 100%" @click="onRegister">注 册</el-button>
        <div class="tips">
          已有账号？<router-link to="/login">返回登录</router-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { register } from '../api'

const router = useRouter()
const formRef = ref()
const form = reactive({
  role: 'USER',
  username: '',
  name: '',
  phone: '',
  password: '',
  confirmPassword: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== form.password) callback(new Error('两次输入的密码不一致'))
        else callback()
      },
      trigger: 'blur'
    }
  ]
}

const onRegister = () => {
  formRef.value.validate(async valid => {
    if (!valid) return
    await register(form)
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  })
}
</script>

<style scoped>
.register-page {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1f6feb 0%, #3fb1e3 100%);
}
.register-card {
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
