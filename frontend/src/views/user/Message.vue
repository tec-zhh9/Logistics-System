<template>
  <PageCard title="消息提醒">
    <SearchBar>
      <template #search>
        <el-radio-group v-model="readFilter" @change="load">
          <el-radio-button value="">全部</el-radio-button>
          <el-radio-button value="0">未读</el-radio-button>
          <el-radio-button value="1">已读</el-radio-button>
        </el-radio-group>
      </template>
      <template #actions>
        <el-button type="primary" @click="readAll">全部标记已读</el-button>
      </template>
    </SearchBar>

    <DataTable
      :data="pagedList"
      :loading="loading"
      :total="filteredList.length"
      v-model:page="pageNum"
      v-model:size="pageSize"
      :row-class-name="rowClassName"
    >
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="title" label="标题" min-width="160" show-overflow-tooltip />
      <el-table-column prop="content" label="内容" min-width="240" show-overflow-tooltip />
      <el-table-column label="是否已读" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="row.isRead === 1 ? 'info' : 'danger'" size="small">
            {{ row.isRead === 1 ? '已读' : '未读' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="时间" width="180" />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button v-if="row.isRead !== 1" size="small" type="primary" @click="read(row.id)">标记已读</el-button>
          <el-popconfirm title="确定删除该消息吗？" @confirm="remove(row.id)">
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
import SearchBar from '../../components/SearchBar.vue'
import DataTable from '../../components/DataTable.vue'
import { listMyMessage, readMessage, readAllMessage, deleteMessage } from '../../api'

const list = ref([])
const loading = ref(false)
const readFilter = ref('')
const pageNum = ref(1)
const pageSize = ref(10)

const filteredList = computed(() => {
  if (readFilter.value === '') return list.value
  return list.value.filter(item => String(item.isRead) === readFilter.value)
})

const pagedList = computed(() => {
  const start = (pageNum.value - 1) * pageSize.value
  return filteredList.value.slice(start, start + pageSize.value)
})

const load = async () => {
  loading.value = true
  try {
    const res = await listMyMessage()
    list.value = res.data.list || res.data
    pageNum.value = 1
  } finally {
    loading.value = false
  }
}

const rowClassName = ({ row }) => (row.isRead === 1 ? '' : 'unread-row')

const read = async id => {
  await readMessage(id)
  ElMessage.success('已标记为已读')
  load()
}

const readAll = async () => {
  await readAllMessage()
  ElMessage.success('已全部标记为已读')
  load()
}

const remove = async id => {
  await deleteMessage(id)
  ElMessage.success('删除成功')
  load()
}

onMounted(load)
</script>

<style>
.unread-row {
  background-color: #fdf6ec !important;
  font-weight: 600;
}
</style>
