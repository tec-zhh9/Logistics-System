<template>
  <el-container class="layout">
    <!-- 侧边菜单 -->
    <el-aside width="220px" class="aside">
      <div class="logo">
        <span class="logo-text">物流配送管理系统</span>
      </div>
      <el-menu
        :default-active="route.path"
        router
        background-color="#001529"
        text-color="#bfcbd9"
        active-text-color="#ffffff"
      >
        <el-menu-item v-for="menu in menus" :key="menu.path" :index="menu.path">
          <el-icon><component :is="menu.icon" /></el-icon>
          <span>{{ menu.title }}</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <!-- 顶栏 -->
      <el-header class="header">
        <div class="role-tag">{{ roleText }}</div>
        <el-dropdown @command="onCommand">
          <span class="user-info">
            <el-icon><UserFilled /></el-icon>
            {{ name }}
            <el-icon><ArrowDown /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">个人信息</el-dropdown-item>
              <el-dropdown-item command="password">修改密码</el-dropdown-item>
              <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>

      <!-- 主内容区 -->
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { MENU_CONFIG } from '../router/menus'

const route = useRoute()
const router = useRouter()

const role = localStorage.getItem('role') || ''
const name = localStorage.getItem('name') || '未知用户'

const roleText = computed(
  () => ({ ADMIN: '管理员端', COURIER: '配送员端', USER: '用户端' }[role] || '')
)
const menus = computed(() => MENU_CONFIG[role] || [])

const onCommand = command => {
  if (command === 'logout') {
    localStorage.clear()
    router.push('/login')
  } else if (command === 'profile') {
    router.push(`/${role.toLowerCase()}/profile`)
  } else if (command === 'password') {
    router.push(`/${role.toLowerCase()}/password`)
  }
}
</script>

<style scoped>
.layout {
  height: 100%;
}
.aside {
  background-color: #001529;
  overflow-x: hidden;
}
.aside :deep(.el-menu) {
  border-right: none;
}
.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}
.logo-text {
  font-size: 17px;
  font-weight: 600;
  letter-spacing: 2px;
  background: linear-gradient(135deg, #e67e22 0%, #f39c12 50%, #f5b041 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  text-shadow: 0 2px 8px rgba(230, 126, 34, 0.15);
}
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  border-bottom: 1px solid #e8e8e8;
}
.role-tag {
  font-weight: bold;
  color: #409eff;
}
.user-info {
  display: flex;
  align-items: center;
  gap: 4px;
  cursor: pointer;
  color: #333;
}
.main {
  background: #f0f2f5;
}
</style>
