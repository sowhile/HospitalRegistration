package com.sowhile.registration.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sowhile.registration.model.hosp.HospitalSet;
import com.sowhile.registration.vo.order.SignInfoVo;

public interface HospitalSetService extends IService<HospitalSet> {
    String getSignKey(String hoscode);

    //获取医院签名信息
    SignInfoVo getSignInfoVo(String hoscode);

    int getStatus(String hoscode);
}
