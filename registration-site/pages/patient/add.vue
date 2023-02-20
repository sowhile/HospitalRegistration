<template>
  <!-- header -->
  <div class="nav-container page-component">
    <!--左侧导航 #start -->
    <div class="nav left-nav">
      <div class="nav-item ">
        <span class="v-link clickable dark" onclick="javascript:window.location='/user'">实名认证 </span>
      </div>
      <div class="nav-item ">
        <span class="v-link clickable dark" onclick="javascript:window.location='/order'"> 挂号订单 </span>
      </div>
      <div class="nav-item selected">
        <span class="v-link selected dark" onclick="javascript:window.location='/patient'"> 就诊人管理 </span>
      </div>
      <div class="nav-item ">
        <span class="v-link clickable dark"> 修改账号信息 </span>
      </div>
      <div class="nav-item ">
        <span class="v-link clickable dark"> 意见反馈 </span>
      </div>
    </div>
    <!-- 左侧导航 #end -->
    <!-- 右侧内容 #start -->
    <div class="page-container">
      <div class="personal-patient">
        <div class="header-wrapper">
          <div class="title"> 添加就诊人</div>
        </div>
        <div>
          <div class="sub-title">
            <div class="block"></div>
            就诊人信息
          </div>
          <div class="content-wrapper">
            <el-form ref="patient" :model="patient" :rules="validateRules" label-position="left" label-width="110px">
              <el-form-item label="姓名：" prop="name">
                <el-input v-model="patient.name" class="input v-input" placeholder="请输入真实姓名全称"/>
              </el-form-item>
              <el-form-item label="证件类型：" prop="certificatesType">
                <el-select v-model="patient.certificatesType" class="v-select patient-select"
                           placeholder="请选择证件类型">
                  <el-option
                    v-for="item in certificatesTypeList"
                    :key="item.value"
                    :label="item.name"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="证件号码：" prop="certificatesNo">
                <el-input v-model="patient.certificatesNo" class="input v-input" placeholder="请输入证件号码"/>
              </el-form-item>
              <el-form-item label="性别：" prop="sex">
                <el-radio-group v-model="patient.sex">
                  <el-radio :label="1">男</el-radio>
                  <el-radio :label="0">女</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="出生日期：" prop="birthdate">
                <el-date-picker
                  v-model="patient.birthdate"
                  class="v-date-picker"
                  placeholder="选择日期"
                  type="date">
                </el-date-picker>
              </el-form-item>
              <el-form-item label="手机号码：" prop="phone">
                <el-input v-model="patient.phone" class="input v-input" maxlength="11" placeholder="请输入手机号码"/>
              </el-form-item>
            </el-form>
          </div>
          <div class="sub-title">
            <div class="block"></div>
            建档信息（完善后部分医院首次就诊不排队建档）
          </div>
          <div class="content-wrapper">
            <el-form ref="patient" :model="patient" :rules="validateRules" label-position="left" label-width="110px">
              <el-form-item label="婚姻状况：" prop="isMarry">
                <el-radio-group v-model="patient.isMarry">
                  <el-radio :label="0">未婚</el-radio>
                  <el-radio :label="1">已婚</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="自费/医保：" prop="isInsure">
                <el-radio-group v-model="patient.isInsure">
                  <el-radio :label="0">自费</el-radio>
                  <el-radio :label="1">医保</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="当前住址：" prop="addressSelected">
                <el-cascader
                  ref="selectedShow"
                  v-model="patient.addressSelected"
                  :props="props"
                  class="v-address"></el-cascader>
              </el-form-item>
              <el-form-item label="详细地址：" prop="address">
                <el-input v-model="patient.address" class="input v-input"
                          placeholder="应公安机关要求，请填写现真实住址"/>
              </el-form-item>
            </el-form>
          </div>
          <div class="sub-title">
            <div class="block"></div>
            联系人信息（选填）
          </div>
          <div class="content-wrapper">
            <el-form :model="patient" label-position="left" label-width="110px">
              <el-form-item label="姓名：" prop="contactsName">
                <el-input v-model="patient.contactsName" class="input v-input" placeholder="请输入联系人姓名全称"/>
              </el-form-item>
              <el-form-item label="证件类型：" prop="contactsCertificatesType">
                <el-select v-model="patient.contactsCertificatesType" class="v-select patient-select"
                           placeholder="请选择证件类型">
                  <el-option
                    v-for="item in certificatesTypeList"
                    :key="item.value"
                    :label="item.name"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="证件号码：" prop="contactsCertificatesNo">
                <el-input v-model="patient.contactsCertificatesNo" class="input v-input"
                          placeholder="请输入联系人证件号码"/>
              </el-form-item>
              <el-form-item label="手机号码：" prop="contactsPhone">
                <el-input v-model="patient.contactsPhone" class="input v-input" placeholder="请输入联系人手机号码"/>
              </el-form-item>
            </el-form>
          </div>
        </div>
        <div class="bottom-wrapper">
          <div class="button-wrapper">
            <div class="v-button" @click="saveOrUpdate()">{{ submitBnt }}</div>
          </div>
        </div>
      </div>
    </div>
    <!-- 右侧内容 #end -->
  </div>
  <!-- footer -->
