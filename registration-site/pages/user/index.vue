<template>
  <!-- header -->
  <div class="nav-container page-component">
    <!--左侧导航 #start -->
    <div class="nav left-nav">
      <div class="nav-item selected">
        <span class="v-link selected dark" onclick="javascript:window.location='/user'">实名认证 </span>
      </div>
      <div class="nav-item">
        <span class="v-link selected dark" onclick="javascript:window.location='/order'"> 挂号订单 </span>
      </div>
      <div class="nav-item ">
        <span class="v-link clickable dark" onclick="javascript:window.location='/patient'"> 就诊人管理 </span>
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
      <div>
        <div class="title"> 实名认证</div>
        <div class="status-bar">
          <div class="status-wrapper"><span class="iconfont"></span>
            <div v-if="userInfo.authStatus === 0">请进行实名认证</div>
            <div v-if="userInfo.authStatus === 1">审核中</div>
            <div v-if="userInfo.authStatus === 2">认证成功</div>
            <div v-if="userInfo.authStatus === -1">认证失败</div>
          </div>
        </div>
        <div class="tips"><span class="iconfont"></span>
          完成实名认证后才能添加就诊人，正常进行挂号，为了不影响后续步骤，建议您提前进行实名认证。
        </div>
        <div v-if="userInfo.authStatus == 0" class="form-wrapper">
          <div>
            <el-form :model="userAuth" label-position="left" label-width="110px">
              <el-form-item class="form-normal" label="姓名：" prop="name">
                <div class="name-input">
                  <el-input v-model="userAuth.name" class="input v-input" placeholder="请输入姓名"/>
                </div>
              </el-form-item>
              <el-form-item label="证件类型：" prop="certificatesType">
                <el-select v-model="userAuth.certificatesType" class="v-select patient-select"
                           placeholder="请选择证件类型">
                  <el-option
                    v-for="item in certificatesTypeList"
                    :key="item.value"
                    :label="item.name"
                    :value="item.name">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="证件号码：" prop="certificatesNo">
                <el-input v-model="userAuth.certificatesNo" class="input v-input" placeholder="请输入证件号码"/>
              </el-form-item>
              <el-form-item label="上传证件：" prop="name">
                <div class="upload-wrapper">
                  <div class="avatar-uploader">
                    <el-upload
                      :action="fileUrl"
                      :on-success="onUploadSuccess"
                      :show-file-list="false"
                      class="avatar-uploader">
                      <div class="upload-inner-wrapper">
                        <img v-if="userAuth.certificatesUrl" :src="userAuth.certificatesUrl" class="avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                        <div v-if="!userAuth.certificatesUrl" class="text"> 上传证件照片</div>
                      </div>
                    </el-upload>
                  </div>
                  <img class="example" src="//img.114yygh.com/static/web/auth_example.png">
                </div>
              </el-form-item>
            </el-form>
            <div class="bottom-wrapper">
              <div class="button-wrapper">
                <div class="v-button" @click="saveUserAuth()">{{ submitBnt }}</div>
              </div>
            </div>
          </div>
        </div>
        <div v-if="userInfo.authStatus != 0" class="context-container">
          <div>
            <!--            <el-form :model="formData" label-width="110px" label-position="right">-->
            <el-form label-position="right" label-width="110px">
              <el-form-item class="form-normal" label="姓名：" prop="name">
                <div class="name-input">
                  {{ userInfo.name }}
                </div>
              </el-form-item>
              <el-form-item label="证件类型：" prop="name">
                {{ userInfo.certificatesType }}
              </el-form-item>
              <el-form-item label="证件号码：" prop="name">
                {{ userInfo.certificatesNo }}
              </el-form-item>
            </el-form>
          </div>
        </div>
      </div>
    </div><!-- 右侧内容 #end -->
    <!-- 登录弹出框 -->
  </div>
  <!-- footer -->
</template>

<script>
import '~/assets/css/hospital_personal.css'
import '~/assets/css/hospital.css'
import '~/assets/css/personal.css'
import dictApi from '@/api/dict'
import userInfoApi from '@/api/userInfo'

const defaultForm = {
  name: '',
  certificatesType: '',
  certificatesNo: '',
  certificatesUrl: ''
}
export default {
  data() {
    return {
      userAuth: defaultForm,
      certificatesTypeList: [],
      fileUrl: 'http://localhost/api/oss/file/fileUpload?fileHost=userAuth',
      userInfo: {
        param: {}
      },
      submitBnt: '提交'
    }
  },
  created() {
    this.init()
  },
  methods: {
    init() {
      this.getUserInfo()
      this.getDict()
    },
    getUserInfo() {
      userInfoApi.getUserInfo()
        .then(response => {
          this.userInfo = response.data
        })
    },
    saveUserAuth() {
      if (this.submitBnt == '正在提交...') {
        this.$message.info('重复提交')
        return
      }
      this.submitBnt = '正在提交...'
      userInfoApi.saveUserAuth(this.userAuth)
        .then(response => {
          this.$message.success("提交成功")
          window.location.reload()
        }).catch(e => {
        this.submitBnt = '提交'
      })
    },
    getDict() {
      dictApi.findByDictCode("CertificatesType")
        .then(response => {
          this.certificatesTypeList = response.data
        })
    },
    onUploadSuccess(response, file) {
      if (response.code !== 200) {
        this.$message.error("上传失败")
        return
      }
      // 填充上传文件列表
      this.userAuth.certificatesUrl = file.response
    }
  }
}
</script>
<style>
.header-wrapper .title {
  font-size: 16px;
  margin-top: 0;
}

.content-wrapper {
  margin-left: 0;
}

.patient-card .el-card__header .detail {
  font-size: 14px;
}

.page-container .title {
  letter-spacing: 1px;
  font-weight: 700;
  color: #333;
  font-size: 16px;
  margin-top: 0;
  margin-bottom: 20px;
}

.page-container .tips {
  width: 100%;
  padding-left: 0;
}

.page-container .form-wrapper {
  padding-left: 92px;
  width: 580px;
}

.form-normal {
  height: 40px;
}

.bottom-wrapper {
  width: 100%;
  padding: 0;
  margin-top: 0;
}
</style>
