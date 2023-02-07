package com.sowhile.registration.cmn.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.sowhile.registration.cmn.mapper.DictMapper;
import com.sowhile.registration.common.util.BeanUtils;
import com.sowhile.registration.model.cmn.Dict;
import com.sowhile.registration.vo.cmn.DictEeVo;

public class Dictlistener extends AnalysisEventListener<DictEeVo> {

    private DictMapper dictMapper;

    public Dictlistener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    //一行一行读
    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictEeVo, dict);
        dictMapper.insert(dict);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
