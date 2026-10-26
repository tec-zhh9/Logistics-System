<template>
  <PageCard title="系统公告">
    <SearchBar>
      <template #search>
        <el-input v-model="query.title" placeholder="按标题搜索" clearable style="width: 200px" @keyup.enter="load" @clear="load" />
        <el-button type="primary" @click="load">搜索</el-button>
      </template>
    </SearchBar>

    <DataTable :data="list" :loading="loading" :total="total" v-model:page="query.pageNum" v-model:size="query.pageSize" @page-change="load">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="title" label="标题" min-width="220" show-overflow-tooltip />
      <el-table-column prop="createTime" label="发布时间" width="180" />
      <el-table-column label="操作" width="100" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="view(row)">查看</el-button>
        </template>
      </el-table-column>
    </DataTable>

    <el-dialog v-model="viewVisible" :title="current.title" width="640px">
      <div class="announce-time">发布时间：{{ current.createTime }}</div>
      <div class="announce-content" v-html="current.content"></div>
      <template #footer>
        <el-button type="primary" @click="viewVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </PageCard>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import PageCard from '../../components/PageCard.vue'
import SearchBar from '../../components/SearchBar.vue'
import DataTable from '../../components/DataTable.vue'
import { pageAnnouncement } from '../../api'

const list = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ pageNum: 1, pageSize: 10, title: '' })
const viewVisible = ref(false)
const current = ref({})

const load = async () => {
  loading.value = true
  try {
    const res = await pageAnnouncement(query)
    list.value = res.data.list
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const view = row => {
  current.value = row
  viewVisible.value = true
}

onMounted(load)
</script>

<style scoped>
.announce-time {
  margin-bottom: 12px;
  font-size: 12px;
  color: #999;
}
.announce-content {
  line-height: 1.8;
  color: #333;
}
</style>
