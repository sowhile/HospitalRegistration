import request from '@/utils/request'

const hosp_api_name = `/admin/hosp/hospital`
const schedule_api_name = `/admin/hosp/schedule`
const dict_api_name = `/admin/cmn/dict`
export default {
  //医院列表
  getHospList(page, limit, searchObj) {
    return request({
      url: `${hosp_api_name}/list/${page}/${limit}`,
      method: 'get',
      params: searchObj
    })
  },
  //根据dictCode查询所有的子节点（所有省）
  findByDictCode(dictCode) {
    return request({
      url: `${dict_api_name}/findByDictCode/${dictCode}`,
      method: 'get',
    })
  },
  //根据id查询所有的子节点
  findChildId(id) {
    return request({
      url: `${dict_api_name}/findChildData/${id}`,
      method: 'get',
    })
  },
  //更新医院的上线状态
  updateStatus(id, status) {
    return request({
      url: `${hosp_api_name}/updateStatus/${id}/${status}`,
      method: 'get',
    })
  },
  //查看医院详情
  getHospById(id) {
    return request({
      url: `${hosp_api_name}/showHospDetail/${id}`,
      method: 'get'
    })
  },
  //查看医院科室
  getDeptByHoscode(hoscode) {
    return request({
      url: `/admin/hosp/department/getDeptList/${hoscode}`,
      method: 'get'
    })
  },
  getScheduleRule(page, limit, hoscode, depcode) {
    return request({
      url: `${schedule_api_name}/getScheduleRule/${page}/${limit}/${hoscode}/${depcode}`,
      method: 'get'
    })
  },
  //查询排班详情
  getScheduleDetail(hoscode, depcode, workDate) {
    return request({
      url: `${schedule_api_name}/getScheduleDetail/${hoscode}/${depcode}/${workDate}`,
      method: 'get'
    })
  }
}

