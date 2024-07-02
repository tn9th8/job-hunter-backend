package vn.nhannt.jobhunter.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.nhannt.jobhunter.domain.User;
import vn.nhannt.jobhunter.service.UserService;
import vn.nhannt.jobhunter.service.error.IdInvalidException;

/**
 * REST controller for managing {@link vn.nhannt.jobhunter.domain.User}.
 */

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        final User createdUser = userService.save(user);
        return createdUser;
    }

    @PatchMapping("users")
    public User updateUser(@RequestBody User user) {
        User updatedUser = this.userService.update(user);
        return updatedUser;
    }

    @GetMapping("/users")
    public List<User> fetchAllUsers() {
        List<User> users = this.userService.findAll();
        return users;
    }

    @GetMapping("users/{id}")
    public User fetchUser(@PathVariable("id") Long id) {
        User users = this.userService.findOne(id);
        return users;
    }

    @DeleteMapping("users/{id}")
    public void deleteUser(@PathVariable("id") Long id) throws IdInvalidException {
        if (id > 10) {
            throw new IdInvalidException("Id is smaller than 100");
        }

        this.userService.delete(id);
    }
}