package pe.edu.tecsup.hexagonal.app.infrastructure.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pe.edu.tecsup.hexagonal.app.application.port.input.DeleteUserUseCase;
import pe.edu.tecsup.hexagonal.app.application.port.input.UserUseCase;
import pe.edu.tecsup.hexagonal.app.application.port.output.UserRepositoryPort;
import pe.edu.tecsup.hexagonal.app.application.usecase.DeleteUserUseCaseImpl;
import pe.edu.tecsup.hexagonal.app.application.usecase.UserUseCaseImpl;

@Configuration
public class HexagonalConfig {

    @Bean
    public UserUseCase userService(UserRepositoryPort userRepository) {
        return new UserUseCaseImpl(userRepository);
    }

    @Bean
    public DeleteUserUseCase deleteUserUseCase(UserRepositoryPort userRepository) {
        return new DeleteUserUseCaseImpl(userRepository);
    }

}
