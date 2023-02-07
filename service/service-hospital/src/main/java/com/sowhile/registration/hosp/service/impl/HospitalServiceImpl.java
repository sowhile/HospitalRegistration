package com.sowhile.registration.hosp.service.impl;

import com.sowhile.registration.hosp.repository.HospitalRepository;
import com.sowhile.registration.hosp.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;
}
