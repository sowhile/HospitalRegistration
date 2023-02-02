<template>
  <div class="app-container">
    <div class="el-toolbar">
      <div class="el-toolbar-body" style="justify-content: flex-start;">
        <a href="http://localhost:8387/admin/cmn/dict/exportData" target="_blank">
          <el-button type="text"><i class="fa fa-plus"/>导出</el-button>
        </a>
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
  </div>
</template>

<script>
import dict from '@/api/dict'

export default {
  data() {
    return {
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
  },
}
</script>
