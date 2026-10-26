<template>
  <PageCard title="历史寄件">
    <SearchBar>
      <template #search>
        <el-input v-model="query.orderNo" placeholder="按订单号搜索" clearable style="width: 200px" @keyup.enter="load" @clear="load" />
        <el-select v-model="query.status" placeholder="按状态筛选" clearable style="width: 140px" @change="load" @clear="load">
          <el-option label="待接单" value="待接单" />
          <el-option label="已接单" value="已接单" />
          <el-option label="已送达" value="已送达" />
          <el-option label="已签收" value="已签收" />
        </el-select>
        <el-button type="primary" @click="load">搜索</el-button>
      </template>
    </SearchBar>

    <el-table :data="list" border stripe v-loading="loading" style="width: 100%">
      <el-table-column prop="orderNo" label="订单号" min-width="150" show-overflow-tooltip />
      <el-table-column label="寄件网点" width="130">
        <template #default="{ row }">{{ networkName(row.networkPointId) }}</template>
      </el-table-column>
      <el-table-column prop="receiverName" label="收件人" width="100" />
      <el-table-column prop="receiverPhone" label="收件电话" width="130" />
      <el-table-column prop="receiverAddress" label="收件地址" min-width="180" show-overflow-tooltip />
      <el-table-column label="重量(kg)" width="90" align="right">
        <template #default="{ row }">{{ row.weight }}</template>
      </el-table-column>
      <el-table-column label="运费(元)" width="90" align="right">
        <template #default="{ row }">{{ row.amount }}</template>
      </el-table-column>
      <el-table-column label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="statusType(row.status)">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="下单时间" width="180" />
      <el-table-column label="操作" width="100" fixed="right" align="center">
        <template #default="{ row }">
          <el-popconfirm v-if="row.status === '已送达'" title="确定签收该订单吗？" @confirm="sign(row.id)">
            <template #reference><el-button size="small" type="success">签收</el-button></template>
          </el-popconfirm>
          <span v-else>-</span>
        </template>
      </el-table-column>
    </el-table>
  </PageCard>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import PageCard from '../../components/PageCard.vue'
import SearchBar from '../../components/SearchBar.vue'
import { listMyOrder, signOrder, listNetworkPoint } from '../../api'

const list = ref([])
const networks = ref([])
const loading = ref(false)
const query = reactive({ orderNo: '', status: '' })

const statusType = status => {
  const map = {
    '待接单': 'info',
    '已接单': 'primary',
    '已送达': 'warning',
    '已签收': 'success'
  }
  return map[status] || 'info'
}

const networkName = id => {
  const n = networks.value.find(item => item.id === id)
  return n ? n.name : '-'
}

const loadNetworks = async () => {
  const res = await listNetworkPoint()
  networks.value = res.data
}

const load = async () => {
  loading.value = true
  try {
    const res = await listMyOrder(query)
    list.value = res.data
  } finally {
    loading.value = false
  }
}

const sign = async id => {
  await signOrder(id)
  ElMessage.success('签收成功')
  load()
}

onMounted(() => {
  load()
  loadNetworks()
})
</script>
