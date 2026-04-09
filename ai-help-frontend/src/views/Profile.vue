<template>
  <div class="profile-container">
    <div class="profile-content">
      <el-tabs v-model="activeTab" class="profile-tabs">
        <el-tab-pane label="个人信息" name="info">
          <div class="tab-content">
            <el-form
              ref="infoFormRef"
              :model="userForm"
              :rules="infoRules"
              label-width="80px"
            >
              <el-form-item label="用户名">
                <el-input v-model="userForm.username" disabled />
              </el-form-item>
              <el-form-item label="昵称" prop="nickname">
                <el-input v-model="userForm.nickname" placeholder="请输入昵称" />
              </el-form-item>
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="userForm.email" placeholder="请输入邮箱" />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleUpdateInfo" :loading="infoLoading">
                  保存修改
                </el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-tab-pane>

        <el-tab-pane label="修改密码" name="password">
          <div class="tab-content">
            <el-form
              ref="passwordFormRef"
              :model="passwordForm"
              :rules="passwordRules"
              label-width="100px"
            >
              <el-form-item label="旧密码" prop="oldPassword">
                <el-input
                  v-model="passwordForm.oldPassword"
                  type="password"
                  placeholder="请输入旧密码"
                  show-password
                />
              </el-form-item>
              <el-form-item label="新密码" prop="newPassword">
                <el-input
                  v-model="passwordForm.newPassword"
                  type="password"
                  placeholder="请输入新密码（6-20个字符）"
                  show-password
                />
              </el-form-item>
              <el-form-item label="确认密码" prop="confirmPassword">
                <el-input
                  v-model="passwordForm.confirmPassword"
                  type="password"
                  placeholder="请再次输入新密码"
                  show-password
                />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleUpdatePassword" :loading="passwordLoading">
                  修改密码
                </el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { updateUserInfoApi, updatePasswordApi } from '@/api/user'

const userStore = useUserStore()
const activeTab = ref('info')
const infoFormRef = ref<FormInstance>()
const passwordFormRef = ref<FormInstance>()
const infoLoading = ref(false)
const passwordLoading = ref(false)

const userForm = reactive({
  username: '',
  nickname: '',
  email: ''
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const validateConfirmPassword = (rule: any, value: any, callback: any) => {
  if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const infoRules: FormRules = {
  nickname: [
    { max: 50, message: '昵称长度不能超过50个字符', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
}

const passwordRules: FormRules = {
  oldPassword: [
    { required: true, message: '请输入旧密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '新密码长度在6-20个字符之间', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

onMounted(() => {
  if (userStore.userInfo) {
    userForm.username = userStore.userInfo.username || ''
    userForm.nickname = userStore.userInfo.nickname || ''
    userForm.email = userStore.userInfo.email || ''
  }
})

const handleUpdateInfo = async () => {
  if (!infoFormRef.value) return
  await infoFormRef.value.validate(async (valid) => {
    if (!valid) return

    infoLoading.value = true
    try {
      await updateUserInfoApi({
        nickname: userForm.nickname,
        email: userForm.email
      })
      await userStore.fetchUserInfo()
      ElMessage.success('保存成功')
    } catch {
      // error
    } finally {
      infoLoading.value = false
    }
  })
}

const handleUpdatePassword = async () => {
  if (!passwordFormRef.value) return
  await passwordFormRef.value.validate(async (valid) => {
    if (!valid) return

    passwordLoading.value = true
    try {
      await updatePasswordApi({
        oldPassword: passwordForm.oldPassword,
        newPassword: passwordForm.newPassword
      })
      ElMessage.success('密码修改成功')
      passwordFormRef.value?.resetFields()
    } catch {
      // error
    } finally {
      passwordLoading.value = false
    }
  })
}
</script>

<style scoped lang="scss">
.profile-container {
  height: 100%;
  padding: 24px;
  overflow-y: auto;
}

.profile-content {
  max-width: 600px;
  margin: 0 auto;
  background: #fff;
  border-radius: 8px;
  padding: 24px;
}

.profile-tabs {
  :deep(.el-tabs__header) {
    margin-bottom: 24px;
  }
}

.tab-content {
  max-width: 400px;
}
</style>
