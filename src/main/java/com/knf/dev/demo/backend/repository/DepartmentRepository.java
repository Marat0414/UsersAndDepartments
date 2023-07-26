package com.knf.dev.demo.backend.repository;

import com.knf.dev.demo.backend.entity.Department;
import com.knf.dev.demo.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department,Long> {


    Optional<Department> findByName(String departmentName);
}
