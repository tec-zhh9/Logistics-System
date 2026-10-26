<template>
  <PageCard title="区域管理">
    <SearchBar>
      <template #search>
        <el-input v-model="query.name" placeholder="按名称搜索" clearable style="width: 200px" @keyup.enter="load" @clear="load" />
        <el-button type="primary" @click="load">搜索</el-button>
      </template>
      <template #actions>
        <el-button type="success" @click="openDialog()">新增</el-button>
      </template>
    </SearchBar>

    <DataTable :data="list" :loading="loading" :total="total" v-model:page="query.pageNum" v-model:size="query.pageSize" @page-change="load">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="name" label="区域名称" min-width="180" />
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="openDialog(row)">编辑</el-button>
          <el-popconfirm title="确定删除吗？" @confirm="remove(row.id)">
            <template #reference><el-button size="small" type="danger">删除</el-button></template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </DataTable>

    <FormDialog v-model="dialogVisible" :title="form.id ? '编辑区域' : '新增区域'" :model="form" :rules="rules" @submit="save">
      <el-form-item label="名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入区域名称" />
      </el-form-item>
    </FormDialog>
  </PageCard>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import PageCard from '../../components/PageCard.vue'
import SearchBar from '../../components/SearchBar.vue'
import DataTable from '../../components/DataTable.vue'
import FormDialog from '../../components/FormDialog.vue'
import { pageRegion, addRegion, updateRegion, deleteRegion } from '../../api'

const list = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ pageNum: 1, pageSize: 10, name: '' })
const dialogVisible = ref(false)
const form = reactive({ id: null, name: '' })
const rules = { name: [{ required: true, message: '请输入名称', trigger: 'blur' }] }

const load = async () => {
  loading.value = true
  try {
    const res = await pageRegion(query)
    list.value = res.data.list
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const openDialog = row => {
  form.id = row ? row.id : null
  form.name = row ? row.name : ''
  dialogVisible.value = true
}

const save = async () => {
  if (form.id) {
    await updateRegion(form)
  } else {
    await addRegion(form)
  }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  load()
}

const remove = async id => {
  await deleteRegion(id)
  ElMessage.success('删除成功')
  load()
}

onMounted(load)
</script>
