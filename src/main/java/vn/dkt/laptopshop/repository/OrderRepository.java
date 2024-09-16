package vn.dkt.laptopshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.dkt.laptopshop.domain.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findAll();

  Order findById(long id);

  void deleteById(long id);
}
