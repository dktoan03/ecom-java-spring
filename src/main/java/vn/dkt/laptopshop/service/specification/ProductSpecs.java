package vn.dkt.laptopshop.service.specification;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import vn.dkt.laptopshop.domain.Product;
import vn.dkt.laptopshop.domain.Product_;

public class ProductSpecs {
  public static Specification<Product> nameLike(String name) {
    if (name.equals(""))
      return null;
    return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Product_.NAME), "%" + name + "%");
  }

  public static Specification<Product> minPrice(Double price) {
    if (price == 0)
      return null;
    return (root, query, criteriaBuilder) -> criteriaBuilder.gt(root.get(Product_.PRICE), price);
  }

  public static Specification<Product> maxPrice(Double price) {
    if (price == 0)
      return null;
    return (root, query, criteriaBuilder) -> criteriaBuilder.lt(root.get(Product_.PRICE), price);
  }

  public static Specification<Product> factory(String factory) {
    if (factory.equals(""))
      return null;
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Product_.FACTORY), factory);
  }

  public static Specification<Product> factory(List<String> factory) {
    if (factory.get(0).equals(""))
      return null;
    return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get(Product_.FACTORY)).value(factory);
  }

  public static Specification<Product> target(List<String> target) {
    if (target.get(0).equals(""))
      return null;
    return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get(Product_.TARGET)).value(target);
  }

  public static Specification<Product> matchPrice(Double min, Double max) {

    return (root, query, criteriaBuilder) -> criteriaBuilder.and(
        criteriaBuilder.gt(root.get(Product_.PRICE), min),
        criteriaBuilder.lt(root.get(Product_.PRICE), max));
  }

  // public static Specification<Product> matchPrice(Double min, Double max) {

  // return (root, query, criteriaBuilder) -> criteriaBuilder.between(
  // root.get(Product_.PRICE), min, max);
  // }

}
