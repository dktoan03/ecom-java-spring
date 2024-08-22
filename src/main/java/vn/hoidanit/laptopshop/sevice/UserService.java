package vn.hoidanit.laptopshop.sevice;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.UserRepository;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> getAllUsers() {
    return this.userRepository.findAll();
  }

  public List<User> getAllUsersByEmail(String email) {

    return this.userRepository.findByEmail(email);
  }

  public User getUserById(long id) {

    return this.userRepository.findById(id);
  }

  public User handleSaveUser(User user) {
    User newUser = this.userRepository.save(user);
    System.out.println(newUser);
    return newUser;
  }
}
