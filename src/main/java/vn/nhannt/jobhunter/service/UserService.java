package vn.nhannt.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.nhannt.jobhunter.domain.entity.Role;
import vn.nhannt.jobhunter.domain.entity.User;
import vn.nhannt.jobhunter.domain.response.ResCreationUserDTO;
import vn.nhannt.jobhunter.domain.response.ResPaginationDTO;
import vn.nhannt.jobhunter.domain.response.ResUpdateUserDTO;
import vn.nhannt.jobhunter.domain.response.ResUserDTO;
import vn.nhannt.jobhunter.repository.UserRepository;
import vn.nhannt.jobhunter.util.error.UniqueException;

/**
 * Service for managing
 * {@link vn.nhannt.jobhunter.domain.entity.mycompany.myapp.domain.User}.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final CompanyService companyService;

    private final RoleService roleService;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            CompanyService companyService,
            RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.companyService = companyService;
        this.roleService = roleService;
    }

    public User create(User user) throws UniqueException {
        // check the fk field
        if (user.getCompany() != null) {
            user.setCompany(this.companyService.findOne(user.getCompany().getId()));
        }
        if (user.getRole() != null) {
            Role role = this.roleService.findRoleById(user.getRole().getId());
            user.setRole(role != null ? role : null);
        }

        // check the email field. Return true là tồn tại
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UniqueException("Đã tồn tại User có Email " + user.getEmail());
        }
        // hash Password
        String hashPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);

        return this.userRepository.save(user);
    }

    public User update(User reqUser) throws UniqueException {
        User currentUser = this.findOne(reqUser.getId());
        currentUser.setName(reqUser.getName());
        currentUser.setAge(reqUser.getAge());
        currentUser.setGender(reqUser.getGender());
        currentUser.setAddress(reqUser.getAddress());
        // check the fk field
        if (currentUser.getCompany() != null) {
            currentUser.setCompany(
                    this.companyService.findOne(
                            currentUser.getCompany().getId()));
        }
        // check role
        if (reqUser.getRole() != null) {
            Role role = this.roleService.findRoleById(reqUser.getRole().getId());
            currentUser.setRole(role != null ? role : null);
        }

        return this.userRepository.save(currentUser);

    }

    public ResPaginationDTO findAll(Specification<User> spec, Pageable pageable) {
        final Page<User> pUsers = this.userRepository.findAll(spec, pageable);

        final ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();
        meta.setPage(pUsers.getNumber() + 1);
        meta.setPageSize(pUsers.getSize());
        meta.setPages(pUsers.getTotalPages());
        meta.setTotal(pUsers.getTotalElements());

        List<ResUserDTO> result = pUsers.getContent().stream()
                .map(user -> ResUserDTO.mapFrom(user))
                .collect(Collectors.toList());

        final ResPaginationDTO resPaginationDTO = new ResPaginationDTO();
        resPaginationDTO.setMeta(meta);
        resPaginationDTO.setResult(result);

        return resPaginationDTO;
    }

    public User findOne(Long id) {
        final Optional<User> optionalUser = this.userRepository.findById(id);
        if (optionalUser.isPresent() == false) {
            throw new IllegalArgumentException("User is not found with " + id);
        }
        return optionalUser.get();
    }

    public void delete(Long id) throws UniqueException {
        this.findOne(id);
        this.userRepository.deleteById(id);
    }

    // others
    public User findByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    public void updateRefreshToken(String username, String refreshToken) {
        User currentUser = this.findByUsername(username);
        if (currentUser != null) {
            currentUser.setRefreshToken(refreshToken);
        }
        this.userRepository.save(currentUser);
    }

    public boolean checkUserOwnsRefreshToken(String username, String refreshToken) throws UniqueException {
        if (this.userRepository.existsByEmailAndRefreshToken(username, refreshToken) == false) {
            throw new UniqueException("Người dùng " + username + " không có refreshToken hợp lệ");
        }
        return true;
    }

    // map
    public ResCreationUserDTO convertToResCreationUserDTO(User dbUser) {
        final ResCreationUserDTO resUser = new ResCreationUserDTO();
        resUser.setId(dbUser.getId());
        resUser.setName(dbUser.getName());
        resUser.setEmail(dbUser.getEmail());
        resUser.setAge(dbUser.getAge());
        resUser.setGender(dbUser.getGender());
        resUser.setAddress(dbUser.getAddress());
        resUser.setCreatedAt(dbUser.getCreatedAt());
        resUser.setCreatedBy(dbUser.getCreatedBy());
        // FK
        // TO DO role
        if (dbUser.getCompany() != null) {
            final ResCreationUserDTO.FkCompany fkCompany = new ResCreationUserDTO.FkCompany(
                    dbUser.getCompany().getId(),
                    dbUser.getCompany().getName());
            resUser.setCompany(fkCompany);
        }
        return resUser;
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User dbUser) {
        final ResUpdateUserDTO resUser = new ResUpdateUserDTO();
        resUser.setId(dbUser.getId());
        resUser.setName(dbUser.getName());
        resUser.setAge(dbUser.getAge());
        resUser.setGender(dbUser.getGender());
        resUser.setAddress(dbUser.getAddress());
        resUser.setUpdatedAt(dbUser.getUpdatedAt());
        resUser.setUpdatedBy(dbUser.getUpdatedBy());
        // FK
        // TO TO role
        if (dbUser.getCompany() != null) {
            final ResUpdateUserDTO.FkCompany fkCompany = new ResUpdateUserDTO.FkCompany(
                    dbUser.getCompany().getId(),
                    dbUser.getCompany().getName());
            resUser.setCompany(fkCompany);
        }
        return resUser;
    }

}
