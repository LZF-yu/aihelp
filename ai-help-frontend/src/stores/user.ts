import { defineStore } from 'pinia'
import { ref } from 'vue'
import { loginApi, registerApi, getUserInfoApi } from '@/api/user'
import type { LoginRequest, RegisterRequest, UserInfo } from '@/types/api'

export const useUserStore = defineStore('user', () => {
  const token = ref<string>(localStorage.getItem('token') || '')
  const userInfo = ref<UserInfo | null>(null)

  const setToken = (newToken: string) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  const removeToken = () => {
    token.value = ''
    localStorage.removeItem('token')
    userInfo.value = null
  }

  const login = async (data: LoginRequest) => {
    const res = await loginApi(data)
    setToken(res.data.token)
    await fetchUserInfo()
    return res
  }

  const register = async (data: RegisterRequest) => {
    const res = await registerApi(data)
    setToken(res.data.token)
    return res
  }

  const fetchUserInfo = async () => {
    try {
      const res = await getUserInfoApi()
      userInfo.value = res.data
    } catch {
      removeToken()
    }
  }

  const logout = () => {
    removeToken()
  }

  return {
    token,
    userInfo,
    setToken,
    removeToken,
    login,
    register,
    fetchUserInfo,
    logout
  }
})
