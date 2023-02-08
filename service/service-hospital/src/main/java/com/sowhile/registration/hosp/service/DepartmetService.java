package com.sowhile.registration.hosp.service;

import com.sowhile.registration.model.hosp.Department;
import com.sowhile.registration.vo.hosp.DepartmentQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * @author sowhile
 * <p>
 */
public interface DepartmetService {
    void save(Map<String, Object> paramMap);

    Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo);

    void remove(String hoscode, String depcode);
}
