import request from '@/utils/request'

const api_name = `/api/ucenter/wx`

export default {
  //登录
  getLoginParam() {
    return request({
      url: `${api_name}/getLoginParam`,
      method: `get`
    })
  },
  //生成微信支付二维码
  createNative(orderId) {
    return request({
      url: `/api/order/weixin/createNative/${orderId}`,
      method: 'get'
    })
  },
  //支付查询接口
  queryPayStatus(orderId) {
    return request({
      url: `/api/order/weixin/queryPayStatus/${orderId}`,
      method: 'get'
    })
  },
  //取消订单
  cancelOrder(orderId) {
    return request({
      url: `/api/order/orderInfo/auth/cancelOrder/${orderId}`,
      method: 'get'
    })
  },
}
