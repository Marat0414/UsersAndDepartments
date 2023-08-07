package com.maratmuradaliev.dev.demo.backend.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maratmuradaliev.dev.demo.backend.entity.User;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository
        extends JpaRepository<User, Long> {

    @Transactional
    void deleteByDepartmentId(Long departmentId);


    List<User> findByDepartmentId(Long departmentId);

    @Query("SELECT u FROM User u WHERE u.department.name = :departmentName")
    List<User> findByDepartmentName(@Param("departmentName")String departmentName);
}