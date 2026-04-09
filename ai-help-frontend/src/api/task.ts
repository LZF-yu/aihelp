import request from '@/utils/request'
import type { Response, PageResult, Task, TaskRequest, TaskQueryRequest } from '@/types'

export const getTasksApi = (params: TaskQueryRequest) => {
  return request.get<Response<PageResult<Task>>>('/tasks', { params })
}

export const getTaskByIdApi = (id: number) => {
  return request.get<Response<Task>>(`/tasks/${id}`)
}

export const createTaskApi = (data: TaskRequest) => {
  return request.post<Response<Task>>('/tasks', data)
}

export const updateTaskApi = (id: number, data: TaskRequest) => {
  return request.put<Response<Task>>(`/tasks/${id}`, data)
}

export const deleteTaskApi = (id: number) => {
  return request.delete<Response<null>>(`/tasks/${id}`)
}

export const updateTaskStatusApi = (id: number, status: number) => {
  return request.patch<Response<null>>(`/tasks/${id}/status`, null, {
    params: { status }
  })
}

export const batchDeleteTasksApi = (ids: string) => {
  return request.delete<Response<null>>('/tasks/batch', {
    params: { ids }
  })
}
