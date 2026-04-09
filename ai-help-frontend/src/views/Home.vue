<template>
  <div class="home-container">
    <div class="home-content">
      <aside class="left-sidebar">
        <div class="filter-section">
          <div class="section-title">
            分类筛选
            <el-button link type="primary" size="small" @click="openCategoryDialog()">
              <el-icon><Setting /></el-icon>
              管理
            </el-button>
          </div>
          <ul class="filter-list">
            <li
              :class="{ active: !queryParams.categoryId }"
              @click="handleCategoryChange(null)"
            >
              <span class="category-dot" style="background: #409eff"></span>
              全部任务
            </li>
            <li
              v-for="cat in categoryStore.categories"
              :key="cat.id"
              :class="{ active: queryParams.categoryId === cat.id }"
              @click="handleCategoryChange(cat.id)"
            >
              <span class="category-dot" :style="{ background: cat.categoryColor }"></span>
              {{ cat.categoryName }}
            </li>
          </ul>
        </div>

        <div class="filter-section">
          <div class="section-title">状态筛选</div>
          <ul class="filter-list">
            <li :class="{ active: queryParams.status === null }" @click="handleStatusChange(null)">
              全部
            </li>
            <li :class="{ active: queryParams.status === 0 }" @click="handleStatusChange(0)">
              未完成
            </li>
            <li :class="{ active: queryParams.status === 1 }" @click="handleStatusChange(1)">
              已完成
            </li>
          </ul>
        </div>

        <div class="filter-section">
          <div class="section-title">优先级</div>
          <ul class="filter-list">
            <li :class="{ active: !queryParams.priority }" @click="handlePriorityChange('')">
              全部
            </li>
            <li :class="{ active: queryParams.priority === 'high' }" @click="handlePriorityChange('high')">
              <span class="priority-tag high">高</span> 高优先级
            </li>
            <li :class="{ active: queryParams.priority === 'middle' }" @click="handlePriorityChange('middle')">
              <span class="priority-tag middle">中</span> 中优先级
            </li>
            <li :class="{ active: queryParams.priority === 'low' }" @click="handlePriorityChange('low')">
              <span class="priority-tag low">低</span> 低优先级
            </li>
          </ul>
        </div>

        <div class="ai-actions">
          <el-button type="primary" @click="showAIDialog('dailyPlan')">
            <el-icon><MagicStick /></el-icon>
            今日计划
          </el-button>
          <el-button type="success" @click="showAIDialog('weeklyPlan')">
            <el-icon><MagicStick /></el-icon>
            周计划
          </el-button>
          <el-button type="warning" @click="showAIDialog('report')">
            <el-icon><Document /></el-icon>
            生成总结
          </el-button>
        </div>
      </aside>

      <main class="center-content">
        <div class="task-header">
          <div class="search-box">
            <el-input
              v-model="queryParams.keyword"
              placeholder="搜索任务..."
              :prefix-icon="Search"
              @input="handleSearch"
              clearable
            />
          </div>
          <el-button type="primary" @click="handleAddTask">
            <el-icon><Plus /></el-icon>
            添加任务
          </el-button>
        </div>

        <div class="task-list">
          <el-table
            v-loading="taskStore.loading"
            :data="taskStore.tasks"
            style="width: 100%"
            @row-click="handleRowClick"
          >
            <el-table-column width="50">
              <template #default="{ row }">
                <el-checkbox
                  :model-value="row.status === 1"
                  @change="handleStatusChangeRow(row)"
                />
              </template>
            </el-table-column>
            <el-table-column prop="title" label="任务标题" min-width="200">
              <template #default="{ row }">
                <span :class="{ completed: row.status === 1 }">{{ row.title }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="categoryName" label="分类" width="120">
              <template #default="{ row }">
                <el-tag
                  v-if="row.categoryName"
                  size="small"
                  :style="{ background: row.categoryColor + '20', border: 'none', color: row.categoryColor }"
                >
                  {{ row.categoryName }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="priority" label="优先级" width="100">
              <template #default="{ row }">
                <el-tag
                  size="small"
                  :type="getPriorityType(row.priority)"
                >
                  {{ getPriorityLabel(row.priority) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="deadline" label="截止时间" width="160">
              <template #default="{ row }">
                <span v-if="row.deadline" :class="{ overdue: isOverdue(row) }">
                  {{ formatDate(row.deadline) }}
                </span>
                <span v-else class="no-deadline">未设置</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120" fixed="right">
              <template #default="{ row }">
                <el-button link type="primary" @click.stop="handleEditTask(row)">编辑</el-button>
                <el-button link type="danger" @click.stop="handleDeleteTask(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <div class="pagination">
            <el-pagination
              v-model:current-page="queryParams.current"
              v-model:page-size="queryParams.size"
              :total="taskStore.total"
              :page-sizes="[10, 20, 50]"
              :pager-count="5"
              layout="slot, prev, pager, next"
              @size-change="handlePageChange"
              @current-change="handlePageChange"
            >
              <template #default>
                <span class="pagination-info">共 {{ taskStore.total }} 条</span>
                <el-select v-model="queryParams.size" size="small" style="width: 100px; margin-left: 8px;" @change="handlePageChange">
                  <el-option :value="10" label="10条/页" />
                  <el-option :value="20" label="20条/页" />
                  <el-option :value="50" label="50条/页" />
                </el-select>
              </template>
            </el-pagination>
          </div>
        </div>
      </main>

      <aside class="right-sidebar">
        <div class="dashboard-card">
          <div class="card-title">数据概览</div>
          <div class="stats-grid">
            <div class="stat-item">
              <div class="stat-value">{{ dashboardData.todayTotal }}</div>
              <div class="stat-label">今日新增</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ dashboardData.todayCompleted }}</div>
              <div class="stat-label">今日完成</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ dashboardData.pendingTotal }}</div>
              <div class="stat-label">待办总数</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ dashboardData.completedTotal }}</div>
              <div class="stat-label">已完成</div>
            </div>
          </div>
        </div>

        <div class="dashboard-card">
          <div class="card-title">今日重点</div>
          <div class="priority-list" v-if="dashboardData.todayPriorityTasks?.length">
            <div
              v-for="task in dashboardData.todayPriorityTasks"
              :key="task.id"
              class="priority-item"
              @click="handleEditTask(task)"
            >
              <span class="priority-dot" :class="task.priority"></span>
              <span class="priority-title">{{ task.title }}</span>
            </div>
          </div>
          <el-empty v-else description="暂无重点任务" :image-size="60" />
        </div>

        <div class="dashboard-card">
          <div class="card-title">AI功能</div>
          <div class="ai-quick-actions">
            <el-button size="small" @click="showAIDialog('decompose')">
              <el-icon><Connection /></el-icon>
              智能拆解
            </el-button>
            <el-button size="small" @click="showAIDialog('prioritySort')">
              <el-icon><Sort /></el-icon>
              优先级排序
            </el-button>
          </div>
        </div>
      </aside>
    </div>

    <!-- 任务添加/编辑弹窗 -->
    <el-dialog
      v-model="taskDialogVisible"
      :title="taskForm.id ? '编辑任务' : '添加任务'"
      width="600px"
      @close="resetTaskForm"
    >
      <el-form ref="taskFormRef" :model="taskForm" :rules="taskRules" label-width="80px">
        <el-form-item label="任务标题" prop="title">
          <el-input v-model="taskForm.title" placeholder="请输入任务标题" />
        </el-form-item>
        <el-form-item label="任务描述" prop="description">
          <el-input
            v-model="taskForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入任务描述"
          />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="taskForm.categoryId" placeholder="请选择分类" clearable>
            <el-option
              v-for="cat in categoryStore.categories"
              :key="cat.id"
              :label="cat.categoryName"
              :value="cat.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级">
          <el-radio-group v-model="taskForm.priority">
            <el-radio label="high">高</el-radio>
            <el-radio label="middle">中</el-radio>
            <el-radio label="low">低</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="截止时间">
          <el-date-picker
            v-model="taskForm.deadline"
            type="datetime"
            placeholder="选择截止时间"
            format="YYYY-MM-DD HH:mm"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="提醒时间">
          <el-date-picker
            v-model="taskForm.reminderTime"
            type="datetime"
            placeholder="选择提醒时间"
            format="YYYY-MM-DD HH:mm"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item v-if="!taskForm.id" label="AI拆解">
          <el-button @click="handleAIDecompose" :loading="aiLoading">
            <el-icon><MagicStick /></el-icon>
            AI智能拆解
          </el-button>
          <span class="ai-tip">让AI帮你把复杂任务拆成小步骤</span>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="taskDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveTask">确定</el-button>
      </template>
    </el-dialog>

    <!-- AI功能弹窗 -->
    <el-dialog
      v-model="aiDialogVisible"
      :title="aiDialogTitle"
      width="700px"
    >
      <div v-if="aiLoading" class="ai-loading">
        <el-icon class="is-loading"><Loading /></el-icon>
        <span>AI正在思考中，请稍候...</span>
      </div>
      <div v-else-if="aiResult" class="ai-result" v-html="formatAIResult(aiResult)"></div>
      <el-empty v-else description="点击按钮生成内容" />
      <template #footer>
        <el-button @click="aiDialogVisible = false">关闭</el-button>
        <el-button v-if="aiResult" type="primary" @click="copyAIResult">复制结果</el-button>
      </template>
    </el-dialog>

    <!-- 分类管理弹窗 -->
    <el-dialog
      v-model="categoryDialogVisible"
      title="分类管理"
      width="600px"
    >
      <div class="category-manage">
        <div class="category-add">
          <el-form :inline="true" :model="categoryForm" class="category-form">
            <el-form-item label="分类名称">
              <el-input
                v-model="categoryForm.categoryName"
                placeholder="请输入分类名称"
                style="width: 160px"
              />
            </el-form-item>
            <el-form-item label="颜色">
              <el-color-picker v-model="categoryForm.categoryColor" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleAddCategory">
                添加分类
              </el-button>
            </el-form-item>
          </el-form>
        </div>

        <el-table :data="categoryStore.categories" size="small" class="category-table">
          <el-table-column prop="categoryName" label="分类名称" min-width="120">
            <template #default="{ row }">
              <span v-if="editingCategoryId !== row.id">
                <span class="category-dot" :style="{ background: row.categoryColor }"></span>
                {{ row.categoryName }}
              </span>
              <el-input
                v-else
                v-model="categoryEditForm.categoryName"
                size="small"
                style="width: 120px"
              />
            </template>
          </el-table-column>
          <el-table-column label="颜色" width="80">
            <template #default="{ row }">
              <span v-if="editingCategoryId !== row.id">
                <el-color-picker
                  v-model="row.categoryColor"
                  size="small"
                  @change="handleUpdateCategory(row)"
                />
              </span>
              <el-color-picker
                v-else
                v-model="categoryEditForm.categoryColor"
                size="small"
              />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" fixed="right">
            <template #default="{ row }">
              <template v-if="editingCategoryId !== row.id">
                <el-button link type="primary" size="small" @click="startEditCategory(row)">
                  编辑
                </el-button>
                <el-button link type="danger" size="small" @click="handleDeleteCategory(row.id)">
                  删除
                </el-button>
              </template>
              <template v-else>
                <el-button link type="primary" size="small" @click="confirmEditCategory(row)">
                  保存
                </el-button>
                <el-button link size="small" @click="editingCategoryId = null">
                  取消
                </el-button>
              </template>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Search, Plus, MagicStick, Document, Connection, Sort, Loading, Setting
} from '@element-plus/icons-vue'
import { useTaskStore } from '@/stores/task'
import { useCategoryStore } from '@/stores/category'
import { getDashboardApi } from '@/api/dashboard'
import {
  generateDailyPlanApi,
  generateWeeklyPlanApi,
  generateDailyReportApi,
  generateWeeklyReportApi,
  decomposeTaskApi,
  sortPriorityApi
} from '@/api/ai'
import type { Task } from '@/types'
import type { FormInstance, FormRules } from 'element-plus'
import dayjs from 'dayjs'

