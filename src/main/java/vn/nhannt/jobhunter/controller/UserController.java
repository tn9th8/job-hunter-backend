package vn.nhannt.jobhunter.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.nhannt.jobhunter.service.error.IdInvalidException;

/**
 * UserController
 */
@RestController
public class UserController {

    @GetMapping("/users")
    public String fetchUsers() {
        return new String("Fetch users thành");
    }

    @PostMapping("/users")
    public String createUser(@RequestBody String entity) {
        return new String("Create user thành công");
    }

    @PatchMapping("users")
    public String updateUser(@RequestBody String entity) {
        return new String("Update user thành công");
    }

    @DeleteMapping("users/{id}")
    public String deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        if (id < 100) {
            throw new IdInvalidException("Id is smaller than 100");
        }
        return new String("Delete user thành");
    }
}