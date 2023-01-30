package com.sowhile.registration.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sowhile.registration.common.result.Result;
import com.sowhile.registration.common.util.MD5;
import com.sowhile.registration.hosp.service.HospitalSetService;
import com.sowhile.registration.model.hosp.HospitalSet;
import com.sowhile.registration.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@Api
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
@CrossOrigin
public class HospitalSetController {
    @Autowired
    private HospitalSetService hospitalSetService;

    //1.查询医院设置表里的所有信息
    @ApiOperation(value = "查询所有医院信息")
    @GetMapping("findAll")
    public Result findAllHospitalSet() {
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    //2.逻辑删除医院
    @ApiOperation(value = "逻辑删除医院")
    @DeleteMapping("{id}")
    public Result removeHospitalSet(@PathVariable Integer id) {
        boolean flag = hospitalSetService.removeById(id);
        if (flag) return Result.ok();
        else return Result.fail();
    }

    //3.条件查询带分页
    @ApiOperation(value = "分页条件查询")
    @PostMapping("findPageHospSet/{current}/{limit}")
    public Result findPageHospSet(@PathVariable("current") Integer current,
                                  @PathVariable("limit") Integer limit,
                                  @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {
        //创建Page对象，传入当前页，每页记录数
        Page<HospitalSet> hospitalSetPage = new Page<>(current, limit);
        //构建条件
        QueryWrapper<HospitalSet> hospitalSetQueryWrapper = new QueryWrapper<>();
        String hosname = hospitalSetQueryVo.getHosname();
        String hoscode = hospitalSetQueryVo.getHoscode();
        if (!StringUtils.isEmpty(hosname)) hospitalSetQueryWrapper.like("hosname", hosname);
        if (!StringUtils.isEmpty(hoscode)) hospitalSetQueryWrapper.eq("hoscode", hoscode);
        //调用方法实现分页查询
        Page<HospitalSet> setPage = hospitalSetService.page(hospitalSetPage, hospitalSetQueryWrapper);
        return Result.ok(setPage);
    }

    //4.添加医院设置
    @ApiOperation(value = "添加医院")
    @PostMapping("saveHospitalSet")
    public Result saveHospitalSet(@RequestBody(required = true) HospitalSet hospitalSet) {
        //医院状态码为1表示正常
        hospitalSet.setStatus(1);
        //生成sign_key
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis() + "" + new Random().nextInt(1000)));
        //调用service
        boolean save = hospitalSetService.save(hospitalSet);
        if (save) return Result.ok();
        else return Result.fail();
    }

    //5.根据id获取医院设置
    @ApiOperation(value = "根据id获取医院设置")
    @GetMapping("getHospSet/{id}")
    public Result getHospitalSet(@PathVariable("id") Integer id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    //6.修改医院设置
    @ApiOperation(value = "修改医院")
    @PostMapping("updateHospitalSet/{id}")
    public Result updateHospitalSet(@RequestBody(required = true) HospitalSet hospitalSet) {
        boolean updateByIdFlag = hospitalSetService.updateById(hospitalSet);
        if (updateByIdFlag) return Result.ok();
        else return Result.fail();
    }

    //7.批量删除医院设置
    @ApiOperation("批量删除医院")
    @DeleteMapping("batchRemove")
    public Result batchRemoveHospitalSet(@RequestBody List<Integer> idList) {
        hospitalSetService.removeByIds(idList);
        return Result.ok();
    }

    //8.医院锁定和解锁
    @ApiOperation("医院锁定和解锁")
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Integer id,
                                  @PathVariable Integer status) {
        //根据id查询医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        //设置状态
        hospitalSet.setStatus(status);
        //调用方法
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();
    }


    //9.发送签名key
    @PutMapping("sendKey/{id}")
    public Result lockHospitalSet(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        //TODO 发送短信
        return Result.ok();
    }

}