const taskStore = useTaskStore()
const categoryStore = useCategoryStore()
const taskFormRef = ref<FormInstance>()

const queryParams = reactive({
  categoryId: null as number | null,
  status: null as number | null,
  priority: '',
  keyword: '',
  current: 1,
  size: 10
})

const taskDialogVisible = ref(false)
const taskForm = reactive({
  id: null as number | null,
  title: '',
  description: '',
  categoryId: null as number | null,
  priority: 'middle',
  deadline: '',
  reminderTime: ''
})

const taskRules: FormRules = {
  title: [{ required: true, message: '请输入任务标题', trigger: 'blur' }]
}

const dashboardData = ref({
  todayTotal: 0,
  todayCompleted: 0,
  pendingTotal: 0,
  completedTotal: 0,
  todayPriorityTasks: [] as Task[]
})

const aiDialogVisible = ref(false)
const aiDialogTitle = ref('')
const aiLoading = ref(false)
const aiResult = ref('')
const aiDialogType = ref('')

const categoryDialogVisible = ref(false)
const categoryForm = reactive({
  categoryName: '',
  categoryColor: '#409EFF'
})
const editingCategoryId = ref<number | null>(null)
const categoryEditForm = reactive({
  categoryName: '',
  categoryColor: '#409EFF'
})

