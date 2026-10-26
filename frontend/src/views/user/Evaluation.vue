<template>
  <PageCard title="我的评价">
    <DataTable
      :data="pagedList"
      :loading="loading"
      :total="list.length"
      v-model:page="pageNum"
      v-model:size="pageSize"
    >
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="orderId" label="订单号" width="100" />
      <el-table-column label="评分" width="160">
        <template #default="{ row }">
          <el-rate :model-value="row.score" disabled />
        </template>
      </el-table-column>
      <el-table-column prop="content" label="评价内容" min-width="240" show-overflow-tooltip />
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="100" fixed="right">
        <template #default="{ row }">
          <el-popconfirm title="确定删除该评价吗？" @confirm="remove(row.id)">
            <template #reference><el-button size="small" type="danger">删除</el-button></template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </DataTable>
  </PageCard>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import PageCard from '../../components/PageCard.vue'
import DataTable from '../../components/DataTable.vue'
import { listMyEvaluation, deleteEvaluation } from '../../api'

const list = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)

const pagedList = computed(() => {
  const start = (pageNum.value - 1) * pageSize.value
  return list.value.slice(start, start + pageSize.value)
})

const load = async () => {
  loading.value = true
  try {
    const res = await listMyEvaluation()
    list.value = res.data.list || res.data
  } finally {
    loading.value = false
  }
}

const remove = async id => {
  await deleteEvaluation(id)
  ElMessage.success('删除成功')
  load()
}

onMounted(load)
</script>
