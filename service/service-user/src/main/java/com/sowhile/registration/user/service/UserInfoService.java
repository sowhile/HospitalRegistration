package com.sowhile.registration.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sowhile.registration.model.user.UserInfo;
import com.sowhile.registration.vo.user.LoginVo;
import com.sowhile.registration.vo.user.UserAuthVo;

import java.util.Map;

public interface UserInfoService extends IService<UserInfo> {
    Map<String, Object> loginUser(LoginVo loginVo);

    UserInfo getByOpenid(String openId);

    void userAuth(Long userId, UserAuthVo userAuthVo);
}
