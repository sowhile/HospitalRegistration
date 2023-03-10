package com.sowhile.registration.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sowhile.registration.enums.RefundStatusEnum;
import com.sowhile.registration.model.order.PaymentInfo;
import com.sowhile.registration.model.order.RefundInfo;
import com.sowhile.registration.order.mapper.RefundMapper;
import com.sowhile.registration.order.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author sowhile
 * <p>
 */
@Service
public class RefundServiceImpl extends
        ServiceImpl<RefundMapper, RefundInfo> implements RefundService {

//    @Autowired
//    private RefundMapper refundMapper;

    @Override
    public RefundInfo saveRefundInfo(PaymentInfo paymentInfo) {
        QueryWrapper<RefundInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", paymentInfo.getOrderId());
        queryWrapper.eq("payment_type", paymentInfo.getPaymentType());
        //TODO
//        RefundInfo refundInfo = refundMapper.selectOne(queryWrapper);
        RefundInfo refundInfo = baseMapper.selectOne(queryWrapper);
        if (null != refundInfo) return refundInfo;
        // 保存交易记录
        refundInfo = new RefundInfo();
        refundInfo.setCreateTime(new Date());
        refundInfo.setOrderId(paymentInfo.getOrderId());
        refundInfo.setPaymentType(paymentInfo.getPaymentType());
        refundInfo.setOutTradeNo(paymentInfo.getOutTradeNo());
        refundInfo.setRefundStatus(RefundStatusEnum.UNREFUND.getStatus());
        refundInfo.setSubject(paymentInfo.getSubject());
        //paymentInfo.setSubject("test");
        refundInfo.setTotalAmount(paymentInfo.getTotalAmount());
        //TODO
//        refundMapper.insert(refundInfo);
        baseMapper.insert(refundInfo);
        return refundInfo;
    }
}