<template>
  <PageCard title="用户管理">
    <SearchBar>
      <template #search>
        <el-input
          v-model="query.username"
          placeholder="按用户名搜索"
          style="width: 160px"
          clearable
          @keyup.enter="onSearch"
          @clear="onSearch"
        />
        <el-input
          v-model="query.name"
          placeholder="按姓名搜索"
          style="width: 160px"
          clearable
          @keyup.enter="onSearch"
          @clear="onSearch"
        />
        <el-button type="primary" @click="onSearch">搜索</el-button>
      </template>
      <template #actions>
        <el-button type="success" @click="openDialog()">新增用户</el-button>
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
      <el-table-column prop="name" label="姓名" min-width="100" />
      <el-table-column prop="gender" label="性别" width="70" />
      <el-table-column prop="phone" label="手机号" min-width="130" />
      <el-table-column prop="balance" label="余额" width="100" />
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === '正常' ? 'success' : 'danger'">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="注册时间" width="170" />
      <el-table-column label="操作" width="320" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="openDialog(row)">编辑</el-button>
          <el-popconfirm
            :title="row.status === '正常' ? '确定禁用该用户吗？' : '确定启用该用户吗？'"
            @confirm="toggleStatus(row)"
          >
            <template #reference>
              <el-button size="small" :type="row.status === '正常' ? 'warning' : 'success'">
                {{ row.status === '正常' ? '禁用' : '启用' }}
              </el-button>
            </template>
          </el-popconfirm>
          <el-popconfirm title="确定将该用户密码重置为 123456 吗？" @confirm="resetPwd(row.id)">
            <template #reference>
              <el-button size="small" type="info">重置密码</el-button>
            </template>
          </el-popconfirm>
          <el-popconfirm title="确定删除该用户吗？" @confirm="remove(row.id)">
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </DataTable>

    <FormDialog
      v-model="dialogVisible"
      :title="form.id ? '编辑用户' : '新增用户'"
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
      <el-form-item label="性别" prop="gender">
        <el-select v-model="form.gender" placeholder="请选择性别" style="width: 100%">
          <el-option label="男" value="男" />
          <el-option label="女" value="女" />
        </el-select>
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="form.phone" placeholder="请输入手机号" />
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
import { pageUser, addUser, updateUser, updateUserStatus, resetUserPassword, deleteUser } from '../../api'

const list = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ pageNum: 1, pageSize: 10, username: '', name: '' })

const dialogVisible = ref(false)
const form = reactive({ id: null, username: '', name: '', gender: '', phone: '' })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }]
}

const load = async () => {
  loading.value = true
  try {
    const res = await pageUser(query)
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
  form.gender = row ? row.gender : ''
  form.phone = row ? row.phone : ''
  dialogVisible.value = true
}

const save = async () => {
  if (form.id) {
    await updateUser(form)
  } else {
    await addUser(form)
  }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  load()
}

const toggleStatus = async row => {
  const next = row.status === '正常' ? '禁用' : '正常'
  await updateUserStatus(row.id, next)
  ElMessage.success(next + '成功')
  load()
}

const resetPwd = async id => {
  await resetUserPassword(id)
  ElMessage.success('密码已重置为 123456')
}

const remove = async id => {
  await deleteUser(id)
  ElMessage.success('删除成功')
  load()
}

onMounted(load)
</script>