onMounted(async () => {
  await Promise.all([
    categoryStore.fetchCategories(),
    fetchTasks(),
    fetchDashboard()
  ])
})

const fetchTasks = async () => {
  await taskStore.fetchTasks(queryParams)
}

const fetchDashboard = async () => {
  try {
    const res = await getDashboardApi()
    dashboardData.value = res.data
  } catch {
    // ignore
  }
}

const handleCategoryChange = (categoryId: number | null) => {
  queryParams.categoryId = categoryId
  queryParams.current = 1
  fetchTasks()
}

const handleStatusChange = (status: number | null) => {
  queryParams.status = status
  queryParams.current = 1
  fetchTasks()
}

const handlePriorityChange = (priority: string) => {
  queryParams.priority = priority
  queryParams.current = 1
  fetchTasks()
}

let searchTimer: number | null = null
const handleSearch = () => {
  if (searchTimer) clearTimeout(searchTimer)
  searchTimer = window.setTimeout(() => {
    queryParams.current = 1
    fetchTasks()
  }, 500)
}

const handlePageChange = () => {
  fetchTasks()
}

const handleAddTask = () => {
  taskForm.id = null
  taskDialogVisible.value = true
}

const handleEditTask = (row: Task) => {
  Object.assign(taskForm, {
    id: row.id,
    title: row.title,
    description: row.description || '',
    categoryId: row.categoryId || null,
    priority: row.priority || 'middle',
    deadline: row.deadline || '',
    reminderTime: row.reminderTime || ''
  })
  taskDialogVisible.value = true
}

