package com.sowhile.registration.hosp.service;

import com.sowhile.registration.model.hosp.Hospital;
import com.sowhile.registration.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface HospitalService {
    void save(Map<String, Object> switchMap);

    Hospital getByHoscode(String hoscode);

    Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    void updateStatus(String id, Integer status);

    Map<String, Object> show(String id);

    String getHospName(String hoscode);

    List<Hospital> findByHosame(String hosname);

    Map<String, Object> item(String hoscode);
}
