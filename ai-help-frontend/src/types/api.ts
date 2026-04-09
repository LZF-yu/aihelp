export interface Response<T = any> {
  code: number
  message: string
  data: T
}

export interface PageResult<T> {
  total: number
  records: T[]
  current: number
  size: number
}

export interface LoginRequest {
  username: string
  password: string
  rememberMe?: boolean
}

export interface RegisterRequest {
  username: string
  password: string
  email?: string
}

export interface UserInfo {
  id: number
  username: string
  email?: string
  nickname?: string
  avatar?: string
  createTime?: string
}

export interface LoginResponse {
  token: string
  username: string
  userId: number
}

export interface UserUpdateRequest {
  nickname?: string
  email?: string
  avatar?: string
}

export interface PasswordUpdateRequest {
  oldPassword: string
  newPassword: string
}
