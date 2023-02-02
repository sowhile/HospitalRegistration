<template>
  <div class="app-container">
    <el-form label-width="120px">
      <el-form-item label="医院名称">
        <el-input v-model="hospitalSet.hosname"/>
      </el-form-item>
      <el-form-item label="医院编号">
        <el-input v-model="hospitalSet.hoscode"/>
      </el-form-item>
      <el-form-item label="api基础路径">
        <el-input v-model="hospitalSet.apiUrl"/>
      </el-form-item>
      <el-form-item label="联系人姓名">
        <el-input v-model="hospitalSet.contactsName"/>
      </el-form-item>
      <el-form-item label="联系人手机">
        <el-input v-model="hospitalSet.contactsPhone"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="saveOrUpdate">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>


<script>
import hospSet from '@/api/hospset'

export default {
  //定义变量和初始值
  data() {
    return {
      hospitalSet: {}
    }
  },
  //页面渲染之前执行
  created() {
    //调用接口得到医院信息
    if (this.$route.params && this.$route.params.id) {
      const id = this.$route.params.id;
      this.getHospSet(id);
    }
    // else {
    //   清空表单数据
    //   this.hospitalSet = {}
    // }
  },
  methods: {
    //根据id查询
    getHospSet(id) {
      hospSet.getHospSet(id)
        .then(response => {
          this.hospitalSet = response.data;
        })
    },

    saveOrUpdate() {
      if (!this.hospitalSet.id) {
        this.save();
      } else {
        this.update();
      }
    },
    save() {
      hospSet.saveHosp(this.hospitalSet)
        .then(response => {
          //提示
          this.$message({
            type: 'success',
            message: '添加成功!'
          })
          //跳转列表页面，使用路由跳转方式实现
          this.$router.push({path: '/hospSet/list'})
        })
    },
    update() {
      hospSet.updateHospSet(this.hospitalSet)
        .then(response => {
          //提示
          this.$message({
            type: 'success',
            message: '修改成功!'
          })
          //跳转列表页面，使用路由跳转方式实现
          this.$router.push({path: '/hospSet/list'})
        })
    }
  },
}
</script>

<style scoped>

</style>
