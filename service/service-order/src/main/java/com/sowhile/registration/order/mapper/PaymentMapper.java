package com.sowhile.registration.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sowhile.registration.model.order.PaymentInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper extends BaseMapper<PaymentInfo> {
}
