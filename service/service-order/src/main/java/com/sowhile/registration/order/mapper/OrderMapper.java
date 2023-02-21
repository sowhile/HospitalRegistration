package com.sowhile.registration.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sowhile.registration.model.order.OrderInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<OrderInfo> {
}
