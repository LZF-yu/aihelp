export interface Task {
  id: number
  userId: number
  categoryId?: number
  categoryName?: string
  categoryColor?: string
  parentId?: number
  title: string
  description?: string
  priority: 'high' | 'middle' | 'low'
  status: 0 | 1
  deadline?: string
  reminderTime?: string
  completedTime?: string
  tags?: string
  createTime: string
  updateTime: string
}

export interface TaskRequest {
  id?: number
  title: string
  description?: string
  categoryId?: number
  parentId?: number
  priority?: string
  status?: number
  deadline?: string
  reminderTime?: string
  tags?: string
}

export interface TaskQueryRequest {
  categoryId?: number
  status?: number
  priority?: string
  keyword?: string
  parentId?: number
  current?: number
  size?: number
}

export interface DashboardData {
  todayTotal: number
  todayCompleted: number
  pendingTotal: number
  completedTotal: number
  statusStats: Record<string, number>
  categoryStats: Record<string, number>
  todayPriorityTasks: Task[]
}
