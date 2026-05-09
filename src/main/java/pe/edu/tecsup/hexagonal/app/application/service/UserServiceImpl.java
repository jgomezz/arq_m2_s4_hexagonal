package pe.edu.tecsup.hexagonal.app.application.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.tecsup.hexagonal.app.application.port.input.UserService;
import pe.edu.tecsup.hexagonal.app.application.port.output.UserRepositoryPort;
import pe.edu.tecsup.hexagonal.app.domain.exception.InvalidUserDataException;
import pe.edu.tecsup.hexagonal.app.domain.exception.UserNotFoundException;
import pe.edu.tecsup.hexagonal.app.domain.model.User;

import java.util.List;

//@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public User createUser(User newUser) {

        if (newUser == null) {
            throw new InvalidUserDataException("User cannot be null");
        }
        // Domain validation
        validateUserInput(newUser);

        // Business rule: Check if email already exists
        if (userRepositoryPort.existsByEmail(newUser.getEmail())) {
            throw new InvalidUserDataException("Email already exists: " + newUser.getEmail());
        }

        return userRepositoryPort.save(newUser);
    }

    @Override
    @Transactional(readOnly = true)
    public User findUser(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }
        return userRepositoryPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        log.info("Finding all users");
        List<User> users = userRepositoryPort.findAll();
        log.info("Found {} users", users.size());
        return users;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findUsersByName(String name) {
        log.info("Finding users by name containing: {}", name);

        if (name == null || name.trim().isEmpty()) {
            throw new InvalidUserDataException("Search name cannot be empty");
        }

        List<User> users = userRepositoryPort.findByNameContaining(name.trim());
        log.info("Found {} users with name containing '{}'", users.size(), name);
        return users;
    }

    @Override
    @Transactional
    public User updateUser(Long id, User user) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }

        User existingUser = userRepositoryPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        validateUserInput(user);

        // Check if email is being changed and if new email already exists
        if (!existingUser.getEmail().equals(user.getEmail()) &&
                userRepositoryPort.existsByEmail(user.getEmail())) {
            throw new InvalidUserDataException("Email already exists: " + user.getEmail());
        }
        existingUser.updateDetails(user.getName(), user.getEmail());
        return userRepositoryPort.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }

        if (!userRepositoryPort.existsById(id)) {
            throw new UserNotFoundException(id);
        }

        userRepositoryPort.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return userRepositoryPort.existsByEmail(email);
    }


    private void validateUserInput(User user) {

        if (!user.hasValidName()) {
            throw new InvalidUserDataException("Name must be at least 2 characters long");
        }

        if (!user.hasValidEmail()) {
            throw new InvalidUserDataException("Invalid email format");
        }
    }

}
