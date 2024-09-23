package vn.dkt.laptopshop.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.dkt.laptopshop.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
  Product save(Product newProduct);

  List<Product> findAll();

  void deleteById(long id);

  Product findById(long id);

  Page<Product> findAll(Pageable page);

  Page<Product> findAll(Specification<Product> specification, Pageable page);

}
