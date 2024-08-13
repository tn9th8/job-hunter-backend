package vn.nhannt.jobhunter.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.nhannt.jobhunter.domain.entity.Permission;
import vn.nhannt.jobhunter.repository.PermissionRepository;
import vn.nhannt.jobhunter.util.error.UniqueException;

@Service
public class PermissionService {

    final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Permission createPermission(Permission reqPermission) throws UniqueException {
        // check exist
        this.isExistPermission(reqPermission);
        // create
        return this.permissionRepository.save(reqPermission);
    }

    public Permission updatePermission(Permission reqPermission) throws UniqueException {
        // check id
        final Permission currentPermission = this.findPermissionOrExcept(reqPermission.getId());
        // check exist
        this.isExistPermission(reqPermission);
        // update partial
        currentPermission.setName(reqPermission.getName());
        currentPermission.setApiPath(reqPermission.getApiPath());
        currentPermission.setMethod(reqPermission.getMethod());
        currentPermission.setModule(reqPermission.getModule());
        return this.permissionRepository.save(currentPermission);
    }

    public Permission findPermissionOrExcept(Long id) {
        final Optional<Permission> optPermission = this.permissionRepository.findById(id);
        if (optPermission.isEmpty()) {
            throw new IllegalArgumentException("Permission is not found with " + id);
        }
        return optPermission.get();
    }

    // others
    public boolean isExistPermission(Permission permission) throws UniqueException {
        if (this.permissionRepository.existsByApiPathAndMethodAndModule(
                permission.getApiPath(),
                permission.getMethod(),
                permission.getModule())) {
            throw new UniqueException("Permission already exists");
        }
        return false;
    }

}
