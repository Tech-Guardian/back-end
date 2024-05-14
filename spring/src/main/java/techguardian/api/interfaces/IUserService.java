package techguardian.api.interfaces;

import java.util.List;

import techguardian.api.entity.User;

public interface IUserService {

    List<User> findAll();
    User createUser(User user);
    User updateUser(Long id, User updateUser);
    User deleteUser(Long id);
}
