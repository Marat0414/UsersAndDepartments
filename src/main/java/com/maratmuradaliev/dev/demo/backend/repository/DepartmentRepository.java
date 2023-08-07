package com.maratmuradaliev.dev.demo.backend.repository;

import com.maratmuradaliev.dev.demo.backend.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department,Long> {


    Optional<Department> findByName(String departmentName);
}
