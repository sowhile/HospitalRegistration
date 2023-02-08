package com.sowhile.registration.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sowhile.registration.hosp.repository.HospitalRepository;
import com.sowhile.registration.hosp.service.HospitalService;
import com.sowhile.registration.model.hosp.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;

    @Override
    public void save(Map<String, Object> switchMap) {
        //把参数map转换为对象Hospital
        String mapString = JSONObject.toJSONString(switchMap);
        Hospital hospital = JSONObject.parseObject(mapString, Hospital.class);

        //判断是否存在数据
        String hoscode = hospital.getHoscode();
        Hospital hospitalExist = hospitalRepository.getHospitalByHoscode(hoscode);
        //存在 修改
        if (hospitalExist != null) {
            //这里一定要获取原来hospital在mongoDB中的_id
            hospital.setId(hospitalExist.getId());

            hospital.setStatus(hospitalExist.getStatus());
            hospital.setCreateTime(hospitalExist.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
        }
        //不存在 添加
        else {
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
        }
        hospitalRepository.save(hospital);
    }

    @Override
    public Hospital getByHoscode(String hoscode) {
        return hospitalRepository.getHospitalByHoscode(hoscode);
    }
}
