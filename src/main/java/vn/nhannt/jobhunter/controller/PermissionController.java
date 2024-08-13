package vn.nhannt.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.nhannt.jobhunter.domain.entity.Permission;
import vn.nhannt.jobhunter.service.PermissionService;
import vn.nhannt.jobhunter.util.annotation.ApiMessage;
import vn.nhannt.jobhunter.util.error.UniqueException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {

    final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @ApiMessage("Create a permission")
    @PostMapping("/permissions")
    public ResponseEntity<Permission> create(@Valid @RequestBody Permission reqPermission)
            throws UniqueException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.permissionService.createPermission(reqPermission));
    }

}
