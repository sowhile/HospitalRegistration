<template>
  <div class="app-container">
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="searchObj.hosname" placeholder="医院名称"/>
      </el-form-item>
      <el-form-item>
        <el-input v-model="searchObj.hoscode" placeholder="医院编号"/>
      </el-form-item>
      <el-button type="primary" icon="el-icon-search" @click="getList()">查询</el-button>
    </el-form>

    <!-- banner列表 -->
    <el-table
      :data="list" stripe style="width: 100%">

      <el-table-column type="index" width="50" label="序号"/>
      <el-table-column prop="hosname" label="医院名称"/>
      <el-table-column prop="hoscode" label="医院编号"/>
      <el-table-column prop="apiUrl" label="api基础路径" width="200"/>
      <el-table-column prop="contactsName" label="联系人姓名"/>
      <el-table-column prop="contactsPhone" label="联系人手机"/>
      <el-table-column label="状态" width="80">
        <template slot-scope="scope">
          {{ scope.row.status === 1 ? '可用' : '不可用' }}
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
      @current-change="getList"
    />
  </div>
</template>

<script>
import {getHospSetList} from '@/api/hospset'

export default {
  //定义变量和初始值
  data() {
    return {
      current: 1, //当前页
      limit: 6, //每页显示记录数
      searchObj: {},  //条件封装对象
      list: [], //每页数据集合
      total: 0 //总记录数
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
      getHospSetList(this.current, this.limit, this.searchObj)
        .then(response => {
          this.list = response.data.records;
          this.total = response.data.total;
          console.log(response);
        })
        .catch(err => {
          console.log(err);
        })
    }
  }
}
</script>
