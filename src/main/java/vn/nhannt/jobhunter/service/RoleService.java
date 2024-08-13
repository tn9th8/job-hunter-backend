package vn.nhannt.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import vn.nhannt.jobhunter.domain.entity.Permission;
import vn.nhannt.jobhunter.domain.entity.Role;
import vn.nhannt.jobhunter.domain.response.ResPaginationDTO;
import vn.nhannt.jobhunter.repository.RoleRepository;
import vn.nhannt.jobhunter.util.error.UniqueException;

@Service
public class RoleService {

    final RoleRepository roleRepository;
    final PermissionService permissionService;

    public RoleService(
            RoleRepository roleRepository,
            PermissionService permissionService) {
        this.roleRepository = roleRepository;
        this.permissionService = permissionService;
    }

    public Role createRole(@Valid Role reqRole) throws UniqueException {
        // check name
        this.isExistName(reqRole.getName());
        // check permissions
        if (reqRole.getPermissions() != null) {
            final List<Long> permissionIds = reqRole.getPermissions().stream()
                    .map(permission -> permission.getId())
                    .collect(Collectors.toList());
            List<Permission> fullPermissions = this.permissionService.findPermissionsByIds(permissionIds);
            reqRole.setPermissions(fullPermissions);
        }
        // create
        return this.roleRepository.save(reqRole);
    }

    public Role updateRole(Role reqRole) throws UniqueException {
        // check id
        final Role currentRole = this.findRoleById(reqRole.getId());
        // check name
        this.isExistNameAndNotId(reqRole.getName(), reqRole.getId());
        // check permissions
        if (reqRole.getPermissions() != null) {
            final List<Long> permissionIds = reqRole.getPermissions().stream()
                    .map(permission -> permission.getId())
                    .collect(Collectors.toList());
            List<Permission> fullPermissions = this.permissionService.findPermissionsByIds(permissionIds);
            currentRole.setPermissions(fullPermissions);
        }
        // update partial
        currentRole.setName(reqRole.getName());
        currentRole.setDescription(reqRole.getDescription());
        currentRole.setActive(reqRole.isActive());
        return this.roleRepository.save(currentRole);
    }

    public void deleteById(Long id) {
        // check id, then delete
        this.findRoleById(id);
        this.roleRepository.deleteById(id);
    }

    public Role findRoleById(Long id) {
        final Optional<Role> optRole = this.roleRepository.findById(id);
        if (optRole.isEmpty()) {
            throw new IllegalArgumentException("Role id " + id + " is not found");
        }
        return optRole.get();
    }

    public ResPaginationDTO findRoles(Specification<Role> spec, Pageable pageable) {
        final Page<Role> page = this.roleRepository.findAll(spec, pageable);

        final ResPaginationDTO resPagination = new ResPaginationDTO();
        resPagination.setResult(page.getContent());

        final ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();
        meta.setPage(page.getNumber() + 1);
        meta.setPageSize(page.getSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());
        resPagination.setMeta(meta);

        return resPagination;
    }

    // others
    public boolean isExistName(String name) throws UniqueException {
        if (this.roleRepository.existsByName(name)) {
            throw new UniqueException("Role name " + name + " is exist");
        }
        return false;
    }

    public boolean isExistNameAndNotId(String name, Long id) throws UniqueException {
        if (this.roleRepository.existsByIdNotAndName(id, name)) {
            throw new UniqueException("Role name " + name + " is exist");
        }
        return false;
    }

}
