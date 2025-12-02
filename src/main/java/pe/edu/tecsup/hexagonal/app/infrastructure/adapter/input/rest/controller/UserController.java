package pe.edu.tecsup.hexagonal.app.infrastructure.adapter.input.rest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import pe.edu.tecsup.hexagonal.app.application.port.input.CreateUserUseCase;
import pe.edu.tecsup.hexagonal.app.application.port.input.DeleteUserUseCase;
import pe.edu.tecsup.hexagonal.app.application.port.input.UpdateUserUseCase;
import pe.edu.tecsup.hexagonal.app.application.port.input.FindUserUseCase;
import pe.edu.tecsup.hexagonal.app.domain.exception.InvalidUserDataException;
import pe.edu.tecsup.hexagonal.app.domain.exception.UserNotFoundException;
import pe.edu.tecsup.hexagonal.app.domain.model.User;
import pe.edu.tecsup.hexagonal.app.infrastructure.adapter.input.rest.dto.UserRequest;
import pe.edu.tecsup.hexagonal.app.infrastructure.adapter.input.rest.dto.UserResponse;
import pe.edu.tecsup.hexagonal.app.infrastructure.adapter.output.persistence.mapper.UserMapper;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final FindUserUseCase userService;
    private final CreateUserUseCase createUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;

    private final UserMapper mapper;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
        try {
            log.info("Creating createUser with name: {} and email: {}", request.getName(), request.getEmail());

            User newUser = this.mapper.toDomain(request);
            User createUser = this.createUserUseCase.execute(newUser);

            if (createUser == null) {
                log.warn("User service returned null");
                return ResponseEntity.badRequest().build();
            }

            log.info("User created successfully with ID: {}", createUser.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(this.mapper.toResponse(createUser));


        } catch (InvalidUserDataException e) {
            log.warn("Invalid user creation request: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Unexpected error creating user", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        try {
            log.info("Fetching user with ID: {}", id);

            User user = this.userService.findUser(id);
            log.info("User found: {}", user.getName());

            return ResponseEntity.ok(this.mapper.toResponse(user));

        } catch (InvalidUserDataException e) {
            log.warn("Invalid user ID: {}", id);
            return ResponseEntity.badRequest().build();
        } catch (UserNotFoundException e) {
            log.warn("User not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        try {
            log.info("Fetching all users");

            List<User> users = this.userService.findAllUsers();
            List<UserResponse> responses = this.mapper.toResponse(users);

            log.info("Found {} users", responses.size());
            return ResponseEntity.ok(responses);

        } catch (Exception e) {
            log.error("Unexpected error fetching all users", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchUsersByName(@RequestParam String name) {
        try {
            log.info("Searching users by name: {}", name);

            List<User> users = this.userService.findUsersByName(name);
            List<UserResponse> responses = this.mapper.toResponse(users);

            log.info("Found {} users with name containing '{}'", responses.size(), name);
            return ResponseEntity.ok(responses);

        } catch (InvalidUserDataException e) {
            log.warn("Invalid search request: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Unexpected error searching users", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
        try {
            log.info("Updating user with ID: {} with data: name={}, email={}", id, request.getName(), request.getEmail());

            User user = mapper.toDomain(request);
            User updatedUser = updateUserUseCase.execute(id, user);

            if (updatedUser == null) {
                log.warn("User service returned null for update");
                return ResponseEntity.badRequest().build();
            }

            log.info("User updated successfully with ID: {}", updatedUser.getId());
            return ResponseEntity.ok(mapper.toResponse(updatedUser));

        } catch (InvalidUserDataException e) {
            log.warn("Invalid user update request: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (UserNotFoundException e) {
            log.warn("User not found for update with ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Unexpected error updating user", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            log.info("Deleting user with ID: {}", id);

            deleteUserUseCase.execute(id);

            log.info("User deleted successfully with ID: {}", id);
            return ResponseEntity.noContent().build();

        } catch (InvalidUserDataException e) {
            log.warn("Invalid user deletion request: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (UserNotFoundException e) {
            log.warn("User not found for deletion with ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Unexpected error deleting user", e);
            return ResponseEntity.internalServerError().build();
        }
    }

}