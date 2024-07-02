package vn.nhannt.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.nhannt.jobhunter.domain.User;
import vn.nhannt.jobhunter.repository.UserRepository;

/**
 * Service for managing {@link com.mycompany.myapp.domain.User}.
 */

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        final User createdUser = this.userRepository.save(user);
        return createdUser;
    }

    public User update(User user) {
        User existingUser = this.findOne(user.getId());
        if (existingUser != null) {
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());

            final User updatedUser = this.userRepository.save(existingUser);
            return updatedUser;
        }
        return null;

    }

    public List<User> findAll() {
        final List<User> users = this.userRepository.findAll();
        return users;
    }

    public User findOne(Long id) {
        final Optional<User> optionalUsers = this.userRepository.findById(id);
        if (optionalUsers.isPresent()) {
            return optionalUsers.get();
        }
        return null;
    }

    public void delete(Long id) {
        User existingUser = this.findOne(id);
        if (existingUser != null) {
            this.userRepository.deleteById(id);
        }
    }

}
