<template>
  <PageCard title="网点管理">
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
      <el-table-column prop="name" label="网点名称" min-width="140" />
      <el-table-column label="所属区域" width="120">
        <template #default="{ row }">{{ regionName(row.regionId) }}</template>
      </el-table-column>
      <el-table-column prop="address" label="地址" min-width="180" show-overflow-tooltip />
      <el-table-column prop="phone" label="电话" width="140" />
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

    <FormDialog v-model="dialogVisible" :title="form.id ? '编辑网点' : '新增网点'" :model="form" :rules="rules" @submit="save">
      <el-form-item label="名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入网点名称" />
      </el-form-item>
      <el-form-item label="所属区域" prop="regionId">
        <el-select v-model="form.regionId" placeholder="请选择所属区域" style="width: 100%">
          <el-option v-for="r in regions" :key="r.id" :label="r.name" :value="r.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="地址" prop="address">
        <el-input v-model="form.address" placeholder="请输入地址" />
      </el-form-item>
      <el-form-item label="电话" prop="phone">
        <el-input v-model="form.phone" placeholder="请输入联系电话" />
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
import { pageNetworkPoint, addNetworkPoint, updateNetworkPoint, deleteNetworkPoint, listRegion } from '../../api'

const list = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ pageNum: 1, pageSize: 10, name: '' })
const dialogVisible = ref(false)
const regions = ref([])
const form = reactive({ id: null, name: '', regionId: null, address: '', phone: '', image: '', description: '' })
const rules = {
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  regionId: [{ required: true, message: '请选择所属区域', trigger: 'change' }]
}

const regionName = id => {
  const r = regions.value.find(item => item.id === id)
  return r ? r.name : '-'
}

const loadRegions = async () => {
  const res = await listRegion()
  regions.value = res.data
}

const load = async () => {
  loading.value = true
  try {
    const res = await pageNetworkPoint(query)
    list.value = res.data.list
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const openDialog = row => {
  form.id = row ? row.id : null
  form.name = row ? row.name : ''
  form.regionId = row ? row.regionId : null
  form.address = row ? row.address : ''
  form.phone = row ? row.phone : ''
  form.image = row ? row.image : ''
  form.description = row ? row.description : ''
  dialogVisible.value = true
}

const save = async () => {
  if (form.id) {
    await updateNetworkPoint(form)
  } else {
    await addNetworkPoint(form)
  }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  load()
}

const remove = async id => {
  await deleteNetworkPoint(id)
  ElMessage.success('删除成功')
  load()
}

onMounted(() => {
  load()
  loadRegions()
})
</script>
