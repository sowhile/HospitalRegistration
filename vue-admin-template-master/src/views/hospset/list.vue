<template>
  <div class="app-container">
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="searchObj.hosname" placeholder="医院名称"></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="searchObj.hoscode" placeholder="医院编号"></el-input>
      </el-form-item>
      <el-button type="primary" icon="el-icon-search" @click="getList()">查询</el-button>
    </el-form>
    <!-- 工具条 -->
    <div>
      <el-button type="danger" size="mini" @click="removeRows()">批量删除</el-button>
    </div>
    <!-- banner列表 -->
    <el-table
      :data="list" stripe style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column type="index" width="50" label="序号"></el-table-column>
      <el-table-column prop="hosname" label="医院名称"></el-table-column>
      <el-table-column prop="hoscode" label="医院编号"></el-table-column>
      <el-table-column prop="apiUrl" label="api基础路径" width="200"></el-table-column>
      <el-table-column prop="contactsName" label="联系人姓名"></el-table-column>
      <el-table-column prop="contactsPhone" label="联系人手机"></el-table-column>
      <el-table-column label="状态" width="80">
        <template slot-scope="scope">
          {{ scope.row.status === 1 ? '可用' : '不可用' }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="220">
        <template slot-scope="scope">
          <el-button v-if="scope.row.status===1" icon="el-icon-remove-outline" size="mini"
                     type="primary" @click="lockHostSet(scope.row.id,0)">锁定
          </el-button>
          <el-button v-if="scope.row.status===0" icon="el-icon-circle-plus-outline" size="mini"
                     type="warning" @click="lockHostSet(scope.row.id,1)">解锁
          </el-button>
          <router-link :to="'/hospSet/edit/'+scope.row.id">
            <el-button icon="el-icon-edit" size="mini" type="success"></el-button>
          </router-link>
          <el-button type="danger" size="mini"
                     icon="el-icon-delete" @click="removeDataById(scope.row.id)">
          </el-button>

        </template>
      </el-table-column>

    </el-table>
    <!-- 分页 -->
    <el-pagination
      background
      :current-page="current"
      :page-size="limit"
      :total="total"
      style="padding: 30px 0; text-align: center;"
      layout="total, prev, pager, next, jumper"
      @current-change="getList"></el-pagination>
  </div>
</template>

<script>
import hospSet from '@/api/hospset'

export default {
  //定义变量和初始值
  data() {
    return {
      current: 1, //当前页
      limit: 6, //每页显示记录数
      searchObj: {},  //条件封装对象
      list: [], //每页数据集合
      total: 0, //总记录数
      multipleSelection: [], //批量选中的记录列表
    }
  },
  //在页面渲染之前执行
  created() {
    this.getList();
  },
  //定义方法，进行请求接口的调用
  methods: {
    //默认为第1页
    getList(page = 1) {
      this.current = page;
      hospSet.getHospSetList(this.current, this.limit, this.searchObj)
        .then(response => {
          this.list = response.data.records;
          this.total = response.data.total;
        })
        .catch(err => {
        })
    },
    //删除医院
    removeDataById(id) {
      this.$confirm('此操作将永久删除医院信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => { //确定执行then方法
        //调用接口
        hospSet.deleteHospSet(id)
          .then(response => {
            //提示
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            //刷新页面
            this.getList(1)
          })
      })
    },
    //用于获取复选框的值
    handleSelectionChange(selection) {
      this.multipleSelection = selection;
    },
    //批量删除
    removeRows() {
      console.log(this.multipleSelection);
      if (this.multipleSelection.length === 0) {
        this.$alert('请至少选择一个医院！', '批量删除', {
          confirmButtonText: '好',
          // callback: action => {
          //   this.$message({
          //     type: 'info',
          //     message: `删除`
          //   });
          // }
        })
      } else {
        this.$confirm('此操作将永久删除医院信息, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => { //确定执行then方法
          let idList = [];
          //遍历得到id值
          for (let i = 0; i < this.multipleSelection.length; i++) {
            let obj = this.multipleSelection[i];
            let id = obj.id;
            idList.push(id);
          }
          //调用接口
          hospSet.batchHospSet(idList)
            .then(response => {
              //提示
              this.$message({
                type: 'success',
                message: '删除成功!'
              })
              //刷新页面
              this.getList(1)
            })
        })
      }
    },
    //锁定和取消锁定
    lockHostSet(id, status) {
      hospSet.lockHospSet(id, status)
        .then(response => {
          //刷新
          this.getList(this.current);
        })
    },
  }
}
</script>
