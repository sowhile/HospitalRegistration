package com.sowhile.registration.hosp.controller.api;

import com.sowhile.registration.common.exception.RegistrationException;
import com.sowhile.registration.common.helper.HttpRequestHelper;
import com.sowhile.registration.common.result.Result;
import com.sowhile.registration.common.result.ResultCodeEnum;
import com.sowhile.registration.common.util.MD5;
import com.sowhile.registration.hosp.service.DepartmentService;
import com.sowhile.registration.hosp.service.HospitalService;
import com.sowhile.registration.hosp.service.HospitalSetService;
import com.sowhile.registration.hosp.service.ScheduleService;
import com.sowhile.registration.model.hosp.Department;
import com.sowhile.registration.model.hosp.Schedule;
import com.sowhile.registration.vo.hosp.DepartmentQueryVo;
import com.sowhile.registration.vo.hosp.ScheduleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 供医院接口管理系统调用的 API
 * 通过此API向 mongoDB 添加医院科室、排班等数据
 */
@Api
@RestController
@RequestMapping("/api/hosp")
public class ApiController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalSetService hospitalSetService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation("上传科室接口")
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request) {
        //获取传递过来的科室信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        //1.获取传递过来的签名(MD5加密)
        String sign = (String) paramMap.get("sign");
        //2.查询数据库
        String hoscode = (String) paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        //查询医院锁定状态
        int status = hospitalSetService.getStatus(hoscode);
        //3.把查出来的签名进行MD5加密
        String encrypt = MD5.encrypt(signKey);

        if (StringUtils.isEmpty(hoscode)) {
            throw new RegistrationException(ResultCodeEnum.PARAM_ERROR);
        }
        if (!sign.equals(encrypt)) {
            throw new RegistrationException(ResultCodeEnum.SIGN_ERROR);
        }
        if (status == 0) {
            throw new RegistrationException(ResultCodeEnum.HOSPITAL_LOCK);
        }

        //调用service的方法
        departmentService.save(paramMap);
        return Result.ok();
    }

    @ApiOperation("查询科室接口")
    @PostMapping("department/list")
    public Result findDepartment(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //必须参数校验
        String hoscode = (String) paramMap.get("hoscode");
        if (StringUtils.isEmpty(hoscode)) {
            throw new RegistrationException(ResultCodeEnum.PARAM_ERROR);
        }
        if (!paramMap.get("sign").equals(MD5.encrypt(hospitalSetService.getSignKey(hoscode)))) {
            throw new RegistrationException(ResultCodeEnum.SIGN_ERROR);
        }

        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String) paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 10 : Integer.parseInt((String) paramMap.get("limit"));

        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);
        //调用service方法
        Page<Department> pageModel = departmentService.findPageDepartment(page, limit, departmentQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation("删除科室接口")
    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request) {
        //获取传递过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        //1.获取传递过来的签名(MD5加密)
        String sign = (String) paramMap.get("sign");
        //2.查询数据库
        String hoscode = (String) paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        //查询医院锁定状态
        int status = hospitalSetService.getStatus(hoscode);
        //3.把查出来的签名进行MD5加密
        String encrypt = MD5.encrypt(signKey);

        if (StringUtils.isEmpty(hoscode)) {
            throw new RegistrationException(ResultCodeEnum.PARAM_ERROR);
        }
        if (!sign.equals(encrypt)) {
            throw new RegistrationException(ResultCodeEnum.SIGN_ERROR);
        }
        if (status == 0) {
            throw new RegistrationException(ResultCodeEnum.HOSPITAL_LOCK);
        }

        String depcode = (String) paramMap.get("depcode");

        departmentService.remove(hoscode, depcode);
        return Result.ok();
    }

    @ApiOperation("上传医院接口")
    @PostMapping("saveHospital")
    public Result saveHosp(HttpServletRequest request) {
        //获取传递过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        //1.获取传递过来的签名(MD5加密)
        String sign = (String) paramMap.get("sign");
        //2.查询数据库
        String hoscode = (String) paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        //查询医院锁定状态
        int status = hospitalSetService.getStatus(hoscode);
        //3.把查出来的签名进行MD5加密
        String encrypt = MD5.encrypt(signKey);

        if (StringUtils.isEmpty(hoscode)) {
            throw new RegistrationException(ResultCodeEnum.PARAM_ERROR);
        }
        if (!sign.equals(encrypt)) {
            throw new RegistrationException(ResultCodeEnum.SIGN_ERROR);
        }
        if (status == 0) {
            throw new RegistrationException(ResultCodeEnum.HOSPITAL_LOCK);
        }

        //传输过程中'+'转换为了' ',所以要转换回来
        String logoData = (String) paramMap.get("logoData");
        String replaceAll = logoData.replaceAll(" ", "+");
        paramMap.put("logoData", replaceAll);
        //调用service方法
        hospitalService.save(paramMap);
        return Result.ok();
    }

    @ApiOperation(value = "获取医院信息")
    @PostMapping("hospital/show")
    public Result hospital(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //必须参数校验
        String hoscode = (String) paramMap.get("hoscode");
        if (StringUtils.isEmpty(hoscode)) {
            throw new RegistrationException(ResultCodeEnum.PARAM_ERROR);
        }
        //签名校验
//        if (!HttpRequestHelper.isSignEquals(paramMap, hospitalSetService.getSignKey(hoscode))) {
//            throw new RegistrationException(ResultCodeEnum.SIGN_ERROR);
//        }
        if (!paramMap.get("sign").equals(MD5.encrypt(hospitalSetService.getSignKey(hoscode)))) {
            throw new RegistrationException(ResultCodeEnum.SIGN_ERROR);
        }

        return Result.ok(hospitalService.getByHoscode((String) paramMap.get("hoscode")));
    }

    @ApiOperation(value = "上传排班信息")
    @PostMapping("saveSchedule")
    public Result saveSchedule(HttpServletRequest request) {
        //获取传递过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        //1.获取传递过来的签名(MD5加密)
        String sign = (String) paramMap.get("sign");
        //2.查询数据库
        String hoscode = (String) paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        //查询医院锁定状态
        int status = hospitalSetService.getStatus(hoscode);
        //3.把查出来的签名进行MD5加密
        String encrypt = MD5.encrypt(signKey);

        if (StringUtils.isEmpty(hoscode)) {
            throw new RegistrationException(ResultCodeEnum.PARAM_ERROR);
        }
        if (!sign.equals(encrypt)) {
            throw new RegistrationException(ResultCodeEnum.SIGN_ERROR);
        }

        if (status == 0) {
            throw new RegistrationException(ResultCodeEnum.HOSPITAL_LOCK);
        }

        scheduleService.save(paramMap);
        return Result.ok();
    }

    @ApiOperation(value = "查询排班信息")
    @PostMapping("schedule/list")
    public Result findSchedule(HttpServletRequest request) {
        //获取传递过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        //1.获取传递过来的签名(MD5加密)
        String sign = (String) paramMap.get("sign");
        //2.查询数据库
        String hoscode = (String) paramMap.get("hoscode");
        String depcode = (String) paramMap.get("depcode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        //3.把查出来的签名进行MD5加密
        String encrypt = MD5.encrypt(signKey);

        if (StringUtils.isEmpty(hoscode)) {
            throw new RegistrationException(ResultCodeEnum.PARAM_ERROR);
        }
        if (!sign.equals(encrypt)) {
            throw new RegistrationException(ResultCodeEnum.SIGN_ERROR);
        }

        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String) paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 10 : Integer.parseInt((String) paramMap.get("limit"));

        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);
        //调用service方法
        Page<Schedule> pageModel = scheduleService.findPageSchedule(page, limit, scheduleQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation("删除排班接口")
    @PostMapping("schedule/remove")
    public Result removeSchedule(HttpServletRequest request) {
        //获取传递过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        //1.获取传递过来的签名(MD5加密)
        String sign = (String) paramMap.get("sign");
        //2.查询数据库
        String hoscode = (String) paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        //查询医院锁定状态
        int status = hospitalSetService.getStatus(hoscode);
        //3.把查出来的签名进行MD5加密
        String encrypt = MD5.encrypt(signKey);

        if (StringUtils.isEmpty(hoscode)) {
            throw new RegistrationException(ResultCodeEnum.PARAM_ERROR);
        }
        if (!sign.equals(encrypt)) {
            throw new RegistrationException(ResultCodeEnum.SIGN_ERROR);
        }
        if (status == 0) {
            throw new RegistrationException(ResultCodeEnum.HOSPITAL_LOCK);
        }

        String hosScheduleId = (String) paramMap.get("hosScheduleId");

        scheduleService.remove(hoscode, hosScheduleId);
        return Result.ok();
    }
}