<template>
  <PageCard title="管理员信息管理">
    <SearchBar>
      <template #search>
        <el-input
          v-model="query.username"
          placeholder="按用户名搜索"
          style="width: 200px"
          clearable
          @keyup.enter="onSearch"
          @clear="onSearch"
        />
        <el-button type="primary" @click="onSearch">搜索</el-button>
      </template>
      <template #actions>
        <el-button type="success" @click="openDialog()">新增管理员</el-button>
      </template>
    </SearchBar>

    <DataTable
      :data="list"
      :loading="loading"
      :total="total"
      v-model:page="query.pageNum"
      v-model:size="query.pageSize"
      @page-change="load"
      @size-change="load"
    >
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="username" label="用户名" min-width="120" />
      <el-table-column prop="name" label="姓名" min-width="110" />
      <el-table-column prop="phone" label="手机号" min-width="130" />
      <el-table-column prop="email" label="邮箱" min-width="180" show-overflow-tooltip />
      <el-table-column prop="createTime" label="创建时间" width="170" />
      <el-table-column label="操作" width="230" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="openDialog(row)">编辑</el-button>
          <el-popconfirm title="确定将该管理员密码重置为 123456 吗？" @confirm="resetPwd(row.id)">
            <template #reference>
              <el-button size="small" type="warning">重置密码</el-button>
            </template>
          </el-popconfirm>
          <el-popconfirm title="确定删除该管理员吗？" @confirm="remove(row.id)">
            <template #reference>
              <el-button size="small" type="danger" :disabled="row.id === currentId">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </DataTable>

    <FormDialog
      v-model="dialogVisible"
      :title="form.id ? '编辑管理员' : '新增管理员'"
      :model="form"
      :rules="rules"
      @submit="save"
    >
      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username" placeholder="请输入用户名" />
      </el-form-item>
      <el-form-item label="姓名" prop="name">
        <el-input v-model="form.name" placeholder="请输入姓名" />
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="form.phone" placeholder="请输入手机号" />
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="form.email" placeholder="请输入邮箱" />
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
import { pageAdmin, addAdmin, updateAdmin, resetAdminPassword, deleteAdmin, getProfile } from '../../api'

const list = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ pageNum: 1, pageSize: 10, username: '' })
const currentId = ref(null)

const dialogVisible = ref(false)
const form = reactive({ id: null, username: '', name: '', phone: '', email: '' })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }]
}

const load = async () => {
  loading.value = true
  try {
    const res = await pageAdmin(query)
    list.value = res.data.list
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const onSearch = () => {
  query.pageNum = 1
  load()
}

const openDialog = row => {
  form.id = row ? row.id : null
  form.username = row ? row.username : ''
  form.name = row ? row.name : ''
  form.phone = row ? row.phone : ''
  form.email = row ? row.email : ''
  dialogVisible.value = true
}

const save = async () => {
  if (form.id) {
    await updateAdmin(form)
  } else {
    await addAdmin(form)
  }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  load()
}

const resetPwd = async id => {
  await resetAdminPassword(id)
  ElMessage.success('密码已重置为 123456')
}

const remove = async id => {
  if (id === currentId.value) {
    ElMessage.warning('不能删除当前登录的管理员')
    return
  }
  await deleteAdmin(id)
  ElMessage.success('删除成功')
  load()
}

onMounted(async () => {
  const res = await getProfile()
  currentId.value = res.data.id
  load()
})
</script>
