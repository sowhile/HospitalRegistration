package com.sowhile.registration.order.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sowhile.registration.model.order.OrderInfo;
import com.sowhile.registration.model.order.PaymentInfo;

import java.util.Map;

public interface PaymentService extends IService<PaymentInfo> {
    void savePaymentInfo(OrderInfo order, Integer status);

    /**
     * 支付成功
     */
    void paySuccess(String outTradeNo, Integer paymentType, Map<String, String> paramMap);
}
