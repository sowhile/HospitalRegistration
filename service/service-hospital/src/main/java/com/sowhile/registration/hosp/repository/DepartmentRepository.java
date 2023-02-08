package com.sowhile.registration.hosp.repository;

import com.sowhile.registration.model.hosp.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author sowhile
 * <p>
 */
@Repository
public interface DepartmentRepository extends MongoRepository<Department, String> {

    Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode);
}
