import request from '@/utils/request'
import type { Response, DashboardData } from '@/types'

export const getDashboardApi = () => {
  return request.get<Response<DashboardData>>('/dashboard')
}
