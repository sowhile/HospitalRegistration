package com.sowhile.registration.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sowhile.registration.model.order.PaymentInfo;
import com.sowhile.registration.model.order.RefundInfo;

/**
 * @author sowhile
 * <p>
 */
public interface RefundService extends IService<RefundInfo> {
    /**
     * 保存退款记录
     *
     * @param paymentInfo
     */
    RefundInfo saveRefundInfo(PaymentInfo paymentInfo);
}