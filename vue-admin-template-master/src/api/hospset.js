import request from '@/utils/request'

export function getHospSetList(current, limit, searchObj) {
  return request({
    url: `/admin/hosp/hospitalSet/findPageHospSet/${current}/${limit}`,
    method: 'post',
    data: searchObj //使用json传递
  })
}
