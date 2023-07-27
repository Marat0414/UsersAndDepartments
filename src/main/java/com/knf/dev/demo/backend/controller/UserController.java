package com.knf.dev.demo.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.knf.dev.demo.backend.controller.dto.UserDto;
import com.knf.dev.demo.backend.entity.Department;
import com.knf.dev.demo.backend.exception.ResourceNotFoundException;
import com.knf.dev.demo.backend.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.knf.dev.demo.backend.entity.User;
import com.knf.dev.demo.backend.exception.InternalServerError;

import com.knf.dev.demo.backend.repository.UserRepository;

@RestController
@RequestMapping("/api/v1/users")

public class UserController {


    private final UserRepository userRepository;

    private final DepartmentRepository departmentRepository;

    @Autowired
    public UserController(UserRepository userRepository, DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    // Create user - have to specify  the name of user's department
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {

        Department department = departmentRepository.findByName(userDto.getDepartmentName())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with Name: " + userDto.getDepartmentName()));

        User newUser = new User();
        newUser.setFirstName(userDto.getFirstName());
        newUser.setLastName(userDto.getLastName());
        newUser.setEmail(userDto.getEmail());
        newUser.setSalary(userDto.getSalary());
        newUser.setDepartment(department);

        User createdUser = userRepository.save(newUser);

        // Set the department name on the created user
        createdUser.getDepartment().setName(department.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // Update user - have to specify  the name of user's department
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));


        Department department = departmentRepository.findByName(userDto.getDepartmentName())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with Name: " + userDto.getDepartmentName()));

        existingUser.setFirstName(userDto.getFirstName());
        existingUser.setLastName(userDto.getLastName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setSalary(userDto.getSalary());
        existingUser.setDepartment(department);

        User updatedUser = userRepository.save(existingUser);

        // Set the department name on the updated user
        updatedUser.getDepartment().setName(department.getName());

        return ResponseEntity.ok(updatedUser);
    }

    // Get all Users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {

        try {
            List<User> users = new ArrayList<User>();
            userRepository.findAll().forEach(users::add);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }

    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserByID(@PathVariable("id") Long id) {

        Optional<User> userdata = userRepository.findById(id);
        if (userdata.isPresent()) {
            return new ResponseEntity<>(userdata.get(), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Invalid User Id");
        }

    }


    // Delete user by  id
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") Long id) {

        Optional<User> userdata = userRepository.findById(id);
        if (userdata.isPresent()) {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new ResourceNotFoundException("Invalid User Id");
        }
    }
}
