package vn.nhannt.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.nhannt.jobhunter.domain.entity.Role;

@Repository
public interface RoleRepository extends
                JpaRepository<Role, Long>,
                JpaSpecificationExecutor<Role> {

        boolean existsByName(String name);

        Role findByName(String name);

        boolean existsByIdNotAndName(Long id, String name);
}
