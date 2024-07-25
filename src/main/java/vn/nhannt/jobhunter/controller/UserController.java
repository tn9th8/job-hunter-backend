package vn.nhannt.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.nhannt.jobhunter.domain.entity.User;
import vn.nhannt.jobhunter.domain.response.ResCreationUserDTO;
import vn.nhannt.jobhunter.domain.response.ResPaginationDTO;
import vn.nhannt.jobhunter.domain.response.ResUpdateUserDTO;
import vn.nhannt.jobhunter.domain.response.ResUserDTO;
import vn.nhannt.jobhunter.service.UserService;
import vn.nhannt.jobhunter.util.annotation.ApiMessage;
import vn.nhannt.jobhunter.util.error.UniqueException;

/**
 * REST controller for managing {@link vn.nhannt.jobhunter.domain.entity.User}.
 */

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * TO DO
     * 
     * @param user
     * @return
     * @throws UniqueException to handle
     */
    @PostMapping("/users")
    @ApiMessage("Create a user")
    public ResponseEntity<ResCreationUserDTO> createUser(@Valid @RequestBody User reqUser) throws UniqueException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.userService.convertToResCreationUserDTO(userService.create(reqUser)));
    }

    @PatchMapping("users")
    @ApiMessage("Update a user")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User reqUser) throws UniqueException {
        return ResponseEntity
                .ok()
                .body(this.userService.convertToResUpdateUserDTO(this.userService.update(reqUser)));
    }

    @GetMapping("/users")
    @ApiMessage("Fetch all users")
    public ResponseEntity<ResPaginationDTO> fetchAllUsers(
            @Filter Specification<User> spec,
            Pageable pageable) {
        return ResponseEntity.ok(this.userService.findAll(spec, pageable));
    }

    @GetMapping("users/{id}")
    @ApiMessage("Fetch one user")
    public ResponseEntity<ResUserDTO> fetchOneUser(@PathVariable("id") String sId)
            throws UniqueException {
        // TO DO: đưa logic vào service
        try {
            Long id = Long.valueOf(sId);
            return ResponseEntity.ok(
                    this.userService.convertToResUserDTO(
                            this.userService.findOne(id)));

        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(
                    "Can not convert a id PathVariable from String to Long");
        }
    }

    // TO DO: dùng .ok()
    @DeleteMapping("users/{id}")
    @ApiMessage("Delete a user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String sId)
            throws UniqueException {
        // TO DO: đưa logic vào service
        try {
            Long id = Long.valueOf(sId);
            this.userService.delete(id);
            return ResponseEntity.noContent().build();

        } catch (NumberFormatException nfe) {
            throw new NumberFormatException("Can not convert a id PathVariable from String to Long");
        }
    }
}