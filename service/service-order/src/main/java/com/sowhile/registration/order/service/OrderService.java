package com.sowhile.registration.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sowhile.registration.model.order.OrderInfo;

public interface OrderService extends IService<OrderInfo> {
    //保存订单
    Long saveOrder(String scheduleId, Long patientId);
}