package vn.nhannt.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.nhannt.jobhunter.domain.entity.User;

/**
 * Spring Data JPA repository for the User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndRefreshToken(String email, String refreshToken);

}
