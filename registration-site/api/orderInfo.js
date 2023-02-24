import request from '@/utils/request'

const api_name = `/api/order/orderInfo`

export default {
  submitOrder(scheduleId, patientId) {
    return request({
      url: `${api_name}/auth/submitOrder/${scheduleId}/${patientId}`,
      method: 'post'
    })
  },
  //订单详细信息
  getOrder(orderId) {
    return request({
      url: `${api_name}/auth/getOrder/${orderId}`,
      method: 'get'
    })
  },
  //订单列表
  getPageList(page, limit, searchObj) {
    return request({
      url: `${api_name}/auth/${page}/${limit}`,
      method: `get`,
      params: searchObj
    })
  },
  //订单状态
  getStatusList() {
    return request({
      url: `${api_name}/auth/getStatusList`,
      method: 'get'
    })
  },
}