</template>
<script>
import '~/assets/css/hospital_personal.css'
import '~/assets/css/hospital.css'
import '~/assets/css/personal.css'
import dictApi from '@/api/dict'
import patientApi from '@/api/patient'

const defaultForm = {
  name: '',
  certificatesType: '',
  certificatesNo: '',
  sex: 1,
  birthdate: '',
  phone: '',
  isMarry: 0,
  isInsure: 0,
  provinceCode: '',
  cityCode: '',
  districtCode: '',
  addressSelected: null,
  address: '',
  contactsName: '',
  contactsCertificatesType: '',
  contactsCertificatesNo: '',
  contactsPhone: '',
  param: {}
}
export default {
  data() {
    return {
      patient: defaultForm,
      certificatesTypeList: [],
      props: {
        lazy: true,
        async lazyLoad(node, resolve) {
          const {level} = node
          //异步获取省市区
          dictApi.findByParentId(level ? node.value : '86').then(response => {
            let list = response.data
            let provinceList = list.map((item, i) => {
              return {
                value: item.id,
                label: item.name,
                leaf: node.level == 2 ? true : false,//可控制显示几级
              }
            })
            resolve && resolve(provinceList)
          })
        }
      },
      submitBnt: '保存',
      validateRules: {
        name: [{required: true, trigger: 'blur', message: '必须输入'}],
        certificatesType: [{required: true, trigger: 'blur', message: '必须输入'}],
        certificatesNo: [{required: true, trigger: 'blur', message: '必须输入'}],
        birthdate: [{required: true, trigger: 'blur', message: '必须输入'}],
        phone: [{required: true, trigger: 'blur', message: '必须输入'}],
        addressSelected: [{required: true, trigger: 'blur', message: '必须输入'}],
        address: [{required: true, trigger: 'blur', message: '必须输入'}]
      }
    }
  },
  created() {
    this.init();
  },
  mounted() {
    if (this.$route.query.id) {
      setTimeout(() => {
        this.$refs.selectedShow.presentText = this.patient.param.provinceString + '/' + this.patient.param.cityString + '/' + this.patient.param.districtString //"北京市/市辖区/西城区";// 首次手动复制
        // this.$refs.selectedShow.value = '110000/110100/110102';
      }, 1000)
    }
  },
  methods: {
    init() {
      if (this.$route.query.id) {
        const id = this.$route.query.id
        this.fetchDataById(id)
      } else {
        // 对象拓展运算符：拷贝对象，而不是赋值对象的引用
        this.patient = {...defaultForm}
      }
      this.getDict()
    },

    fetchDataById(id) {
      patientApi.getById(id).then(response => {
        this.patient = response.data
        //添加默认值
        this.patient.addressSelected = [this.patient.provinceCode, this.patient.cityCode, this.patient.districtCode]
      })
    },
    getDict() {
      dictApi.findByDictCode("CertificatesType").then(response => {
        this.certificatesTypeList = response.data
      })
    },

    saveOrUpdate() {
      this.$refs.patient.validate(valid => {
        if (valid) {
          //地址处理
          if (this.patient.addressSelected.length == 3) {
            this.patient.provinceCode = this.patient.addressSelected[0]
            this.patient.cityCode = this.patient.addressSelected[1]
            this.patient.districtCode = this.patient.addressSelected[2]
          }
          if (!this.patient.id) {
            this.saveData()
          } else {
            this.updateData()
          }
        }
      })
    },
    // 新增
    saveData() {
      if (this.submitBnt == '正在提交...') {
        this.$message.info('重复提交')
        return
      }
      this.submitBnt = '正在提交...'
      patientApi.save(this.patient).then(response => {
        this.$message.success("提交成功")
        window.location.href = '/patient'
      }).catch(e => {
        this.submitBnt = '保存'
      })
    },
    // 根据id更新记录
    updateData() {
      if (this.submitBnt == '正在提交...') {
        this.$message.info('重复提交')
        return
      }
      this.submitBnt = '正在提交...'
      patientApi.updateById(this.patient).then(response => {
        this.$message.success("提交成功")
        window.location.href = '/patient'
      }).catch(e => {
        this.submitBnt = '保存'
      })
    }
  }
}
</script>
<style>
.header-wrapper .title {
  font-size: 16px;
  margin-top: 0;
}

.sub-title {
  margin-top: 0;
}

.bottom-wrapper {
  padding: 0;
  margin: 0;
}

.bottom-wrapper .button-wrapper {
  margin-top: 0;
}
</style>
