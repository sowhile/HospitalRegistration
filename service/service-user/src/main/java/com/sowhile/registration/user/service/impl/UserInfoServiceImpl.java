package com.sowhile.registration.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sowhile.registration.common.exception.RegistrationException;
import com.sowhile.registration.common.helper.JwtHelper;
import com.sowhile.registration.common.result.ResultCodeEnum;
import com.sowhile.registration.model.user.UserInfo;
import com.sowhile.registration.user.mapper.UserInfoMapper;
import com.sowhile.registration.user.service.UserInfoService;
import com.sowhile.registration.vo.user.LoginVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Override
    public Map<String, Object> loginUser(LoginVo loginVo) {
        //从loginVo获取输入的手机号和验证码
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();
        //判断手机号和验证码是否为空
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code))
            throw new RegistrationException(ResultCodeEnum.PARAM_ERROR);
        //判断手机验证码和输入的验证码是否一致
        //TODO

        //判断是否为第一次登录
        QueryWrapper<UserInfo> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("phone", phone);
        UserInfo userInfo = baseMapper.selectOne(userQueryWrapper);
        if (userInfo == null) {
            //第一次使用这个手机号
            userInfo = new UserInfo();
            userInfo.setName("");
            userInfo.setPhone(phone);
            userInfo.setStatus(1);
            this.save(userInfo);
        }
        //校验用户是否被禁用
        if (userInfo.getStatus() == 0) {
            throw new RegistrationException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }

        //返回登录信息
        Map<String, Object> map = new HashMap<>();
        String name = userInfo.getName();
        if (StringUtils.isEmpty(name))
            name = userInfo.getNickName();
        if (StringUtils.isEmpty(name))
            name = userInfo.getPhone();
        map.put("name", name);
        //token生成
        String token = JwtHelper.createToken(userInfo.getId(), userInfo.getName());
        map.put("token", token);
        return map;
    }
}
