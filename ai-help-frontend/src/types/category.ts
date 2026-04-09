export interface Category {
  id: number
  userId: number
  categoryName: string
  categoryColor: string
  sortOrder: number
  createTime: string
}

export interface CategoryRequest {
  categoryName: string
  categoryColor?: string
  sortOrder?: number
}
