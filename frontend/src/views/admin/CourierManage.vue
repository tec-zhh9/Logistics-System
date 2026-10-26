<template>
  <PageCard title="配送员管理">
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
        <el-button type="success" @click="openDialog()">新增配送员</el-button>
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
      <el-table-column prop="username" label="用户名" min-width="110" />
      <el-table-column prop="name" label="姓名" min-width="90" />
      <el-table-column prop="gender" label="性别" width="70" />
      <el-table-column prop="phone" label="手机号" min-width="120" />
      <el-table-column label="负责区域" min-width="100">
        <template #default="{ row }">{{ regionName(row.regionId) }}</template>
      </el-table-column>
      <el-table-column prop="balance" label="余额" width="90" />
      <el-table-column prop="totalIncome" label="总收入" width="90" />
      <el-table-column prop="orderCount" label="接单数" width="80" />
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === '正常' ? 'success' : 'danger'">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="170" />
      <el-table-column label="操作" width="320" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="openDialog(row)">编辑</el-button>
          <el-popconfirm
            :title="row.status === '正常' ? '确定禁用该配送员吗？' : '确定启用该配送员吗？'"
            @confirm="toggleStatus(row)"
          >
            <template #reference>
              <el-button size="small" :type="row.status === '正常' ? 'warning' : 'success'">
                {{ row.status === '正常' ? '禁用' : '启用' }}
              </el-button>
            </template>
          </el-popconfirm>
          <el-popconfirm title="确定将该配送员密码重置为 123456 吗？" @confirm="resetPwd(row.id)">
            <template #reference>
              <el-button size="small" type="info">重置密码</el-button>
            </template>
          </el-popconfirm>
          <el-popconfirm title="确定删除该配送员吗？" @confirm="remove(row.id)">
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </DataTable>

    <FormDialog
      v-model="dialogVisible"
      :title="form.id ? '编辑配送员' : '新增配送员'"
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
      <el-form-item label="负责区域" prop="regionId">
        <el-select v-model="form.regionId" placeholder="请选择负责区域" style="width: 100%">
          <el-option v-for="r in regions" :key="r.id" :label="r.name" :value="r.id" />
        </el-select>
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
import {
  pageCourier,
  addCourier,
  updateCourier,
  updateCourierStatus,
  resetCourierPassword,
  deleteCourier,
  listRegion
} from '../../api'

const list = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ pageNum: 1, pageSize: 10, username: '', name: '' })
const regions = ref([])

const dialogVisible = ref(false)
const form = reactive({ id: null, username: '', name: '', gender: '', phone: '', regionId: null })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }]
}

const regionName = id => regions.value.find(r => r.id === id)?.name || '-'

const load = async () => {
  loading.value = true
  try {
    const res = await pageCourier(query)
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
  form.regionId = row ? row.regionId : null
  dialogVisible.value = true
}

const save = async () => {
  if (form.id) {
    await updateCourier(form)
  } else {
    await addCourier(form)
  }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  load()
}

const toggleStatus = async row => {
  const next = row.status === '正常' ? '禁用' : '正常'
  await updateCourierStatus(row.id, next)
  ElMessage.success(next + '成功')
  load()
}

const resetPwd = async id => {
  await resetCourierPassword(id)
  ElMessage.success('密码已重置为 123456')
}

const remove = async id => {
  await deleteCourier(id)
  ElMessage.success('删除成功')
  load()
}

onMounted(async () => {
  const res = await listRegion()
  regions.value = res.data || []
  load()
})
</script>
