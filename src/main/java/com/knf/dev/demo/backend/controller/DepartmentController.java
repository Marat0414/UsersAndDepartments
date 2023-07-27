package com.knf.dev.demo.backend.controller;

import com.knf.dev.demo.backend.controller.dto.DepartmentDto;
import com.knf.dev.demo.backend.entity.Department;
import com.knf.dev.demo.backend.entity.User;
import com.knf.dev.demo.backend.exception.InternalServerError;
import com.knf.dev.demo.backend.exception.ResourceNotFoundException;

import com.knf.dev.demo.backend.repository.DepartmentRepository;
import com.knf.dev.demo.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController {


    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentController(UserRepository userRepository, DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {

        try {
            List<Department> departments = new ArrayList<Department>();
            departmentRepository.findAll().forEach(departments::add);
            return new ResponseEntity<>(departments, HttpStatus.OK);
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> getDepartmentByID(@PathVariable("id") Long id) {
        Optional<Department> departmentData = departmentRepository.findById(id);
        if (departmentData.isPresent()) {
            Department department = departmentData.get();

            // Iterate through the users and create a simplified list of names and surnames
            List<String> userNames = department.getUsers().stream()
                    .map(user -> user.getFirstName() + " " + user.getLastName())
                    .collect(Collectors.toList());

            // Create a new DepartmentDto object and set the relevant data
            DepartmentDto departmentDto = new DepartmentDto();
            departmentDto.setId(department.getId());
            departmentDto.setName(department.getName());
            departmentDto.setUserNames(userNames);

            return new ResponseEntity<>(departmentDto, HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Invalid Department Id");
        }
    }


    @PostMapping
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        try {
            Department newDepartment = new Department(department.getName());


            departmentRepository.save(newDepartment);
            return new ResponseEntity<>(newDepartment, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable("id") Long id, @RequestBody Department department) {

        Optional<Department> departmentData = departmentRepository.findById(id);
        if (departmentData.isPresent()) {
            Department _department = departmentData.get();
            _department.setName(department.getName());


            return new ResponseEntity<>(departmentRepository.save(_department), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Invalid Department Id");
        }
    }

    // Method to get users by department ID
    @GetMapping("/{departmentId}/users")
    public ResponseEntity<List<User>> getUsersByDepartment(@PathVariable Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + departmentId));

        List<User> users = userRepository.findByDepartmentId(departmentId);
        return ResponseEntity.ok(users);
    }

    // Method to delete users by department ID
    @DeleteMapping("/{departmentId}/users")
    public ResponseEntity<Void> deleteUsersByDepartmentId(@PathVariable Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + departmentId));

        userRepository.deleteByDepartmentId(department.getId());

        departmentRepository.delete(department);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/name/{departmentName}/users")
    public ResponseEntity<Void> deleteUsersByDepartmentName(@PathVariable String departmentName) {
        try {
            List<User> usersToDelete = userRepository.findByDepartmentName(departmentName);
            if (usersToDelete.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            for (User user : usersToDelete) {
                user.setDepartment(null); // Disassociate the user from the department
                userRepository.delete(user);
            }
           Department department = departmentRepository.findByName(departmentName)
                   .orElseThrow(()-> new ResourceNotFoundException("Department not found with name: " + departmentName));
            departmentRepository.delete(department);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            // if department does not exist
            return ResponseEntity.notFound().build();
        }
    }
}