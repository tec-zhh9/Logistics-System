<template>
  <el-dialog
    v-model="visible"
    :title="title"
    :width="width"
    :close-on-click-modal="false"
    @close="onClose"
  >
    <el-form
      ref="formRef"
      :model="model"
      :rules="rules"
      :label-width="labelWidth"
      v-bind="$attrs"
    >
      <slot />
    </el-form>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" :loading="loading" @click="onSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
  modelValue: { type: Boolean, default: false },
  title: { type: String, default: '编辑' },
  width: { type: String, default: '600px' },
  model: { type: Object, default: () => ({}) },
  rules: { type: Object, default: () => ({}) },
  labelWidth: { type: String, default: '80px' },
  loading: { type: Boolean, default: false }
})

const emit = defineEmits(['update:modelValue', 'submit', 'close'])

const formRef = ref()

const visible = computed({
  get: () => props.modelValue,
  set: val => emit('update:modelValue', val)
})

const onSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate((valid) => {
    if (valid) emit('submit')
  })
}

const onClose = () => {
  formRef.value?.resetFields()
  emit('close')
}

defineExpose({ formRef })
</script>
