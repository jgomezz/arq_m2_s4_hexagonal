package pe.edu.tecsup.hexagonal.app.infrastructure.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.stereotype.Service;
import pe.edu.tecsup.hexagonal.app.application.port.input.UserService;
import pe.edu.tecsup.hexagonal.app.application.port.output.UserRepositoryPort;
import pe.edu.tecsup.hexagonal.app.application.service.UserServiceImpl;

@Configuration
public class HexagonalConfig {

    @Bean
    public UserService userService(UserRepositoryPort userRepository) {
        return new UserServiceImpl(userRepository);
    }


}
