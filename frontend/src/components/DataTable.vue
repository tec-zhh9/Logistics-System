<template>
  <div>
    <el-table
      :data="data"
      border
      stripe
      :loading="loading"
      style="width: 100%"
      v-bind="$attrs"
    >
      <slot />
    </el-table>
    <el-pagination
      v-if="showPagination"
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :total="total"
      :layout="layout"
      :page-sizes="pageSizes"
      style="margin-top: 12px; justify-content: flex-end"
      @current-change="onPageChange"
      @size-change="onSizeChange"
    />
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  data: { type: Array, default: () => [] },
  loading: { type: Boolean, default: false },
  total: { type: Number, default: 0 },
  page: { type: Number, default: 1 },
  size: { type: Number, default: 10 },
  pageSizes: { type: Array, default: () => [10, 20, 50] },
  layout: { type: String, default: 'total, sizes, prev, pager, next' },
  showPagination: { type: Boolean, default: true }
})

const emit = defineEmits(['update:page', 'update:size', 'page-change', 'size-change'])

const currentPage = computed({
  get: () => props.page,
  set: val => emit('update:page', val)
})
const pageSize = computed({
  get: () => props.size,
  set: val => emit('update:size', val)
})

const onPageChange = val => emit('page-change', val)
const onSizeChange = val => emit('size-change', val)
</script>
