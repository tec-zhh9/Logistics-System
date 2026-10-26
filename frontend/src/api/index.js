import request from '../utils/request'

// ============ 认证与账户 ============
export const login = data => request.post('/login', data)
export const register = data => request.post('/register', data)
export const getProfile = () => request.get('/profile')
export const updateProfile = data => request.put('/profile', data)
export const changePassword = data => request.put('/password', data)

// ============ 数据统计 ============
export const getOrderAmount = () => request.get('/stats/order-amount')
export const getNetworkStats = () => request.get('/stats/order-network')
export const getCategoryStats = () => request.get('/stats/order-category')
export const getCourierHome = () => request.get('/stats/courier-home')
export const getUserHome = () => request.get('/stats/user-home')

// ============ 公告 ============
export const pageAnnouncement = params => request.get('/announcement/page', { params })
export const listAnnouncement = params => request.get('/announcement/list', { params })
export const addAnnouncement = data => request.post('/announcement', data)
export const updateAnnouncement = data => request.put('/announcement', data)
export const deleteAnnouncement = id => request.delete(`/announcement/${id}`)

// ============ 寄件类别 ============
export const pageCategory = params => request.get('/category/page', { params })
export const listCategory = params => request.get('/category/list', { params })
export const addCategory = data => request.post('/category', data)
export const updateCategory = data => request.put('/category', data)
export const deleteCategory = id => request.delete(`/category/${id}`)

// ============ 区域管理 ============
export const pageRegion = params => request.get('/region/page', { params })
export const listRegion = params => request.get('/region/list', { params })
export const addRegion = data => request.post('/region', data)
export const updateRegion = data => request.put('/region', data)
export const deleteRegion = id => request.delete(`/region/${id}`)

// ============ 网点管理 ============
export const pageNetworkPoint = params => request.get('/network-point/page', { params })
export const listNetworkPoint = params => request.get('/network-point/list', { params })
export const addNetworkPoint = data => request.post('/network-point', data)
export const updateNetworkPoint = data => request.put('/network-point', data)
export const deleteNetworkPoint = id => request.delete(`/network-point/${id}`)

// ============ 物流资讯 ============
export const pageNews = params => request.get('/news/page', { params })
export const getNews = id => request.get(`/news/${id}`)
export const addNews = data => request.post('/news', data)
export const updateNews = data => request.put('/news', data)
export const deleteNews = id => request.delete(`/news/${id}`)

// ============ 评价管理 ============
export const pageEvaluation = params => request.get('/evaluation/page', { params })
export const listMyEvaluation = () => request.get('/evaluation/my')
export const addEvaluation = data => request.post('/evaluation', data)
export const deleteEvaluation = id => request.delete(`/evaluation/${id}`)

// ============ 运费规则 ============
export const listFreightRule = params => request.get('/freight-rule/list', { params })
export const addFreightRule = data => request.post('/freight-rule', data)
export const updateFreightRule = data => request.put('/freight-rule', data)
export const deleteFreightRule = id => request.delete(`/freight-rule/${id}`)

// ============ 提现记录 ============
export const listWithdraw = params => request.get('/withdraw/list', { params })
export const listMyWithdraw = () => request.get('/withdraw/my')
export const addWithdraw = data => request.post('/withdraw', data)
export const auditWithdraw = (id, data) => request.put(`/withdraw/audit/${id}`, data)

// ============ 充值记录 ============
export const listRecharge = params => request.get('/recharge/list', { params })
export const listMyRecharge = () => request.get('/recharge/my')
export const addRecharge = data => request.post('/recharge', data)

// ============ 配送员认证 ============
export const listCourierCert = params => request.get('/courier-cert/list', { params })
export const getMyCert = () => request.get('/courier-cert/my')
export const addCourierCert = data => request.post('/courier-cert', data)
export const auditCourierCert = (id, data) => request.put(`/courier-cert/audit/${id}`, data)
export const deleteCourierCert = id => request.delete(`/courier-cert/${id}`)

// ============ 管理员信息 ============
export const pageAdmin = params => request.get('/admin/page', { params })
export const addAdmin = data => request.post('/admin', data)
export const updateAdmin = data => request.put('/admin', data)
export const resetAdminPassword = id => request.put(`/admin/password/${id}`)
export const deleteAdmin = id => request.delete(`/admin/${id}`)

// ============ 配送员管理 ============
export const pageCourier = params => request.get('/courier/page', { params })
export const addCourier = data => request.post('/courier', data)
export const updateCourier = data => request.put('/courier', data)
export const updateCourierStatus = (id, status) => request.put(`/courier/status/${id}`, null, { params: { status } })
export const resetCourierPassword = id => request.put(`/courier/password/${id}`)
export const deleteCourier = id => request.delete(`/courier/${id}`)

// ============ 用户管理 ============
export const pageUser = params => request.get('/user/page', { params })
export const addUser = data => request.post('/user', data)
export const updateUser = data => request.put('/user', data)
export const updateUserStatus = (id, status) => request.put(`/user/status/${id}`, null, { params: { status } })
export const resetUserPassword = id => request.put(`/user/password/${id}`)
export const deleteUser = id => request.delete(`/user/${id}`)

// ============ 订单管理 ============
export const listOrder = params => request.get('/order/list', { params })
export const listMyOrder = params => request.get('/order/my', { params })
export const listCourierOrder = params => request.get('/order/courier', { params })
export const listPendingOrder = params => request.get('/order/pending', { params })
export const getOrder = id => request.get(`/order/${id}`)
export const addOrder = data => request.post('/order', data)
export const acceptOrder = id => request.put(`/order/accept/${id}`)
export const deliverOrder = id => request.put(`/order/deliver/${id}`)
export const signOrder = id => request.put(`/order/sign/${id}`)
export const deleteOrder = id => request.delete(`/order/${id}`)

// ============ 地址管理 ============
export const listMyAddress = () => request.get('/address/my')
export const addAddress = data => request.post('/address', data)
export const updateAddress = data => request.put('/address', data)
export const deleteAddress = id => request.delete(`/address/${id}`)

// ============ 收藏管理 ============
export const listMyFavorite = () => request.get('/favorite/my')
export const addFavorite = networkPointId => request.post(`/favorite/${networkPointId}`)
export const deleteFavorite = networkPointId => request.delete(`/favorite/${networkPointId}`)

// ============ 消息提醒 ============
export const listMyMessage = () => request.get('/message/my')
export const readMessage = id => request.put(`/message/read/${id}`)
export const readAllMessage = () => request.put('/message/read-all')
export const deleteMessage = id => request.delete(`/message/${id}`)
