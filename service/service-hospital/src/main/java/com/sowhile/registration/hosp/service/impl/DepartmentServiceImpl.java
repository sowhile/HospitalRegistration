package com.sowhile.registration.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sowhile.registration.hosp.repository.DepartmentRepository;
import com.sowhile.registration.hosp.service.DepartmentService;
import com.sowhile.registration.model.hosp.Department;
import com.sowhile.registration.vo.hosp.DepartmentQueryVo;
import com.sowhile.registration.vo.hosp.DepartmentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sowhile
 * <p>
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public void save(Map<String, Object> paramMap) {
        //paramMap转成Department对象
        String jsonString = JSONObject.toJSONString(paramMap);
        Department department = JSONObject.parseObject(jsonString, Department.class);

        Department departmentExist = departmentRepository.
                getDepartmentByHoscodeAndDepcode(department.getHoscode(), department.getHoscode());
        //存在department，进行修改操作
        if (departmentExist != null) {
            department.setId(departmentExist.getId());
            department.setCreateTime(departmentExist.getCreateTime());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
        } else {
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
        }
        departmentRepository.save(department);
    }

    @Override
    public Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo) {
        //创建Pageable对象，设置当前页和每页记录数
        //0是第一页
        Pageable pageable = PageRequest.of(page - 1, limit);
        //创建Example对象
        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo, department);
        department.setIsDeleted(0);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);

        Example<Department> example = Example.of(department, matcher);
        Page<Department> all = departmentRepository.findAll(example, pageable);
        return all;
    }

    @Override
    public void remove(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (department != null) {
            department.setIsDeleted(1);
            department.setUpdateTime(new Date());
            departmentRepository.save(department);
        }
    }

    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {
        //创建一个List集合，用于最终的数据封装
        List<DepartmentVo> result = new ArrayList<>();

        //根据医院的编号，查询所有科室信息
        Department departmentQuery = new Department();
        departmentQuery.setHoscode(hoscode);
        Example<Department> departmentExample = Example.of(departmentQuery);
        //departmentList是所有科室的列表信息
        List<Department> allDepartmentList = departmentRepository.findAll(departmentExample);

        //根据大科室编号bigCode分组,获取每个大科室里面下级子科室
        Map<String, List<Department>> departmentMap =
                allDepartmentList.stream().collect(Collectors.groupingBy(Department::getBigcode));
        for (Map.Entry<String, List<Department>> entry : departmentMap.entrySet()) {
            //大科室编号
            String bigCode = entry.getKey();
            //大科室编号对应的全部数据
            List<Department> departmentList1 = entry.getValue();

            //封装大科室
            DepartmentVo departmentVo = new DepartmentVo();
            departmentVo.setDepcode(bigCode);
            departmentVo.setDepname(departmentList1.get(0).getBigname());

            //封装小科室
            ArrayList<DepartmentVo> children = new ArrayList<>();
            for (Department department : departmentList1) {
                DepartmentVo departmentVo1 = new DepartmentVo();
                departmentVo1.setDepcode(department.getDepcode());
                departmentVo1.setDepname(department.getDepname());
                children.add(departmentVo1);
            }

            //把小科室的list集合放到大科室children里面
            departmentVo.setChildren(children);
            result.add(departmentVo);
        }
        return result;
    }

    @Override
    public String getDepName(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (department != null)
            return department.getDepname();
        else
            return null;
    }
}