const handleDeleteTask = async (row: Task) => {
  await ElMessageBox.confirm('确定要删除该任务吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  await taskStore.deleteTask(row.id)
  ElMessage.success('删除成功')
  fetchTasks()
  fetchDashboard()
}

const handleStatusChangeRow = async (row: Task) => {
  const newStatus = row.status === 1 ? 0 : 1
  await taskStore.updateTaskStatus(row.id, newStatus)
  ElMessage.success(newStatus === 1 ? '任务已完成' : '任务已恢复')
  fetchTasks()
  fetchDashboard()
}

const handleRowClick = (row: Task) => {
  handleEditTask(row)
}

const handleSaveTask = async () => {
  if (!taskFormRef.value) return
  await taskFormRef.value.validate(async (valid) => {
    if (!valid) return

    try {
      if (taskForm.id) {
        await taskStore.updateTask(taskForm.id, taskForm)
        ElMessage.success('更新成功')
      } else {
        await taskStore.createTask(taskForm)
        ElMessage.success('创建成功')
      }
      taskDialogVisible.value = false
      resetTaskForm()
      fetchTasks()
      fetchDashboard()
    } catch {
      // error
    }
  })
}

const resetTaskForm = () => {
  taskFormRef.value?.resetFields()
  Object.assign(taskForm, {
    id: null,
    title: '',
    description: '',
    categoryId: null,
    priority: 'middle',
    deadline: '',
    reminderTime: ''
  })
}

const handleAIDecompose = async () => {
  if (!taskForm.title) {
    ElMessage.warning('请先输入任务标题')
    return
  }
  aiLoading.value = true
  aiResult.value = ''
  aiDialogTitle.value = 'AI任务拆解'
  aiDialogType.value = 'decompose'
  aiDialogVisible.value = true
  try {
    const res = await decomposeTaskApi(taskForm.title, taskForm.description)
    aiResult.value = res.data.content
  } catch {
    ElMessage.error('AI拆解失败')
  } finally {
    aiLoading.value = false
  }
}

const showAIDialog = async (type: string) => {
  aiDialogVisible.value = true
  aiLoading.value = true
  aiResult.value = ''

  try {
    switch (type) {
      case 'dailyPlan':
        aiDialogTitle.value = '今日计划'
        const dailyRes = await generateDailyPlanApi()
        aiResult.value = dailyRes.data.content
        break
      case 'weeklyPlan':
        aiDialogTitle.value = '本周计划'
        const weeklyRes = await generateWeeklyPlanApi()
        aiResult.value = weeklyRes.data.content
        break
      case 'report':
        aiDialogTitle.value = '工作总结'
        const dailyReportRes = await generateDailyReportApi()
        aiResult.value = dailyReportRes.data.content
        break
      case 'decompose':
        aiDialogTitle.value = '智能拆解'
        break
      case 'prioritySort':
        aiDialogTitle.value = '优先级排序'
        const sortRes = await sortPriorityApi()
        aiResult.value = sortRes.data.content
        break
    }
  } catch {
    ElMessage.error('生成失败，请重试')
  } finally {
    aiLoading.value = false
  }
}

const formatAIResult = (content: string) => {
  let result = content
    .replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>')
    .replace(/\n/g, '<br>')
  return result
}

const copyAIResult = () => {
  navigator.clipboard.writeText(aiResult.value)
  ElMessage.success('已复制到剪贴板')
}

const getPriorityType = (priority: string) => {
  const map: Record<string, any> = {
    high: 'danger',
    middle: 'warning',
    low: 'info'
  }
  return map[priority] || 'info'
}

const getPriorityLabel = (priority: string) => {
  const map: Record<string, string> = {
    high: '高',
    middle: '中',
    low: '低'
  }
  return map[priority] || priority
}

const formatDate = (date: string) => {
  return dayjs(date).format('YYYY-MM-DD HH:mm')
}

const isOverdue = (row: Task) => {
  if (row.status === 1) return false
  if (!row.deadline) return false
  return dayjs(row.deadline).isBefore(dayjs())
}

const openCategoryDialog = () => {
  categoryDialogVisible.value = true
}

const handleAddCategory = async () => {
  if (!categoryForm.categoryName) {
    ElMessage.warning('请输入分类名称')
    return
  }
  try {
    await categoryStore.createCategory({
      categoryName: categoryForm.categoryName,
      categoryColor: categoryForm.categoryColor
    })
    ElMessage.success('添加成功')
    categoryForm.categoryName = ''
    categoryForm.categoryColor = '#409EFF'
  } catch {
    // error
  }
}

const startEditCategory = (row: any) => {
  editingCategoryId.value = row.id
  categoryEditForm.categoryName = row.categoryName
  categoryEditForm.categoryColor = row.categoryColor
}

const confirmEditCategory = async (row: any) => {
  try {
    await categoryStore.updateCategory(row.id, {
      categoryName: categoryEditForm.categoryName,
      categoryColor: categoryEditForm.categoryColor
    })
    ElMessage.success('更新成功')
    editingCategoryId.value = null
    await categoryStore.fetchCategories()
  } catch {
    // error
  }
}

const handleUpdateCategory = async (row: any) => {
  try {
    await categoryStore.updateCategory(row.id, {
      categoryName: row.categoryName,
      categoryColor: row.categoryColor
    })
    ElMessage.success('颜色已更新')
  } catch {
    // error
  }
}

const handleDeleteCategory = async (id: number) => {
  await ElMessageBox.confirm('删除分类后，该分类下的任务将变为"未分类"，确定删除吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  try {
    await categoryStore.deleteCategory(id)
    ElMessage.success('删除成功')
    if (queryParams.categoryId === id) {
      queryParams.categoryId = null
      fetchTasks()
    }
  } catch {
    // error
  }
}
</script>

<style scoped lang="scss">
.home-container {
  height: 100%;
  background: #f5f7fa;
}

.home-content {
  display: flex;
  height: 100%;
  gap: 0;
}

.left-sidebar {
  width: 220px;
  background: #fff;
  border-right: 1px solid #e4e7ed;
  padding: 16px;
  overflow-y: auto;
  flex-shrink: 0;
}

.filter-section {
  margin-bottom: 20px;

  .section-title {
    font-size: 12px;
    color: #909399;
    margin-bottom: 8px;
    padding-left: 8px;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
}

.filter-list {
  list-style: none;

  li {
    padding: 8px 12px;
    border-radius: 6px;
    cursor: pointer;
    font-size: 14px;
    color: #606266;
    display: flex;
    align-items: center;
    gap: 8px;
    transition: all 0.2s;

    &:hover {
      background: #f5f7fa;
    }

    &.active {
      background: #ecf5ff;
      color: #409eff;
    }
  }
}

.category-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
}

.priority-tag {
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 12px;

  &.high { background: #fef0f0; color: #f56c6c; }
  &.middle { background: #fdf6ec; color: #e6a23c; }
  &.low { background: #f4f4f5; color: #909399; }
}

.ai-actions {
  margin-top: 24px;
  display: flex;
  flex-direction: column;
  gap: 8px;

  .el-button {
    justify-content: flex-start;
  }
}

.center-content {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
  min-width: 0;
}

.task-header {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;

  .search-box {
    flex: 1;
  }
}

.task-list {
  background: #fff;
  border-radius: 8px;
  padding: 16px;
}

.completed {
  text-decoration: line-through;
  color: #909399;
}

.no-deadline {
  color: #c0c4cc;
  font-size: 13px;
}

.overdue {
  color: #f56c6c;
}

.pagination {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}

.right-sidebar {
  width: 260px;
  background: #fff;
  border-left: 1px solid #e4e7ed;
  padding: 16px;
  overflow-y: auto;
  flex-shrink: 0;
}

.dashboard-card {
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;

  &:last-child {
    border-bottom: none;
  }
}

.card-title {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 12px;
}

.stats-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.stat-item {
  text-align: center;
  padding: 12px;
  background: #f5f7fa;
  border-radius: 8px;

  .stat-value {
    font-size: 24px;
    font-weight: 600;
    color: #409eff;
  }

  .stat-label {
    font-size: 12px;
    color: #909399;
    margin-top: 4px;
  }
}

.priority-list {
  .priority-item {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 8px 0;
    cursor: pointer;

    &:hover .priority-title {
      color: #409eff;
    }
  }

  .priority-dot {
    width: 6px;
    height: 6px;
    border-radius: 50%;

    &.high { background: #f56c6c; }
    &.middle { background: #e6a23c; }
    &.low { background: #909399; }
  }

  .priority-title {
    font-size: 13px;
    color: #606266;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

.ai-quick-actions {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.ai-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 40px;
  color: #909399;
}

.ai-result {
  padding: 16px;
  background: #f5f7fa;
  border-radius: 8px;
  line-height: 1.8;
  font-size: 14px;
  color: #303133;
  white-space: pre-wrap;

  :deep(strong) {
    color: #409eff;
    font-weight: 600;
  }
}

.ai-tip {
  margin-left: 12px;
  font-size: 12px;
  color: #909399;
}

.category-manage {
  .category-form {
    margin-bottom: 16px;
    padding-bottom: 16px;
    border-bottom: 1px solid #f0f0f0;
  }

  .category-table {
    margin-top: 8px;
  }
}
</style>
