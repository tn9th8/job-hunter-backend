package vn.nhannt.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.nhannt.jobhunter.domain.entity.Permission;
import vn.nhannt.jobhunter.domain.response.ResPaginationDTO;
import vn.nhannt.jobhunter.service.PermissionService;
import vn.nhannt.jobhunter.util.annotation.ApiMessage;
import vn.nhannt.jobhunter.util.error.UniqueException;

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

    @ApiMessage("Update a permission")
    @PatchMapping("/permissions")
    public ResponseEntity<Permission> update(@Valid @RequestBody Permission reqPermission) throws UniqueException {
        return ResponseEntity
                .ok(this.permissionService.updatePermission(reqPermission));
    }

    @ApiMessage("Fetch a permission")
    @GetMapping("/permissions/{id}")
    public ResponseEntity<Permission> fetchOne(@PathVariable("id") Long id) {
        return ResponseEntity
                .ok(this.permissionService.findPermission(id));
    }

    @ApiMessage("Fetch all permission")
    @GetMapping("/permissions")
    public ResponseEntity<ResPaginationDTO> fetchAll(
            @Filter Specification<Permission> spec,
            Pageable pageable) {
        return ResponseEntity
                .ok()
                .body(this.permissionService.findPermissions(spec, pageable));
    }

}
