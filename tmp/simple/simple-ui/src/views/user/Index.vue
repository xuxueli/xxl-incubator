<template>
  <!-- 用户管理页：数据表格 + 弹窗表单 -->
  <el-card>
    <!-- 工具栏：新增按钮 + 搜索框 -->
    <template #header>
      <div class="card-header">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增用户
        </el-button>
      </div>
    </template>

    <!-- 用户数据表格 -->
    <el-table :data="userList" border stripe style="width: 100%" v-loading="loading">
      <el-table-column prop="username" label="用户名" width="150" />
      <el-table-column prop="email" label="邮箱" width="250" />
      <el-table-column prop="createTime" label="创建时间" width="200" />
      <el-table-column prop="updateTime" label="修改时间" width="200" />
      <el-table-column label="操作" fixed="right" width="200">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <!-- 新增/编辑弹窗 -->
  <el-dialog
    v-model="dialogVisible"
    :title="dialogTitle"
    width="500px"
    @closed="resetForm"
  >
    <el-form ref="formRef" :model="formData" :rules="formRules" label-width="80px">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="formData.username" placeholder="请输入用户名" clearable />
      </el-form-item>
      <el-form-item label="密码" :prop="isEdit ? '' : 'password'">
        <el-input
          v-model="formData.password"
          type="password"
          :placeholder="isEdit ? '留空则不修改' : '请输入密码'"
          show-password
          clearable
        />
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="formData.email" placeholder="请输入邮箱" clearable />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">
        确定
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import userApi from '@/api/user'

/**
 * 用户管理页面
 * 功能：用户列表展示、新增、编辑、删除
 */

// 表格数据与加载状态
const userList = ref([])
const loading = ref(false)

// 弹窗控制
const dialogVisible = ref(false)
const isEdit = ref(false)       // 是否为编辑模式
const submitLoading = ref(false)

// 表单引用
const formRef = ref(null)

/**
 * 表单数据
 */
const formData = reactive({
  id: '',
  username: '',
  password: '',
  email: ''
})

/**
 * 弹窗标题（根据新增/编辑模式切换）
 */
const dialogTitle = ref('')

/**
 * 表单校验规则
 */
const formRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '长度 2-20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度 6-20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入有效的邮箱地址', trigger: 'blur' }
  ]
}

/**
 * 页面挂载时加载数据
 */
onMounted(() => {
  fetchUserList()
})

/**
 * 获取用户列表
 */
async function fetchUserList() {
  loading.value = true
  try {
    userList.value = await userApi.list()
  } catch {
    // 错误提示由响应拦截器处理
  } finally {
    loading.value = false
  }
}

/**
 * 打开新增用户弹窗
 */
function handleAdd() {
  isEdit.value = false
  dialogTitle.value = '新增用户'
  dialogVisible.value = true
}

/**
 * 打开编辑用户弹窗
 * @param {Object} row - 当前行数据
 */
function handleEdit(row) {
  isEdit.value = true
  dialogTitle.value = '编辑用户'
  // 回填表单数据（密码不回填，编辑时留空表示不修改）
  formData.id = row.id
  formData.username = row.username
  formData.password = ''
  formData.email = row.email
  dialogVisible.value = true
}

/**
 * 删除用户
 * @param {Object} row - 当前行数据
 */
async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(`确定要删除用户 "${row.username}" 吗？`, '确认删除', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await userApi.delete(row.id)
    ElMessage.success('删除成功')
    fetchUserList()  // 重新加载列表
  } catch {
    // 用户取消或请求失败，不做处理
  }
}

/**
 * 提交表单（新增或编辑）
 */
async function handleSubmit() {
  // 校验表单
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitLoading.value = true
  try {
    if (isEdit.value) {
      // 编辑模式：调用 PUT 接口
      await userApi.update(formData.id, formData)
      ElMessage.success('更新成功')
    } else {
      // 新增模式：调用 POST 接口
      await userApi.create(formData)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    fetchUserList()  // 重新加载列表
  } catch {
    // 错误提示由响应拦截器处理
  } finally {
    submitLoading.value = false
  }
}

/**
 * 弹窗关闭时重置表单
 */
function resetForm() {
  formData.id = ''
  formData.username = ''
  formData.password = ''
  formData.email = ''
  formRef.value?.resetFields()
}
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
