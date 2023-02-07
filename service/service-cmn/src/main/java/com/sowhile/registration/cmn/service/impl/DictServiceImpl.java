package com.sowhile.registration.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sowhile.registration.cmn.listener.Dictlistener;
import com.sowhile.registration.cmn.mapper.DictMapper;
import com.sowhile.registration.cmn.service.DictService;
import com.sowhile.registration.common.util.BeanUtils;
import com.sowhile.registration.model.cmn.Dict;
import com.sowhile.registration.vo.cmn.DictEeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict>
        implements DictService {

    @Autowired
    private DictMapper dictMapper;

    //根据数据id查询子数据列表
    @Override
    @Cacheable(value = "dict", keyGenerator = "keyGenerator")
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("parent_id", id);
        List<Dict> dictList = baseMapper.selectList(dictQueryWrapper);
        //向List中每个Dict对象中设置hasChildren值
        for (Dict dict : dictList) {
            Long dictId = dict.getId();
            dict.setHasChildren(this.isChildren(dictId));
        }
        return dictList;
    }

    //导出数据字典
    @Override
    public void exportDictData(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
//            String fileName = URLEncoder.encode("数据字典", "UTF-8");
            String fileName = "dict";
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            List<Dict> dictList = baseMapper.selectList(null);
            List<DictEeVo> dictVoList = new ArrayList<>(dictList.size());
            for (Dict dict : dictList) {
                DictEeVo dictVo = new DictEeVo();
                BeanUtils.copyBean(dict, dictVo, DictEeVo.class);
                dictVoList.add(dictVo);
            }

            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("dict").doWrite(dictVoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //导入数据字典
    @Override
    @CacheEvict(value = "dict", allEntries = true)
    public void importDictData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), DictEeVo.class, new Dictlistener(dictMapper)).sheet().doRead();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //判断id下面是否有子节点
    private boolean isChildren(Long id) {
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("parent_id", id);
        Integer selectCount = baseMapper.selectCount(dictQueryWrapper);
        return selectCount > 0;
    }
}
