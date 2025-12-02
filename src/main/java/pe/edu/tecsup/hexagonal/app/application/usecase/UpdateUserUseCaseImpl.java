package pe.edu.tecsup.hexagonal.app.application.usecase;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.tecsup.hexagonal.app.application.port.input.UpdateUserUseCase;
import pe.edu.tecsup.hexagonal.app.application.port.output.UserRepositoryPort;
import pe.edu.tecsup.hexagonal.app.domain.exception.InvalidUserDataException;
import pe.edu.tecsup.hexagonal.app.domain.exception.UserNotFoundException;
import pe.edu.tecsup.hexagonal.app.domain.model.User;

@RequiredArgsConstructor
@Transactional
@Slf4j
public class UpdateUserUseCaseImpl implements UpdateUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    @Transactional
    public User execute(Long id, User user) {
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

    private void validateUserInput(User user) {

        if (!user.hasValidName()) {
            throw new InvalidUserDataException("Name must be at least 2 characters long");
        }

        if (!user.hasValidEmail()) {
            throw new InvalidUserDataException("Invalid email format");
        }
    }
}
