package pe.edu.tecsup.hexagonal.app.application.port.input;


import pe.edu.tecsup.hexagonal.app.domain.model.User;

import java.util.List;

public interface UserUseCase {

    User createUser(User newUser);

    User findUser(Long id);
    List<User> findAllUsers();
    List<User> findUsersByName(String name);

    User updateUser(Long id, User user);
    //void deleteUser(Long id);
    boolean existsByEmail(String email);
}