package vn.dkt.laptopshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.dkt.laptopshop.domain.Cart;
import vn.dkt.laptopshop.domain.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
  Cart findByUser(User user);
}
