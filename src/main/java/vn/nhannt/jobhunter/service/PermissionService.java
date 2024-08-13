package vn.nhannt.jobhunter.service;

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
