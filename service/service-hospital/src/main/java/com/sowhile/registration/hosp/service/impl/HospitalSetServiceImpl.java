package com.sowhile.registration.hosp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sowhile.registration.hosp.mapper.HospitalSetMapper;
import com.sowhile.registration.hosp.service.HospitalSetService;
import com.sowhile.registration.model.hosp.HospitalSet;
import org.springframework.stereotype.Service;

@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet>
        implements HospitalSetService {
    @Override
    public String getSignKey(String hoscode) {
        QueryWrapper<HospitalSet> hospitalQueryWrapper = new QueryWrapper<>();
        hospitalQueryWrapper.eq("hoscode", hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(hospitalQueryWrapper);
        return hospitalSet.getSignKey();
    }
}
