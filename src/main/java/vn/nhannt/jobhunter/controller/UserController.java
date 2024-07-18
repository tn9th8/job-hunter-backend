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

import vn.nhannt.jobhunter.domain.dto.ResCreationUserDTO;
import vn.nhannt.jobhunter.domain.dto.ResPaginationDTO;
import vn.nhannt.jobhunter.domain.dto.ResUpdateUserDTO;
import vn.nhannt.jobhunter.domain.dto.ResUserDTO;
import vn.nhannt.jobhunter.domain.entity.User;
import vn.nhannt.jobhunter.service.UserService;
import vn.nhannt.jobhunter.util.annotation.ApiMessage;
import vn.nhannt.jobhunter.util.error.IdInvalidException;
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
    public ResponseEntity<ResCreationUserDTO> createUser(@RequestBody User reqUser) throws UniqueException {
        final User user = userService.save(reqUser);
        final ResCreationUserDTO userDTO = new ResCreationUserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setAge(user.getAge());
        userDTO.setGender(user.getGender());
        userDTO.setAddress(user.getAddress());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setCreatedBy(user.getCreatedBy());
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @PatchMapping("users")
    @ApiMessage("Update a user")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User reqUser) throws UniqueException {
        User user = this.userService.update(reqUser);
        final ResUpdateUserDTO userDTO = new ResUpdateUserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setAge(user.getAge());
        userDTO.setGender(user.getGender());
        userDTO.setAddress(user.getAddress());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        userDTO.setUpdatedBy(user.getUpdatedBy());
        return ResponseEntity.ok().body(userDTO);
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
    public ResponseEntity<ResUserDTO> fetchUser(@PathVariable("id") Long id) throws UniqueException {
        User user = this.userService.findOne(id);
        final ResUserDTO userDTO = new ResUserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setAge(user.getAge());
        userDTO.setGender(user.getGender());
        userDTO.setAddress(user.getAddress());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setCreatedBy(user.getCreatedBy());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        userDTO.setUpdatedBy(user.getUpdatedBy());
        return ResponseEntity.ok(userDTO);
    }

    /**
     * {@code DELETE  /users/:id} : delete the "id" user.
     *
     * @param id the id of the user to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("users/{id}")
    @ApiMessage("Delete a user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id)
            throws IdInvalidException, UniqueException {
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