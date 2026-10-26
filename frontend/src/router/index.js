import { createRouter, createWebHistory } from 'vue-router'

// 占位页面：功能框架已就绪，后续逐个替换为真实页面
import Placeholder from '../views/Placeholder.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: () => import('../views/Login.vue'), meta: { title: '登录' } },
  { path: '/register', component: () => import('../views/Register.vue'), meta: { title: '注册' } },

  // ================= 管理员端 =================
  {
    path: '/admin',
    component: () => import('../layout/MainLayout.vue'),
    meta: { roles: ['ADMIN'] },
    children: [
      { path: '', redirect: '/admin/dashboard' },
      { path: 'dashboard', component: () => import('../views/admin/Dashboard.vue'), meta: { title: '首页看板' } },
      { path: 'announcement', component: () => import('../views/admin/Announcement.vue'), meta: { title: '系统公告' } },
      { path: 'category', component: () => import('../views/admin/Category.vue'), meta: { title: '寄件类别' } },
      { path: 'region', component: () => import('../views/admin/Region.vue'), meta: { title: '区域管理' } },
      { path: 'network-point', component: () => import('../views/admin/NetworkPoint.vue'), meta: { title: '网点管理' } },
      { path: 'news', component: () => import('../views/admin/News.vue'), meta: { title: '物流资讯' } },
      { path: 'evaluation', component: () => import('../views/admin/Evaluation.vue'), meta: { title: '评价管理' } },
      { path: 'freight-rule', component: () => import('../views/admin/FreightRule.vue'), meta: { title: '运费规则' } },
      { path: 'withdraw', component: () => import('../views/admin/Withdraw.vue'), meta: { title: '提现记录' } },
      { path: 'recharge', component: () => import('../views/admin/Recharge.vue'), meta: { title: '充值记录' } },
      { path: 'courier-cert', component: () => import('../views/admin/CourierCert.vue'), meta: { title: '配送员认证' } },
      { path: 'info', component: () => import('../views/admin/AdminInfo.vue'), meta: { title: '管理员信息' } },
      { path: 'courier', component: () => import('../views/admin/CourierManage.vue'), meta: { title: '配送员管理' } },
      { path: 'user', component: () => import('../views/admin/UserManage.vue'), meta: { title: '用户管理' } },
      { path: 'profile', component: () => import('../views/Profile.vue'), meta: { title: '个人信息' } },
      { path: 'password', component: () => import('../views/Password.vue'), meta: { title: '修改密码' } }
    ]
  },

  // ================= 配送员端 =================
  {
    path: '/courier',
    component: () => import('../layout/MainLayout.vue'),
    meta: { roles: ['COURIER'] },
    children: [
      { path: '', redirect: '/courier/home' },
      { path: 'home', component: () => import('../views/courier/Home.vue'), meta: { title: '首页' } },
      { path: 'pending', component: () => import('../views/courier/PendingOrder.vue'), meta: { title: '待接订单' } },
      { path: 'history', component: () => import('../views/courier/HistoryOrder.vue'), meta: { title: '历史接单' } },
      { path: 'withdraw', component: () => import('../views/courier/Withdraw.vue'), meta: { title: '提现记录' } },
      { path: 'profile', component: () => import('../views/Profile.vue'), meta: { title: '个人信息' } },
      { path: 'password', component: () => import('../views/Password.vue'), meta: { title: '修改密码' } }
    ]
  },

  // ================= 普通用户端 =================
  {
    path: '/user',
    component: () => import('../layout/MainLayout.vue'),
    meta: { roles: ['USER'] },
    children: [
      { path: '', redirect: '/user/home' },
      { path: 'home', component: () => import('../views/user/Home.vue'), meta: { title: '首页' } },
      { path: 'network', component: () => import('../views/user/Network.vue'), meta: { title: '物流网点' } },
      { path: 'orders', component: () => import('../views/user/Orders.vue'), meta: { title: '历史寄件' } },
      { path: 'news', component: () => import('../views/user/News.vue'), meta: { title: '物流资讯' } },
      { path: 'cert', component: () => import('../views/user/Cert.vue'), meta: { title: '配送员认证' } },
      { path: 'recharge', component: () => import('../views/user/Recharge.vue'), meta: { title: '充值记录' } },
      { path: 'address', component: () => import('../views/user/Address.vue'), meta: { title: '我的地址' } },
      { path: 'evaluation', component: () => import('../views/user/Evaluation.vue'), meta: { title: '我的评价' } },
      { path: 'favorite', component: () => import('../views/user/Favorite.vue'), meta: { title: '我的收藏' } },
      { path: 'message', component: () => import('../views/user/Message.vue'), meta: { title: '消息提醒' } },
      { path: 'announcement', component: () => import('../views/user/Announcement.vue'), meta: { title: '系统公告' } },
      { path: 'profile', component: () => import('../views/Profile.vue'), meta: { title: '个人信息' } },
      { path: 'password', component: () => import('../views/Password.vue'), meta: { title: '修改密码' } }
    ]
  },

  { path: '/:pathMatch(.*)*', redirect: '/login' }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 全局路由守卫：未登录跳转登录页，角色不匹配禁止访问
router.beforeEach(to => {
  const token = localStorage.getItem('token')
  const role = localStorage.getItem('role')
  if (!token && to.path !== '/login' && to.path !== '/register') {
    return '/login'
  }
  const parentRole = to.matched.find(m => m.meta.roles)?.meta.roles
  if (parentRole && role && !parentRole.includes(role)) {
    return '/login'
  }
})

export default router
