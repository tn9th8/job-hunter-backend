package vn.nhannt.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.nhannt.jobhunter.domain.dto.Meta;
import vn.nhannt.jobhunter.domain.dto.ResCreationUserDTO;
import vn.nhannt.jobhunter.domain.dto.ResPaginationDTO;
import vn.nhannt.jobhunter.domain.dto.ResUpdateUserDTO;
import vn.nhannt.jobhunter.domain.dto.ResUserDTO;
import vn.nhannt.jobhunter.domain.entity.User;
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

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User save(User user) throws UniqueException {
        // check exists By Email. Return true là chưa tồn tại
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UniqueException("Đã tồn tại một User với Email " + user.getEmail());
        }
        // hash Password
        String hashPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);

        return this.userRepository.save(user);
    }

    public User update(User reqUser) throws UniqueException {
        User existUser = this.findOne(reqUser.getId());
        existUser.setName(reqUser.getName());
        existUser.setAge(reqUser.getAge());
        existUser.setGender(reqUser.getGender());
        existUser.setAddress(reqUser.getAddress());

        return this.userRepository.save(existUser);

    }

    public ResPaginationDTO findAll(Specification<User> spec, Pageable pageable) {
        final Page<User> pUsers = this.userRepository.findAll(spec, pageable);

        final Meta meta = new Meta();
        meta.setPage(pUsers.getNumber() + 1);
        meta.setPageSize(pUsers.getSize());
        meta.setPages(pUsers.getTotalPages());
        meta.setTotal(pUsers.getTotalElements());

        List<ResUserDTO> result = pUsers.getContent().stream()
                .map(user -> this.convertToResUserDTO(user))
                .collect(Collectors.toList());

        final ResPaginationDTO resPaginationDTO = new ResPaginationDTO();
        resPaginationDTO.setMeta(meta);
        resPaginationDTO.setResult(result);

        return resPaginationDTO;
    }

    public User findOne(Long id) throws UniqueException {
        final Optional<User> optionalUser = this.userRepository.findById(id);
        if (optionalUser.isPresent() == false) {
            throw new UniqueException("Không tồn tại User với Id là " + id);
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

    public ResCreationUserDTO convertToResCreationUserDTO(User user) {
        final ResCreationUserDTO userDTO = new ResCreationUserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setAge(user.getAge());
        userDTO.setGender(user.getGender());
        userDTO.setAddress(user.getAddress());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setCreatedBy(user.getCreatedBy());
        return userDTO;
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        final ResUpdateUserDTO userDTO = new ResUpdateUserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setAge(user.getAge());
        userDTO.setGender(user.getGender());
        userDTO.setAddress(user.getAddress());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        userDTO.setUpdatedBy(user.getUpdatedBy());
        return userDTO;
    }

    public ResUserDTO convertToResUserDTO(User user) {
        final ResUserDTO userDTO = new ResUserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setAge(user.getAge());
        userDTO.setGender(user.getGender());
        userDTO.setAddress(user.getAddress());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setCreatedBy(user.getCreatedBy());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        userDTO.setUpdatedBy(user.getUpdatedBy());
        return userDTO;
    }

}
