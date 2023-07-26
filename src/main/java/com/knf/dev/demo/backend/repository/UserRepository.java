package com.knf.dev.demo.backend.repository;

import com.knf.dev.demo.backend.entity.Department;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.knf.dev.demo.backend.entity.User;

import java.util.List;

public interface UserRepository
        extends JpaRepository<User, Long> {

    @Transactional
    void deleteByDepartmentId(Long departmentId);

    List<User> findByDepartmentId(Long departmentId);
}