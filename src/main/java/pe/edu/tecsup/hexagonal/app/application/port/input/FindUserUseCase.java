package pe.edu.tecsup.hexagonal.app.application.port.input;


import pe.edu.tecsup.hexagonal.app.domain.model.User;

import java.util.List;

public interface FindUserUseCase {


    User findUser(Long id);
    List<User> findAllUsers();
    List<User> findUsersByName(String name);

    //boolean existsByEmail(String email);
}