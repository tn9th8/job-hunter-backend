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
import vn.nhannt.jobhunter.domain.entity.Role;
import vn.nhannt.jobhunter.domain.response.ResPaginationDTO;
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

    @ApiMessage("Create a role")
    @PostMapping("/roles")
    public ResponseEntity<Role> create(@Valid @RequestBody Role reqRole)
            throws UniqueException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.roleService.createRole(reqRole));
    }

    @ApiMessage("Update a role")
    @PatchMapping("/roles")
    public ResponseEntity<Role> update(@Valid @RequestBody Role reqRole) throws UniqueException {
        return ResponseEntity.ok()
                .body(this.roleService.updateRole(reqRole));
    }

    @ApiMessage("Delete a role")
    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        this.roleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @ApiMessage("Get a role")
    @GetMapping("/roles/{id}")
    public ResponseEntity<Role> fetchOne(@PathVariable("id") Long id) {
        return ResponseEntity.ok()
                .body(this.roleService.findRoleById(id));
    }

    @ApiMessage("Get all role")
    @GetMapping("/roles")
    public ResponseEntity<ResPaginationDTO> fetchAll(
            @Filter Specification<Role> spec,
            Pageable pageable) {
        return ResponseEntity
                .ok()
                .body(this.roleService.findRoles(spec, pageable));
    }

}
