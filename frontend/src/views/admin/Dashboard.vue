<template>
  <div>
    <el-row :gutter="16">
      <el-col :span="8">
        <el-card>
          <template #header><b>近一个月寄件订单金额（折线图）</b></template>
          <div ref="lineRef" class="chart"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header><b>不同网点寄件订单数量（柱状图）</b></template>
          <div ref="barRef" class="chart"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header><b>不同类别寄件数量占比（饼图）</b></template>
          <div ref="pieRef" class="chart"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'
import { getOrderAmount, getNetworkStats, getCategoryStats } from '../../api'

const lineRef = ref()
const barRef = ref()
const pieRef = ref()
let charts = []

// 统计接口返回 [{name, value}]，拆成 x/y 两组
const toXY = list => ({
  x: (list || []).map(i => i.name),
  y: (list || []).map(i => Number(i.value))
})

onMounted(async () => {
  const [amountRes, networkRes, categoryRes] = await Promise.all([
    getOrderAmount(),
    getNetworkStats(),
    getCategoryStats()
  ])

  // 折线图：近一个月寄件订单金额
  const amount = toXY(amountRes.data)
  charts.push(
    echarts.init(lineRef.value).setOption({
      tooltip: { trigger: 'axis' },
      grid: { left: 50, right: 20, bottom: 50, top: 20 },
      xAxis: { type: 'category', data: amount.x, axisLabel: { rotate: 45, fontSize: 10 } },
      yAxis: { type: 'value' },
      series: [{ type: 'line', data: amount.y, smooth: true, areaStyle: {}, itemStyle: { color: '#409eff' } }]
    })
  )

  // 柱状图：不同网点寄件订单数量
  const network = toXY(networkRes.data)
  charts.push(
    echarts.init(barRef.value).setOption({
      tooltip: { trigger: 'axis' },
      grid: { left: 50, right: 20, bottom: 50, top: 20 },
      xAxis: { type: 'category', data: network.x, axisLabel: { rotate: 30, fontSize: 10 } },
      yAxis: { type: 'value' },
      series: [{ type: 'bar', data: network.y, itemStyle: { color: '#67c23a' }, barMaxWidth: 40 }]
    })
  )

  // 饼图：不同类别寄件数量占比
  charts.push(
    echarts.init(pieRef.value).setOption({
      tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
      legend: { bottom: 0, type: 'scroll' },
      series: [
        {
          type: 'pie',
          radius: ['35%', '60%'],
          center: ['50%', '45%'],
          data: categoryRes.data || [],
          label: { formatter: '{b}\n{d}%' }
        }
      ]
    })
  )

  const resize = () => charts.forEach(c => c.resize())
  window.addEventListener('resize', resize)
})

onBeforeUnmount(() => {
  charts.forEach(c => c.dispose())
  charts = []
})
</script>

<style scoped>
.chart {
  height: 320px;
}
</style>
