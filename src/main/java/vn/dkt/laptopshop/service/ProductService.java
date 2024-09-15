package vn.dkt.laptopshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import vn.dkt.laptopshop.domain.Cart;
import vn.dkt.laptopshop.domain.CartDetail;
import vn.dkt.laptopshop.domain.Product;
import vn.dkt.laptopshop.domain.User;
import vn.dkt.laptopshop.repository.CartDetailRepository;
import vn.dkt.laptopshop.repository.CartRepository;
import vn.dkt.laptopshop.repository.ProductRepository;

@Service
public class ProductService {
  private final ProductRepository productRepository;
  private final CartRepository cartRepository;
  private final CartDetailRepository cartDetailRepository;
  private final UserService userService;

  public ProductService(ProductRepository productRepository, CartRepository cartRepository,
      CartDetailRepository cartDetailRepository, UserService userService) {
    this.productRepository = productRepository;
    this.cartRepository = cartRepository;
    this.cartDetailRepository = cartDetailRepository;
    this.userService = userService;
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

  public Product getProductById(long id) {
    return this.productRepository.findById(id);
  }

  public void deleteProduct(long id) {
    this.productRepository.deleteById(id);
  }

  public void handleAddProductToCart(String email, long productId, HttpSession session) {
    User user = this.userService.getUserByEmail(email);
    if (user != null) {
      Cart cart = this.cartRepository.findByUser(user);
      if (cart == null) {
        cart = new Cart();
        cart.setUser(user);
        cart.setSum(0);
        this.cartRepository.save(cart);
      }
      Product product = this.productRepository.findById(productId);
      if (product != null) {
        CartDetail cartDetail = this.cartDetailRepository.findByCartAndProduct(cart, product);
        if (cartDetail == null) {
          cartDetail = new CartDetail();
          cartDetail.setCart(cart);
          cartDetail.setProduct(product);
          cartDetail.setPrice(product.getPrice());
          cartDetail.setQuantity(1);

          session.setAttribute("sum", cart.getSum() + 1);

          cart.setSum(cart.getSum() + 1);

          this.cartRepository.save(cart);
          this.cartDetailRepository.save(cartDetail);
        } else {
          // cartDetail.setPrice(cartDetail.getPrice() + product.getPrice());
          cartDetail.setQuantity(cartDetail.getQuantity() + 1);
          this.cartDetailRepository.save(cartDetail);
        }

      }
    }
  }

  public Cart getCartByUser(User user) {
    return this.cartRepository.findByUser(user);
  }

  public void handleDeleteCartDetail(long cartDetailId, HttpSession session) {
    CartDetail cartDetail = this.cartDetailRepository.findById(cartDetailId);
    if (cartDetail != null) {
      Cart cart = cartDetail.getCart();
      this.cartDetailRepository.delete(cartDetail);
      if (cart.getSum() > 1) {
        session.setAttribute("sum", cart.getSum() - 1);
        cart.setSum(cart.getSum() - 1);
        this.cartRepository.save(cart);
      } else {
        session.setAttribute("sum", 0);
        this.cartRepository.delete(cart);
      }
    }
  }

  public void handleUpdateCartBeforeCheckout(List<CartDetail> cartDetails) {
    for (CartDetail cartDetail : cartDetails) {
      CartDetail currentCartDetail = this.cartDetailRepository.findById(cartDetail.getId());
      currentCartDetail.setQuantity(cartDetail.getQuantity());
      this.cartDetailRepository.save(currentCartDetail);
    }
  }
}
