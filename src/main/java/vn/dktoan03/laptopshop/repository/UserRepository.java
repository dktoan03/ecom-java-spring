package vn.dktoan03.laptopshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.dktoan03.laptopshop.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User save(User anotherUser);

  List<User> findAll();

  List<User> findByEmail(String email);

  User findById(long id);

  void deleteById(long id);
}
