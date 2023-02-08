package com.sowhile.registration.hosp.controller.api;

import com.sowhile.registration.common.exception.RegistrationException;
import com.sowhile.registration.common.helper.HttpRequestHelper;
import com.sowhile.registration.common.result.Result;
import com.sowhile.registration.common.result.ResultCodeEnum;
import com.sowhile.registration.common.util.MD5;
import com.sowhile.registration.hosp.service.HospitalService;
import com.sowhile.registration.hosp.service.HospitalSetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api
@RestController
@RequestMapping("/api/hosp")
public class ApiController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalSetService hospitalSetService;

    @ApiOperation("上传医院接口")
    @PostMapping("saveHospital")
    public Result saveHosp(HttpServletRequest request) {
        //获取传递过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> switchMap = HttpRequestHelper.switchMap(requestMap);
        //1.获取传递过来的签名(MD5加密)
        String sign = (String) switchMap.get("sign");
        //2.查询数据库
        String hoscode = (String) switchMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        //3.把查出来的签名进行MD5加密
        String encrypt = MD5.encrypt(signKey);
        if (StringUtils.isEmpty(hoscode)) {
            throw new RegistrationException(ResultCodeEnum.PARAM_ERROR);
        }
        if (!sign.equals(encrypt)) {
            throw new RegistrationException(ResultCodeEnum.SIGN_ERROR);
        }

        //传输过程中'+'转换为了' ',所以要转换回来
        String logoData = (String) switchMap.get("logoData");
        String replaceAll = logoData.replaceAll(" ", "+");
        switchMap.put("logoData", replaceAll);
        //调用service方法
        hospitalService.save(switchMap);
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

}
