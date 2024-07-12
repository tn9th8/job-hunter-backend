package vn.nhannt.jobhunter.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.nhannt.jobhunter.domain.User;
import vn.nhannt.jobhunter.service.UserService;
import vn.nhannt.jobhunter.util.error.IdInvalidException;

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
    public ResponseEntity<User> createUser(@RequestBody User user) {
        final User createdUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(createdUser);
    }

    @PatchMapping("users")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User updatedUser = this.userService.update(user);
        return ResponseEntity.ok().body(updatedUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> fetchAllUsers() {
        List<User> users = this.userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("users/{id}")
    public ResponseEntity<User> fetchUser(@PathVariable("id") Long id) {
        User user = this.userService.findOne(id);
        return ResponseEntity.ok(user);
    }

    /**
     * {@code DELETE  /users/:id} : delete the "id" user.
     *
     * @param id the id of the user to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id)
            throws IdInvalidException {
        // TO DO: đưa logic vào service
        try {
            Long validId = Long.valueOf(id);

            if (validId > 100) {
                throw new IdInvalidException("id is not bigger than 100");
            }

            this.userService.delete(validId);
            return ResponseEntity.noContent().build();

        } catch (NumberFormatException nfe) {
            throw new NumberFormatException("Can not convert a id PathVariable from String to Long");
        }

    }
}