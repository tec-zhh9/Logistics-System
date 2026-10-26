<template>
  <PageCard title="物流资讯管理">
    <SearchBar>
      <template #search>
        <el-input v-model="query.title" placeholder="按标题搜索" clearable style="width: 200px" @keyup.enter="load" @clear="load" />
        <el-button type="primary" @click="load">搜索</el-button>
      </template>
      <template #actions>
        <el-button type="success" @click="openDialog()">新增</el-button>
      </template>
    </SearchBar>

    <DataTable :data="list" :loading="loading" :total="total" v-model:page="query.pageNum" v-model:size="query.pageSize" @page-change="load">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
      <el-table-column prop="createTime" label="发布时间" width="180" />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="openDialog(row)">编辑</el-button>
          <el-popconfirm title="确定删除吗？" @confirm="remove(row.id)">
            <template #reference><el-button size="small" type="danger">删除</el-button></template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </DataTable>

    <FormDialog v-model="dialogVisible" :title="form.id ? '编辑资讯' : '新增资讯'" :model="form" :rules="rules" width="760px" @submit="save">
      <el-form-item label="标题" prop="title">
        <el-input v-model="form.title" placeholder="请输入资讯标题" />
      </el-form-item>
      <el-form-item label="内容" prop="content">
        <div class="editor-wrap">
          <Toolbar :editor="editorRef" :default-config="toolbarConfig" style="border-bottom: 1px solid #ddd" />
          <Editor
            v-model="form.content"
            :default-config="editorConfig"
            style="height: 300px; overflow-y: hidden"
            @onCreated="onCreated"
          />
        </div>
      </el-form-item>
    </FormDialog>
  </PageCard>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount, shallowRef } from 'vue'
import { ElMessage } from 'element-plus'
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import PageCard from '../../components/PageCard.vue'
import SearchBar from '../../components/SearchBar.vue'
import DataTable from '../../components/DataTable.vue'
import FormDialog from '../../components/FormDialog.vue'
import { pageNews, addNews, updateNews, deleteNews } from '../../api'

const list = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ pageNum: 1, pageSize: 10, title: '' })
const dialogVisible = ref(false)
const form = reactive({ id: null, title: '', content: '', image: '' })
const rules = { title: [{ required: true, message: '请输入标题', trigger: 'blur' }] }

// wangEditor 富文本
const editorRef = shallowRef()
const toolbarConfig = {}
const editorConfig = { placeholder: '请输入资讯内容...' }
const onCreated = editor => (editorRef.value = editor)
onBeforeUnmount(() => editorRef.value && editorRef.value.destroy())

const load = async () => {
  loading.value = true
  try {
    const res = await pageNews(query)
    list.value = res.data.list
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const openDialog = row => {
  form.id = row ? row.id : null
  form.title = row ? row.title : ''
  form.content = row ? row.content : ''
  form.image = row ? row.image : ''
  dialogVisible.value = true
}

const save = async () => {
  if (form.id) {
    await updateNews(form)
  } else {
    await addNews(form)
  }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  load()
}

const remove = async id => {
  await deleteNews(id)
  ElMessage.success('删除成功')
  load()
}

onMounted(load)
</script>

<style scoped>
.editor-wrap {
  border: 1px solid #ddd;
  width: 100%;
  z-index: 100;
}
</style>
