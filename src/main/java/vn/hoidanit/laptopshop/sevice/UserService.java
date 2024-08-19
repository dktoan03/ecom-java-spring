package vn.hoidanit.laptopshop.sevice;

import org.springframework.stereotype.Service;

import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.UserRepository;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User handleSaveUser(User user) {

    User newUser = this.userRepository.save(user);
    System.out.println(newUser);
    return newUser;
  }
}
