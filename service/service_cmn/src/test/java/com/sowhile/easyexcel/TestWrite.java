package com.sowhile.easyexcel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;

public class TestWrite {
    public static void main(String[] args) {
        ArrayList<UserData> userData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserData data = new UserData();
            data.setUid(i);
            data.setUserName("lucy" + i);
            userData.add(data);
        }
        //设置excel文件路径
        String fileName = "D:\\excel\\1.xlsx";
        EasyExcel.write(fileName, UserData.class).sheet("用户信息")
                .doWrite(userData);
    }
}
