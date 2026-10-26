<template>
  <PageCard title="待接订单">
    <SearchBar>
      <template #search>
        <el-input v-model="query.orderNo" placeholder="按订单号搜索" clearable style="width: 200px" @keyup.enter="load" @clear="load" />
        <el-button type="primary" @click="load">搜索</el-button>
      </template>
    </SearchBar>

    <DataTable :data="list" :loading="loading" :show-pagination="false">
      <el-table-column prop="orderNo" label="订单号" width="180" />
      <el-table-column prop="senderName" label="寄件人" width="100" />
      <el-table-column prop="senderPhone" label="寄件电话" width="130" />
      <el-table-column prop="senderAddress" label="寄件地址" min-width="180" show-overflow-tooltip />
      <el-table-column prop="receiverName" label="收件人" width="100" />
      <el-table-column prop="receiverPhone" label="收件电话" width="130" />
      <el-table-column prop="receiverAddress" label="收件地址" min-width="180" show-overflow-tooltip />
      <el-table-column prop="weight" label="重量(kg)" width="90" />
      <el-table-column prop="amount" label="运费(元)" width="90" />
      <el-table-column prop="createTime" label="下单时间" width="170" />
      <el-table-column label="操作" width="100" fixed="right">
        <template #default="{ row }">
          <el-popconfirm title="确定接取该订单吗？" @confirm="accept(row.id)">
            <template #reference>
              <el-button size="small" type="primary">接单</el-button>
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
import { listPendingOrder, acceptOrder } from '../../api'

const list = ref([])
const loading = ref(false)
const query = reactive({ orderNo: '' })

const load = async () => {
  loading.value = true
  try {
    const res = await listPendingOrder(query)
    list.value = res.data.list || res.data
  } finally {
    loading.value = false
  }
}

const accept = async id => {
  await acceptOrder(id)
  ElMessage.success('接单成功')
  load()
}

onMounted(load)
</script>
