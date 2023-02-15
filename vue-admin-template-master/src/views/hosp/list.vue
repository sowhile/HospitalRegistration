<template>
  <div class="app-container">
    <el-form :inline="true" class="demo-form-inline">

      <el-form-item>
        <el-select v-model="searchObj.provinceCode" placeholder="请选择省" @change="provinceChanged()">
          <el-option v-for="item in provinceList" :key="item.id" :label="item.name" :value="item.id"/>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-select v-model="searchObj.cityCode" placeholder="请选择市" @change="cityChanged()">
          <el-option v-for="item in cityList" :key="item.id" :label="item.name" :value="item.id"/>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-input v-model="searchObj.hosname" placeholder="医院名称"/>
      </el-form-item>

      <el-button icon="el-icon-search" type="primary" @click="fetchData()">查询</el-button>
      <el-button type="default" @click="resetData()">清空</el-button>
    </el-form>

    <!-- banner列表 -->
    <el-table v-loading="listLoading" :data="list" border fit highlight-current-row>

      <el-table-column align="center" label="序号" width="60">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column label="医院logo">
        <template slot-scope="scope">
          <img :src="'data:image/jpeg;base64,'+scope.row.logoData" width="80">
        </template>
      </el-table-column>

      <el-table-column label="医院名称" prop="hosname"/>
      <el-table-column label="等级" prop="param.hostypeString" width="90"/>
      <el-table-column label="详情地址" prop="param.fullAddress"/>
      <el-table-column label="状态" width="80">
        <template slot-scope="scope">
          {{ scope.row.status === 0 ? '未上线' : '已上线' }}
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime"/>

      <el-table-column align="center" label="操作" width="230">
        <template slot-scope="scope">
          <el-button v-if="scope.row.status === 1" size="mini" type="danger" @click="updateStatus(scope.row.id, 0)">
            下线
          </el-button>
          <el-button v-if="scope.row.status === 0" size="mini" type="primary" @click="updateStatus(scope.row.id, 1)">
            上线
          </el-button>
          <router-link :to="'/hospSet/hospital/show/'+scope.row.id">
            <el-button size="mini" type="primary">查看</el-button>
          </router-link>
          <router-link :to="'/hospSet/hospital/schedule/'+scope.row.hoscode">
            <el-button size="mini" type="primary">排班</el-button>
          </router-link>
        </template>

      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      :current-page="page"
      :page-size="limit"
      :page-sizes="[5, 10, 20, 30, 40, 50, 100]"
      :total="total"
      layout="sizes, prev, pager, next, jumper, ->, total, slot"
      style="padding: 30px 0; text-align: center;"
      @current-change="fetchData"
      @size-change="changeSize"
    />
  </div>
</template>


<script>
import hospitalApi from '@/api/hosp'

export default {
  data() {
    return {
      listLoading: true, // 数据是否正在加载
      list: null, // banner列表
      total: 0, // 数据库中的总记录数
      page: 1, // 默认页码
      limit: 10, // 每页记录数
      searchObj: {}, // 查询表单对象
      provinceList: [],
      cityList: [],
      districtList: []
    }
  },
  created() {
    //调用医院列表
    this.fetchData(this.page)
    this.findAllProvince()
  },
  methods: {
    // 加载医院列表数据
    fetchData(page = 1) {
      // 异步获取远程数据(ajax)
      this.page = page
      hospitalApi.getHospList(this.page, this.limit, this.searchObj)
        .then(response => {
            this.list = response.data.content
            this.total = response.data.totalElements
            // 数据加载并绑定成功
            this.listLoading = false
          }
        )
    },
    //查询所有省
    findAllProvince() {
      hospitalApi.findByDictCode('Province')
        .then(response => {
          this.provinceList = response.data
        })
    },
    // 当页码发生改变的时候
    changeSize(size) {
      this.limit = size
      this.fetchData(1)
    },
    // 重置查询表单
    resetData() {
      this.searchObj = {}
      this.fetchData()
    },

    //点击省，显示对应的市 联动
    provinceChanged() {
      this.searchObj.cityCode = null
      this.cityList = []

      this.districtList = []
      this.searchObj.districtCode = null
      hospitalApi.findChildId(this.searchObj.provinceCode)
        .then(response => {
          this.cityList = response.data
        })
    },
    //点击选择城市后
    cityChanged() {
      this.$forceUpdate()
      this.districtList = []
      this.searchObj.districtCode = null
      hospitalApi.findChildId(this.searchObj.cityCode)
        .then(response => {
          this.districtList = response.data
        })
    },
    //
    districtChanged() {
    },
    //更新上线下线状态
    updateStatus(id, status) {
      hospitalApi.updateStatus(id, status)
        .then(response => {
          this.fetchData(this.page)
        })
    },
  }
}
</script>
