package vn.nhannt.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.nhannt.jobhunter.domain.entity.Permission;
import vn.nhannt.jobhunter.domain.response.ResPaginationDTO;
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
        final Permission currentPermission = this.findPermission(reqPermission.getId());
        // check exist
        this.isExistPermission(reqPermission);
        // update partial
        currentPermission.setName(reqPermission.getName());
        currentPermission.setApiPath(reqPermission.getApiPath());
        currentPermission.setMethod(reqPermission.getMethod());
        currentPermission.setModule(reqPermission.getModule());
        return this.permissionRepository.save(currentPermission);
    }

    public Permission findPermission(Long id) {
        final Optional<Permission> optPermission = this.permissionRepository.findById(id);
        if (optPermission.isEmpty()) {
            throw new IllegalArgumentException("Permission is not found with " + id);
        }
        return optPermission.get();
    }

    public ResPaginationDTO findPermissions(Specification<Permission> spec, Pageable pageable) {
        final Page<Permission> page = this.permissionRepository.findAll(spec, pageable);

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
    public boolean isExistPermission(Permission permission) throws UniqueException {
        if (this.permissionRepository.existsByApiPathAndMethodAndModule(
                permission.getApiPath(),
                permission.getMethod(),
                permission.getModule())) {
            throw new UniqueException("Permission already exists");
        }
        return false;
    }

    public List<Permission> findPermissionsByIds(List<Long> ids) {
        return this.permissionRepository.findAllById(ids);
    }

}
