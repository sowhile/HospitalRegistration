package com.sowhile.registration.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sowhile.registration.model.order.OrderInfo;
import com.sowhile.registration.order.mapper.OrderMapper;
import com.sowhile.registration.order.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends
        ServiceImpl<OrderMapper, OrderInfo> implements OrderService {
    //保存订单
    @Override
    public Long saveOrder(String scheduleId, Long patientId) {
        return null;
    }
}