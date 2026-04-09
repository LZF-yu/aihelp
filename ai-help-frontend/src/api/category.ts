import request from '@/utils/request'
import type { Response, Category, CategoryRequest } from '@/types'

export const getCategoriesApi = () => {
  return request.get<Response<Category[]>>('/categories')
}

export const createCategoryApi = (data: CategoryRequest) => {
  return request.post<Response<Category>>('/categories', data)
}

export const updateCategoryApi = (id: number, data: CategoryRequest) => {
  return request.put<Response<Category>>(`/categories/${id}`, data)
}

export const deleteCategoryApi = (id: number) => {
  return request.delete<Response<null>>(`/categories/${id}`)
}
