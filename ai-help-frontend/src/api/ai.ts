import request from '@/utils/request'
import type { Response } from '@/types'

export interface AIResponse {
  content: string
  type: string
}

export const chatApi = (prompt: string) => {
  return request.post<Response<AIResponse>>('/ai/chat', { prompt })
}

export const decomposeTaskApi = (taskTitle: string, taskDescription?: string) => {
  return request.post<Response<AIResponse>>('/ai/decompose', null, {
    params: { taskTitle, taskDescription }
  })
}

export const sortPriorityApi = () => {
  return request.post<Response<AIResponse>>('/ai/priority/sort')
}

export const generateDailyPlanApi = () => {
  return request.post<Response<AIResponse>>('/ai/plan/daily')
}

export const generateWeeklyPlanApi = () => {
  return request.post<Response<AIResponse>>('/ai/plan/weekly')
}

export const generateDailyReportApi = () => {
  return request.post<Response<AIResponse>>('/ai/report/daily')
}

export const generateWeeklyReportApi = () => {
  return request.post<Response<AIResponse>>('/ai/report/weekly')
}
