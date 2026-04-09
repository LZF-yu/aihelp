import request from '@/utils/request'
import type { Response, LoginRequest, RegisterRequest, UserInfo, LoginResponse } from '@/types'

export const loginApi = (data: LoginRequest) => {
  return request.post<Response<LoginResponse>>('/auth/login', data)
}

export const registerApi = (data: RegisterRequest) => {
  return request.post<Response<LoginResponse>>('/auth/register', data)
}

export const getUserInfoApi = () => {
  return request.get<Response<UserInfo>>('/auth/me')
}

export const updateUserInfoApi = (data: any) => {
  return request.put<Response<null>>('/auth/me', data)
}

export const updatePasswordApi = (data: any) => {
  return request.put<Response<null>>('/auth/password', data)
}
