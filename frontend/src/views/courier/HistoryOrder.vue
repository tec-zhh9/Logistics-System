<template>
  <PageCard title="历史接单">
    <SearchBar>
      <template #search>
        <el-input v-model="query.orderNo" placeholder="按订单号搜索" clearable style="width: 200px" @keyup.enter="load" @clear="load" />
        <el-select v-model="query.status" placeholder="按状态筛选" clearable style="width: 150px" @change="load">
          <el-option label="已接单" value="已接单" />
          <el-option label="已送达" value="已送达" />
          <el-option label="已签收" value="已签收" />
        </el-select>
        <el-button type="primary" @click="load">搜索</el-button>
      </template>
    </SearchBar>

    <DataTable :data="list" :loading="loading" :total="total" v-model:page="query.pageNum" v-model:size="query.pageSize" @page-change="load">
      <el-table-column prop="orderNo" label="订单号" width="180" />
      <el-table-column prop="senderName" label="寄件人" width="100" />
      <el-table-column prop="receiverName" label="收件人" width="100" />
      <el-table-column prop="receiverAddress" label="收件地址" min-width="200" show-overflow-tooltip />
      <el-table-column prop="weight" label="重量(kg)" width="90" />
      <el-table-column prop="amount" label="运费(元)" width="90" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusType(row.status)">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="下单时间" width="170" />
      <el-table-column prop="acceptTime" label="接单时间" width="170" />
      <el-table-column label="操作" width="100" fixed="right">
        <template #default="{ row }">
          <el-popconfirm v-if="row.status === '已接单'" title="确定已送达该订单吗？" @confirm="deliver(row.id)">
            <template #reference>
              <el-button size="small" type="success">送达</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </DataTable>
  </PageCard>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import PageCard from '../../components/PageCard.vue'
import SearchBar from '../../components/SearchBar.vue'
import DataTable from '../../components/DataTable.vue'
import { listCourierOrder, deliverOrder } from '../../api'

const list = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ pageNum: 1, pageSize: 10, orderNo: '', status: '' })

const statusType = status => {
  if (status === '已接单') return 'primary'
  if (status === '已送达') return 'success'
  if (status === '已签收') return 'info'
  return ''
}

const load = async () => {
  loading.value = true
  try {
    const res = await listCourierOrder(query)
    list.value = res.data.list || res.data
    total.value = res.data.total || res.data.length
  } finally {
    loading.value = false
  }
}

const deliver = async id => {
  await deliverOrder(id)
  ElMessage.success('已标记送达')
  load()
}

onMounted(load)
</script>
