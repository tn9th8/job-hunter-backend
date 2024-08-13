package vn.nhannt.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.nhannt.jobhunter.domain.entity.Role;
import vn.nhannt.jobhunter.service.RoleService;
import vn.nhannt.jobhunter.util.annotation.ApiMessage;
import vn.nhannt.jobhunter.util.error.UniqueException;

@RestController
@RequestMapping("/api/v1")
public class RoleController {

    final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @ApiMessage("Create a permission")
    @PostMapping("/roles")
    public ResponseEntity<Role> create(@Valid @RequestBody Role reqRole)
            throws UniqueException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.roleService.createRole(reqRole));
    }

    @ApiMessage("Update a permission")
    @PatchMapping("/roles")
    public ResponseEntity<Role> update(@Valid @RequestBody Role reqRole) throws UniqueException {
        return ResponseEntity.ok()
                .body(this.roleService.updateRole(reqRole));
    }

}
