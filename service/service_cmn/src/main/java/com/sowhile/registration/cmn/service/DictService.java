package com.sowhile.registration.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sowhile.registration.model.cmn.Dict;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DictService extends IService<Dict> {

    List<Dict> findChildData(Long id);

    void exportDictData(HttpServletResponse response);
}
