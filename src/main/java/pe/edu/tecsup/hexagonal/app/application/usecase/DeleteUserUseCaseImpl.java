package pe.edu.tecsup.hexagonal.app.application.usecase;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.tecsup.hexagonal.app.application.port.input.DeleteUserUseCase;
import pe.edu.tecsup.hexagonal.app.application.port.input.UserUseCase;
import pe.edu.tecsup.hexagonal.app.application.port.output.UserRepositoryPort;
import pe.edu.tecsup.hexagonal.app.domain.exception.InvalidUserDataException;
import pe.edu.tecsup.hexagonal.app.domain.exception.UserNotFoundException;
import pe.edu.tecsup.hexagonal.app.domain.model.User;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Slf4j
public class DeleteUserUseCaseImpl implements DeleteUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

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

}
