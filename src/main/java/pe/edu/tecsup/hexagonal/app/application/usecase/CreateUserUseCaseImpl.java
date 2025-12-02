package pe.edu.tecsup.hexagonal.app.application.usecase;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.tecsup.hexagonal.app.application.port.input.CreateUserUseCase;
import pe.edu.tecsup.hexagonal.app.application.port.input.DeleteUserUseCase;
import pe.edu.tecsup.hexagonal.app.application.port.input.UserUseCase;
import pe.edu.tecsup.hexagonal.app.application.port.output.UserRepositoryPort;
import pe.edu.tecsup.hexagonal.app.domain.exception.InvalidUserDataException;
import pe.edu.tecsup.hexagonal.app.domain.exception.UserNotFoundException;
import pe.edu.tecsup.hexagonal.app.domain.model.User;

@RequiredArgsConstructor
@Transactional
@Slf4j
public class CreateUserUseCaseImpl implements CreateUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public User execute(User newUser) {

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

    private void validateUserInput(User user) {

        if (!user.hasValidName()) {
            throw new InvalidUserDataException("Name must be at least 2 characters long");
        }

        if (!user.hasValidEmail()) {
            throw new InvalidUserDataException("Invalid email format");
        }
    }
}
