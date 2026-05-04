<template>
  <!-- 商品管理页：数据表格 + 弹窗表单 -->
  <el-card>
    <!-- 工具栏：新增按钮 -->
    <template #header>
      <div class="card-header">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增商品
        </el-button>
      </div>
    </template>

    <!-- 商品数据表格 -->
    <el-table :data="productList" border stripe style="width: 100%" v-loading="loading">
      <el-table-column prop="name" label="商品名称" width="180" />
      <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
      <el-table-column prop="price" label="价格" width="120">
        <template #default="{ row }">
          ￥{{ Number(row.price).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column prop="stock" label="库存" width="100" />
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
      <el-form-item label="商品名称" prop="name">
        <el-input v-model="formData.name" placeholder="请输入商品名称" clearable />
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input
          v-model="formData.description"
          type="textarea"
          :rows="3"
          placeholder="请输入商品描述"
        />
      </el-form-item>
      <el-form-item label="价格" prop="price">
        <el-input-number
          v-model="formData.price"
          :min="0"
          :precision="2"
          :step="0.01"
          controls-position="right"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="库存" prop="stock">
        <el-input-number
          v-model="formData.stock"
          :min="0"
          :step="1"
          controls-position="right"
          style="width: 100%"
        />
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
import productApi from '@/api/product'

/**
 * 商品管理页面
 * 功能：商品列表展示、新增、编辑、删除
 * 使用 ElInputNumber 处理价格和库存输入
 */

// 表格数据与加载状态
const productList = ref([])
const loading = ref(false)

// 弹窗控制
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)

// 表单引用
const formRef = ref(null)

/**
 * 表单数据
 */
const formData = reactive({
  id: '',
  name: '',
  description: '',
  price: 0,
  stock: 0
})

/**
 * 弹窗标题
 */
const dialogTitle = ref('')

/**
 * 表单校验规则
 */
const formRules = {
  name: [
    { required: true, message: '请输入商品名称', trigger: 'blur' },
    { min: 1, max: 100, message: '长度 1-100 个字符', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入商品描述', trigger: 'blur' }
  ],
  price: [
    { required: true, message: '请输入价格', trigger: 'blur' }
  ],
  stock: [
    { required: true, message: '请输入库存数量', trigger: 'blur' }
  ]
}

/**
 * 页面挂载时加载数据
 */
onMounted(() => {
  fetchProductList()
})

/**
 * 获取商品列表
 */
async function fetchProductList() {
  loading.value = true
  try {
    productList.value = await productApi.list()
  } catch {
    // 错误提示由响应拦截器处理
  } finally {
    loading.value = false
  }
}

/**
 * 打开新增商品弹窗
 */
function handleAdd() {
  isEdit.value = false
  dialogTitle.value = '新增商品'
  formData.name = ''
  formData.description = ''
  formData.price = 0
  formData.stock = 0
  dialogVisible.value = true
}

/**
 * 打开编辑商品弹窗
 * @param {Object} row - 当前行数据
 */
function handleEdit(row) {
  isEdit.value = true
  dialogTitle.value = '编辑商品'
  formData.id = row.id
  formData.name = row.name
  formData.description = row.description
  formData.price = Number(row.price)
  formData.stock = Number(row.stock)
  dialogVisible.value = true
}

/**
 * 删除商品
 * @param {Object} row - 当前行数据
 */
async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(`确定要删除商品 "${row.name}" 吗？`, '确认删除', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await productApi.delete(row.id)
    ElMessage.success('删除成功')
    fetchProductList()  // 重新加载列表
  } catch {
    // 用户取消或请求失败
  }
}

/**
 * 提交表单（新增或编辑）
 */
async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitLoading.value = true
  try {
    if (isEdit.value) {
      await productApi.update(formData.id, formData)
      ElMessage.success('更新成功')
    } else {
      await productApi.create(formData)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    fetchProductList()
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
