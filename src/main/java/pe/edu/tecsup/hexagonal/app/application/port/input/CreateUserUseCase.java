package pe.edu.tecsup.hexagonal.app.application.port.input;


import pe.edu.tecsup.hexagonal.app.domain.model.User;

public interface CreateUserUseCase {
    User execute(User newUser);
}