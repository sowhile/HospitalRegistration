import request from '@/utils/request'

//查询医院列表
export function getHospSetList(current, limit, searchObj) {
  return request({
    url: `/admin/hosp/hospitalSet/findPageHospSet/${current}/${limit}`,
    method: 'post',
    data: searchObj //使用json传递
  })
}

//删除医院设置
export function deleteHospSet(id) {
  return request({
    url: `/admin/hosp/hospitalSet/${id}`,
    method: 'delete'
  })
}

