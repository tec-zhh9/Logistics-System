// 三端菜单配置（icon 为全局注册的 Element Plus 图标名）
export const MENU_CONFIG = {
  ADMIN: [
    { path: '/admin/dashboard', title: '首页看板', icon: 'Odometer' },
    { path: '/admin/announcement', title: '系统公告', icon: 'Bell' },
    { path: '/admin/category', title: '寄件类别', icon: 'Goods' },
    { path: '/admin/region', title: '区域管理', icon: 'Location' },
    { path: '/admin/network-point', title: '网点管理', icon: 'OfficeBuilding' },
    { path: '/admin/news', title: '物流资讯', icon: 'Newspaper' },
    { path: '/admin/evaluation', title: '评价管理', icon: 'ChatDotRound' },
    { path: '/admin/freight-rule', title: '运费规则', icon: 'Coin' },
    { path: '/admin/withdraw', title: '提现记录', icon: 'Wallet' },
    { path: '/admin/recharge', title: '充值记录', icon: 'Money' },
    { path: '/admin/courier-cert', title: '配送员认证', icon: 'Postcard' },
    { path: '/admin/info', title: '管理员信息', icon: 'UserFilled' },
    { path: '/admin/courier', title: '配送员管理', icon: 'Van' },
    { path: '/admin/user', title: '用户管理', icon: 'User' },
    { path: '/admin/profile', title: '个人信息', icon: 'Setting' },
    { path: '/admin/password', title: '修改密码', icon: 'Lock' }
  ],
  COURIER: [
    { path: '/courier/home', title: '首页', icon: 'Odometer' },
    { path: '/courier/pending', title: '待接订单', icon: 'Bell' },
    { path: '/courier/history', title: '历史接单', icon: 'Tickets' },
    { path: '/courier/withdraw', title: '提现记录', icon: 'Wallet' },
    { path: '/courier/profile', title: '个人信息', icon: 'Setting' },
    { path: '/courier/password', title: '修改密码', icon: 'Lock' }
  ],
  USER: [
    { path: '/user/home', title: '首页', icon: 'Odometer' },
    { path: '/user/network', title: '物流网点', icon: 'OfficeBuilding' },
    { path: '/user/orders', title: '历史寄件', icon: 'Tickets' },
    { path: '/user/news', title: '物流资讯', icon: 'Newspaper' },
    { path: '/user/cert', title: '配送员认证', icon: 'Postcard' },
    { path: '/user/recharge', title: '充值记录', icon: 'Money' },
    { path: '/user/address', title: '我的地址', icon: 'Location' },
    { path: '/user/evaluation', title: '我的评价', icon: 'ChatDotRound' },
    { path: '/user/favorite', title: '我的收藏', icon: 'Star' },
    { path: '/user/message', title: '消息提醒', icon: 'Bell' },
    { path: '/user/announcement', title: '系统公告', icon: 'Document' },
    { path: '/user/profile', title: '个人信息', icon: 'Setting' },
    { path: '/user/password', title: '修改密码', icon: 'Lock' }
  ]
}
