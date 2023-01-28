package com.sowhile.registration.hosp.controller;

import com.sowhile.registration.hosp.service.HospitalSetService;
import com.sowhile.registration.model.hosp.HospitalSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {
    @Autowired
    private HospitalSetService hospitalSetService;

    //1.查询医院设置表里的所有信息
    @GetMapping("findAll")
    public List<HospitalSet> findAllHospitalSet() {
        List<HospitalSet> list = hospitalSetService.list();
        return list;
    }

    //2.逻辑删除医院
    @DeleteMapping("{id}")
    public boolean removeHospitalSet(@PathVariable Long id) {
        boolean flag = hospitalSetService.removeById(id);
        return flag;
    }
}
