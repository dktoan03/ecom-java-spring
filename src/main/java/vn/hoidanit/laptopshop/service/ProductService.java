package vn.hoidanit.laptopshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.repository.ProductRepository;

@Service
public class ProductService {
  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public void handleDeleteProduct(long id) {
    this.productRepository.deleteById(id);
  }

  public Product handleSaveProduct(Product newProduct) {
    return this.productRepository.save(newProduct);
  }

  public List<Product> getAllProducts() {
    return this.productRepository.findAll();
  }

  public Optional<Product> getProductById(long id) {
    return this.productRepository.findById(id);

  }
}
