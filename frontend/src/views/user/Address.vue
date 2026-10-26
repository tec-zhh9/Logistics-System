<template>
  <PageCard title="我的地址">
    <SearchBar>
      <template #actions>
        <el-button type="success" @click="openDialog()">新增地址</el-button>
      </template>
    </SearchBar>

    <DataTable
      :data="pagedList"
      :loading="loading"
      :total="list.length"
      v-model:page="pageNum"
      v-model:size="pageSize"
    >
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="name" label="联系人" width="120" />
      <el-table-column prop="phone" label="手机号" width="140" />
      <el-table-column prop="address" label="地址" min-width="240" show-overflow-tooltip />
      <el-table-column label="是否默认" width="100" align="center">
        <template #default="{ row }">
          <el-tag v-if="row.isDefault" type="success" size="small">默认</el-tag>
          <el-tag v-else type="info" size="small">普通</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="openDialog(row)">编辑</el-button>
          <el-popconfirm title="确定删除该地址吗？" @confirm="remove(row.id)">
            <template #reference><el-button size="small" type="danger">删除</el-button></template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </DataTable>

    <FormDialog v-model="dialogVisible" :title="form.id ? '编辑地址' : '新增地址'" :model="form" :rules="rules" @submit="save">
      <el-form-item label="联系人" prop="name">
        <el-input v-model="form.name" placeholder="请输入联系人姓名" />
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="form.phone" placeholder="请输入手机号" />
      </el-form-item>
      <el-form-item label="地址" prop="address">
        <el-input v-model="form.address" type="textarea" :rows="2" placeholder="请输入详细地址" />
      </el-form-item>
      <el-form-item label="设为默认">
        <el-switch v-model="form.isDefault" />
      </el-form-item>
    </FormDialog>
  </PageCard>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import PageCard from '../../components/PageCard.vue'
import SearchBar from '../../components/SearchBar.vue'
import DataTable from '../../components/DataTable.vue'
import FormDialog from '../../components/FormDialog.vue'
import { listMyAddress, addAddress, updateAddress, deleteAddress } from '../../api'

const list = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)

const dialogVisible = ref(false)
const form = reactive({ id: null, name: '', phone: '', address: '', isDefault: false })
const rules = {
  name: [{ required: true, message: '请输入联系人姓名', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  address: [{ required: true, message: '请输入详细地址', trigger: 'blur' }]
}

const pagedList = computed(() => {
  const start = (pageNum.value - 1) * pageSize.value
  return list.value.slice(start, start + pageSize.value)
})

const load = async () => {
  loading.value = true
  try {
    const res = await listMyAddress()
    list.value = res.data.list || res.data
  } finally {
    loading.value = false
  }
}

const openDialog = row => {
  form.id = row ? row.id : null
  form.name = row ? row.name : ''
  form.phone = row ? row.phone : ''
  form.address = row ? row.address : ''
  form.isDefault = row ? !!row.isDefault : false
  dialogVisible.value = true
}

const save = async () => {
  if (form.id) {
    await updateAddress({ ...form })
  } else {
    await addAddress({ ...form })
  }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  load()
}

const remove = async id => {
  await deleteAddress(id)
  ElMessage.success('删除成功')
  load()
}

onMounted(load)
</script>
