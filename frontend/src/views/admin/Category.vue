<template>
  <PageCard title="寄件类别管理">
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
      <el-table-column prop="name" label="类别名称" min-width="140" />
      <el-table-column prop="description" label="描述" min-width="220" show-overflow-tooltip />
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

    <FormDialog v-model="dialogVisible" :title="form.id ? '编辑寄件类别' : '新增寄件类别'" :model="form" :rules="rules" @submit="save">
      <el-form-item label="名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入类别名称" />
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入描述" />
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
import { pageCategory, addCategory, updateCategory, deleteCategory } from '../../api'

const list = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ pageNum: 1, pageSize: 10, name: '' })
const dialogVisible = ref(false)
const form = reactive({ id: null, name: '', description: '' })
const rules = { name: [{ required: true, message: '请输入名称', trigger: 'blur' }] }

const load = async () => {
  loading.value = true
  try {
    const res = await pageCategory(query)
    list.value = res.data.list
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const openDialog = row => {
  form.id = row ? row.id : null
  form.name = row ? row.name : ''
  form.description = row ? row.description : ''
  dialogVisible.value = true
}

const save = async () => {
  if (form.id) {
    await updateCategory(form)
  } else {
    await addCategory(form)
  }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  load()
}

const remove = async id => {
  await deleteCategory(id)
  ElMessage.success('删除成功')
  load()
}

onMounted(load)
</script>
