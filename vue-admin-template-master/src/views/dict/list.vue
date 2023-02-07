<template>
  <div class="app-container">
    <div class="el-toolbar">
      <div class="el-toolbar-body" style="justify-content: flex-start;">
        <a href="http://localhost:8387/admin/cmn/dict/exportData" target="_blank">
          <el-button type="text"><i class="fa fa-plus"/>导出</el-button>
        </a>
        <el-button type="text" @click="importData"><i class="fa fa-plus"/> 导入</el-button>
      </div>
    </div>
    <el-table
      :data="list" :load="getChildrens" :tree-props="{children: 'children', hasChildren: 'hasChildren'}" border lazy
      row-key="id"
      style="width: 100%">
      <el-table-column align="left" label="名称" width="230">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>

      <el-table-column label="编码" width="220">
        <template slot-scope="{row}">
          {{ row.dictCode }}
        </template>
      </el-table-column>
      <el-table-column align="left" label="值" width="230">
        <template slot-scope="scope">
          <span>{{ scope.row.value }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建时间">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :visible.sync="dialogImportVisible" title="导入" width="480px">
      <el-form label-position="right" label-width="170px">

        <el-form-item label="导入文件">
          <el-upload
            :action="'http://localhost:8387/admin/cmn/dict/importData'"
            :multiple="false"
            :on-success="onUploadSuccess"
            class="upload-demo">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传xls文件，且不超过500kb</div>
          </el-upload>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogImportVisible = false">
          取消
        </el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import dict from '@/api/dict'

export default {
  data() {
    return {
      dialogImportVisible: false,  //用于设置导入是否弹框的初始值
      list: [], //数据字典列表数组
    }
  }, created() {
    //1为全部数据
    this.getDictList(1)
  },
  methods: {
    //数据字典的列表
    getDictList(id) {
      dict.dictList(id).then(response => {
        this.list = response.data
      })
    },
    //查询下面的内容
    getChildrens(tree, treeNode, resolve) {
      dict.dictList(tree.id).then(response => {
        resolve(response.data)
      })
    },
    //导入数据字典
    importData() {
      this.dialogImportVisible = true
    },
    onUploadSuccess(response, file) {
      this.$message.info('上传成功')
      //关闭弹框
      this.dialogImportVisible = false
      //刷新页面
      this.getDictList(1)
    },
  },
}
</script>
