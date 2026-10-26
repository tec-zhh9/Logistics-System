<template>
  <PageCard title="运费规则管理">
    <SearchBar>
      <template #search>
        <el-select v-model="query.categoryId" placeholder="按寄件类别筛选" clearable style="width: 180px" @change="onSearch" @clear="onSearch">
          <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
        </el-select>
      </template>
      <template #actions>
        <el-button type="success" @click="openDialog()">新增</el-button>
      </template>
    </SearchBar>

    <DataTable :data="list" :loading="loading" :total="total" v-model:page="query.pageNum" v-model:size="query.pageSize" @page-change="applyPage" @size-change="applyPage">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column label="寄件类别" min-width="140">
        <template #default="{ row }">{{ categoryName(row.categoryId) }}</template>
      </el-table-column>
      <el-table-column prop="firstWeight" label="首重(kg)" width="110" />
      <el-table-column prop="firstPrice" label="首重价格(元)" width="120" />
      <el-table-column prop="continuedPrice" label="续重价格(元/kg)" width="140" />
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

    <FormDialog v-model="dialogVisible" :title="form.id ? '编辑运费规则' : '新增运费规则'" :model="form" :rules="rules" @submit="save">
      <el-form-item label="寄件类别" prop="categoryId">
        <el-select v-model="form.categoryId" placeholder="请选择寄件类别" style="width: 100%">
          <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="首重(kg)" prop="firstWeight">
        <el-input-number v-model="form.firstWeight" :min="0" :precision="1" style="width: 100%" />
      </el-form-item>
      <el-form-item label="首重价格" prop="firstPrice">
        <el-input-number v-model="form.firstPrice" :min="0" :precision="2" style="width: 100%" />
      </el-form-item>
      <el-form-item label="续重价格" prop="continuedPrice">
        <el-input-number v-model="form.continuedPrice" :min="0" :precision="2" style="width: 100%" />
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
import { listFreightRule, addFreightRule, updateFreightRule, deleteFreightRule, listCategory } from '../../api'

const all = ref([])
const list = ref([])
const total = ref(0)
const loading = ref(false)
const categories = ref([])
const query = reactive({ pageNum: 1, pageSize: 10, categoryId: null })
const dialogVisible = ref(false)
const form = reactive({ id: null, categoryId: null, firstWeight: 1, firstPrice: null, continuedPrice: null })
const rules = {
  categoryId: [{ required: true, message: '请选择寄件类别', trigger: 'change' }],
  firstWeight: [{ required: true, message: '请输入首重', trigger: 'blur' }],
  firstPrice: [{ required: true, message: '请输入首重价格', trigger: 'blur' }],
  continuedPrice: [{ required: true, message: '请输入续重价格', trigger: 'blur' }]
}

const categoryName = id => {
  const c = categories.value.find(item => item.id === id)
  return c ? c.name : id
}

// listFreightRule 返回全量列表（不分页），前端筛选 + 前端分页
const applyPage = () => {
  const filtered = query.categoryId ? all.value.filter(r => r.categoryId === query.categoryId) : all.value
  total.value = filtered.length
  const start = (query.pageNum - 1) * query.pageSize
  list.value = filtered.slice(start, start + query.pageSize)
}

const onSearch = () => {
  query.pageNum = 1
  applyPage()
}

const load = async () => {
  loading.value = true
  try {
    const res = await listFreightRule()
    all.value = res.data || []
    applyPage()
  } finally {
    loading.value = false
  }
}

const loadCategories = async () => {
  const res = await listCategory()
  categories.value = res.data || []
}

const openDialog = row => {
  form.id = row ? row.id : null
  form.categoryId = row ? row.categoryId : null
  form.firstWeight = row ? row.firstWeight : 1
  form.firstPrice = row ? row.firstPrice : null
  form.continuedPrice = row ? row.continuedPrice : null
  dialogVisible.value = true
}

const save = async () => {
  if (form.id) {
    await updateFreightRule(form)
  } else {
    await addFreightRule(form)
  }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  load()
}

const remove = async id => {
  await deleteFreightRule(id)
  ElMessage.success('删除成功')
  load()
}

onMounted(() => {
  load()
  loadCategories()
})
</script>
