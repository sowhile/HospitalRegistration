package com.sowhile.registration.cmn.controller;

import com.sowhile.registration.cmn.service.DictService;
import com.sowhile.registration.common.result.Result;
import com.sowhile.registration.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value = "数据字典的接口")
@RestController
@RequestMapping("/admin/cmn/dict")
@CrossOrigin
public class DictController {
    @Autowired
    private DictService dictService;

    @ApiOperation("根据数据id查询子数据列表")
    @GetMapping("findChildData/{id}")
    public Result findChildData(@PathVariable Long id) {
        List<Dict> dictList = dictService.findChildData(id);
        return Result.ok(dictList);
    }

    @ApiOperation("导出数据字典")
    @GetMapping("exportData")
    public void exportDict(HttpServletResponse response) {
        dictService.exportDictData(response);
    }

    @ApiOperation("导入数据字典")
    @PostMapping("importData")
    public Result importDict(MultipartFile file) {
        dictService.importDictData(file);
        return Result.ok();
    }
}
