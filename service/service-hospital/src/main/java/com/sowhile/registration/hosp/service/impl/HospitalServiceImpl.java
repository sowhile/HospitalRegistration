package com.sowhile.registration.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sowhile.registration.cmn.client.DictFeignClient;
import com.sowhile.registration.common.util.BeanUtils;
import com.sowhile.registration.hosp.repository.HospitalRepository;
import com.sowhile.registration.hosp.service.HospitalService;
import com.sowhile.registration.model.hosp.Hospital;
import com.sowhile.registration.vo.hosp.HospitalQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DictFeignClient dictFeignClient;

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

    @Override
    public Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        //1.创建Pageable对象
        Pageable pageable = PageRequest.of(page - 1, limit);
        //2.创建条件匹配器
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);

        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo, hospital);
        Example<Hospital> hospitalExample = Example.of(hospital, matcher);
        Page<Hospital> pages = hospitalRepository.findAll(hospitalExample, pageable);

        //获取查询list集合，遍历，进行医院等级封装
        pages.getContent().stream().forEach(item -> {
            this.setHospitalHosType(item);
        });

        return pages;
    }

    @Override
    public void updateStatus(String id, Integer status) {
        if (status == 0 || status == 1) {
            Hospital hospital = hospitalRepository.findById(id).get();
            hospital.setStatus(status);
            hospital.setUpdateTime(new Date());
            hospitalRepository.save(hospital);
        }

    }

    @Override
    public Map<String, Object> show(String id) {
        Map<String, Object> result = new HashMap<>();
        Hospital hospital = this.setHospitalHosType(hospitalRepository.findById(id).get());
        result.put("hospital", hospital);

        //单独处理更直观
        result.put("bookingRule", hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);
        return result;
    }

    @Override
    public String getHospName(String hoscode) {
        Hospital hospitalByHoscode = hospitalRepository.getHospitalByHoscode(hoscode);
        if (hospitalByHoscode != null)
            return hospitalByHoscode.getHosname();
        else
            return null;
    }

    @Override
    public List<Hospital> findByHosame(String hosname) {
        return hospitalRepository.findHospitalByHosnameLike(hosname);
    }

    @Override
    public Map<String, Object> item(String hoscode) {
        Map<String, Object> result = new HashMap<>();
        //医院详情
        Hospital hospital = this.setHospitalHosType(this.getByHoscode(hoscode));
        result.put("hospital", hospital);
        //预约规则
        result.put("bookingRule", hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);
        return result;
    }


    private Hospital setHospitalHosType(Hospital hospital) {
        String hostypeString = dictFeignClient.getName("Hostype", hospital.getHostype());
        String provinceString = dictFeignClient.getName(hospital.getProvinceCode());
        String cityString = dictFeignClient.getName(hospital.getCityCode());
        String districtString = dictFeignClient.getName(hospital.getDistrictCode());

        hospital.getParam().put("hostypeString", hostypeString);
        hospital.getParam().put("provinceString", provinceString);
        hospital.getParam().put("cityString", cityString);
        hospital.getParam().put("districtString", districtString);
        return hospital;
    }
}
