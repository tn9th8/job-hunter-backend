package vn.nhannt.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.nhannt.jobhunter.domain.entity.Permission;
import vn.nhannt.jobhunter.util.constant.ApiMethodEnum;
import vn.nhannt.jobhunter.util.constant.ModuleEnum;

@Repository
public interface PermissionRepository extends
                JpaRepository<Permission, Long>,
                JpaSpecificationExecutor<Permission> {

        boolean existsByApiPathAndMethodAndModule(String apiPath, ApiMethodEnum method, ModuleEnum module);
}
