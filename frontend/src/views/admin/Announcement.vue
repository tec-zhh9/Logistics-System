<template>
  <el-card>
    <template #header>
      <div class="toolbar">
        <b>公告管理</b>
        <div>
          <el-input
            v-model="query.title"
            placeholder="按标题搜索"
            style="width: 200px; margin-right: 8px"
            clearable
            @keyup.enter="load"
            @clear="load"
          />
          <el-button type="primary" @click="load">搜索</el-button>
          <el-button type="success" @click="openDialog()">新增公告</el-button>
        </div>
      </div>
    </template>

    <el-table :data="list" border stripe>
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="title" label="标题" min-width="180" />
      <el-table-column prop="createTime" label="发布时间" width="180" />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="openDialog(row)">编辑</el-button>
          <el-popconfirm title="确定删除该公告吗？" @confirm="remove(row.id)">
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="query.pageNum"
      v-model:page-size="query.pageSize"
      :total="total"
      layout="total, prev, pager, next"
      style="margin-top: 12px; justify-content: flex-end"
      @current-change="load"
    />
  </el-card>

  <!-- 新增/编辑公告（富文本编辑器） -->
  <el-dialog v-model="dialogVisible" :title="form.id ? '编辑公告' : '新增公告'" width="720px">
    <el-form :model="form" label-width="70px">
      <el-form-item label="标题">
        <el-input v-model="form.title" placeholder="请输入公告标题" />
      </el-form-item>
      <el-form-item label="内容">
        <div class="editor-wrap">
          <Toolbar :editor="editorRef" :default-config="toolbarConfig" style="border-bottom: 1px solid #ddd" />
          <Editor
            v-model="form.content"
            :default-config="editorConfig"
            style="height: 260px; overflow-y: hidden"
            @onCreated="onCreated"
          />
        </div>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="save">保存</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount, shallowRef } from 'vue'
import { ElMessage } from 'element-plus'
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { pageAnnouncement, addAnnouncement, updateAnnouncement, deleteAnnouncement } from '../../api'

const list = ref([])
const total = ref(0)
const query = reactive({ pageNum: 1, pageSize: 10, title: '' })
const dialogVisible = ref(false)
const form = reactive({ id: null, title: '', content: '' })

// wangEditor 富文本
const editorRef = shallowRef()
const toolbarConfig = {}
const editorConfig = { placeholder: '请输入公告内容...' }
const onCreated = editor => (editorRef.value = editor)
onBeforeUnmount(() => editorRef.value && editorRef.value.destroy())

const load = async () => {
  const res = await pageAnnouncement(query)
  list.value = res.data.list
  total.value = res.data.total
}

const openDialog = row => {
  form.id = row ? row.id : null
  form.title = row ? row.title : ''
  form.content = row ? row.content : ''
  dialogVisible.value = true
}

const save = async () => {
  if (!form.title.trim()) {
    ElMessage.warning('请输入公告标题')
    return
  }
  if (form.id) {
    await updateAnnouncement(form)
  } else {
    await addAnnouncement(form)
  }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  load()
}

const remove = async id => {
  await deleteAnnouncement(id)
  ElMessage.success('删除成功')
  load()
}

onMounted(load)
</script>

<style scoped>
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.editor-wrap {
  border: 1px solid #ddd;
  width: 100%;
  z-index: 100;
}
</style>
