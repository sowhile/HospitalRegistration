package com.sowhile.registration.hosp.service;

import com.sowhile.registration.model.hosp.Hospital;

import java.util.Map;

public interface HospitalService {
    void save(Map<String, Object> switchMap);

    Hospital getByHoscode(String hoscode);
}
