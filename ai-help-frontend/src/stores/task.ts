import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getTasksApi, createTaskApi, updateTaskApi, deleteTaskApi, updateTaskStatusApi, batchDeleteTasksApi } from '@/api/task'
import type { TaskQueryRequest, TaskRequest, Task } from '@/types/api'

export const useTaskStore = defineStore('task', () => {
  const tasks = ref<Task[]>([])
  const total = ref(0)
  const loading = ref(false)

  const fetchTasks = async (params: TaskQueryRequest) => {
    loading.value = true
    try {
      const res = await getTasksApi(params)
      tasks.value = res.data.records
      total.value = res.data.total
    } finally {
      loading.value = false
    }
  }

  const createTask = async (data: TaskRequest) => {
    const res = await createTaskApi(data)
    return res
  }

  const updateTask = async (id: number, data: TaskRequest) => {
    const res = await updateTaskApi(id, data)
    return res
  }

  const deleteTask = async (id: number) => {
    const res = await deleteTaskApi(id)
    return res
  }

  const updateTaskStatus = async (id: number, status: number) => {
    const res = await updateTaskStatusApi(id, status)
    return res
  }

  const batchDeleteTasks = async (ids: string) => {
    const res = await batchDeleteTasksApi(ids)
    return res
  }

  return {
    tasks,
    total,
    loading,
    fetchTasks,
    createTask,
    updateTask,
    deleteTask,
    updateTaskStatus,
    batchDeleteTasks
  }
})
