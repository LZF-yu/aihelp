import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getCategoriesApi, createCategoryApi, updateCategoryApi, deleteCategoryApi } from '@/api/category'
import type { CategoryRequest, Category } from '@/types/api'

export const useCategoryStore = defineStore('category', () => {
  const categories = ref<Category[]>([])
  const loading = ref(false)

  const fetchCategories = async () => {
    loading.value = true
    try {
      const res = await getCategoriesApi()
      categories.value = res.data
    } finally {
      loading.value = false
    }
  }

  const createCategory = async (data: CategoryRequest) => {
    const res = await createCategoryApi(data)
    await fetchCategories()
    return res
  }

  const updateCategory = async (id: number, data: CategoryRequest) => {
    const res = await updateCategoryApi(id, data)
    await fetchCategories()
    return res
  }

  const deleteCategory = async (id: number) => {
    const res = await deleteCategoryApi(id)
    await fetchCategories()
    return res
  }

  return {
    categories,
    loading,
    fetchCategories,
    createCategory,
    updateCategory,
    deleteCategory
  }
})
