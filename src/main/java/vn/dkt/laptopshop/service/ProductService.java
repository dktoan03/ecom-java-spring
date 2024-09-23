package vn.dkt.laptopshop.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import vn.dkt.laptopshop.domain.Cart;
import vn.dkt.laptopshop.domain.CartDetail;
import vn.dkt.laptopshop.domain.Order;
import vn.dkt.laptopshop.domain.OrderDetail;
import vn.dkt.laptopshop.domain.Product;
import vn.dkt.laptopshop.domain.Product_;
import vn.dkt.laptopshop.domain.User;
import vn.dkt.laptopshop.repository.CartDetailRepository;
import vn.dkt.laptopshop.repository.CartRepository;
import vn.dkt.laptopshop.repository.OrderDetailRepository;
import vn.dkt.laptopshop.repository.OrderRepository;
import vn.dkt.laptopshop.repository.ProductRepository;

@Service
public class ProductService {
  private final ProductRepository productRepository;
  private final CartRepository cartRepository;
  private final CartDetailRepository cartDetailRepository;
  private final UserService userService;
  private final OrderRepository orderRepository;
  private final OrderDetailRepository orderDetailRepository;

  public ProductService(ProductRepository productRepository, CartRepository cartRepository,
      CartDetailRepository cartDetailRepository, UserService userService, OrderRepository orderRepository,
      OrderDetailRepository orderDetailRepository) {
    this.productRepository = productRepository;
    this.cartRepository = cartRepository;
    this.cartDetailRepository = cartDetailRepository;
    this.userService = userService;
    this.orderRepository = orderRepository;
    this.orderDetailRepository = orderDetailRepository;
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

  public Page<Product> getAllProducts(Pageable pageable) {
    return this.productRepository.findAll(pageable);
  }

  public Specification<Product> nameLike(String name) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Product_.NAME), "%" + name + "%");

  }

  public Page<Product> getAllProducts(Pageable pageable, String name) {
    return this.productRepository.findAll(this.nameLike(name), pageable);
  }

  public Product getProductById(long id) {
    return this.productRepository.findById(id);
  }

  public void deleteProduct(long id) {
    this.productRepository.deleteById(id);
  }

  public void handleAddProductToCart(String email, long productId, HttpSession session, long quantity) {
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
          cartDetail.setQuantity(quantity);

          session.setAttribute("sum", cart.getSum() + 1);

          cart.setSum(cart.getSum() + 1);

          this.cartRepository.save(cart);
          this.cartDetailRepository.save(cartDetail);
        } else {
          // cartDetail.setPrice(cartDetail.getPrice() + product.getPrice());
          cartDetail.setQuantity(cartDetail.getQuantity() + quantity);
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

  public void handlePlaceOrder(User currentUser, HttpSession session, String receiverName, String receiverAddress,
      String receiverPhone) {

    Order order = new Order();
    order.setUser(currentUser);
    order.setReceiverAddress(receiverAddress);
    order.setReceiverName(receiverName);
    order.setReceiverPhone(receiverPhone);
    order.setStatus("PENDING");
    order = this.orderRepository.save(order);

    Cart cart = this.cartRepository.findByUser(currentUser);
    if (cart != null) {
      List<CartDetail> cartDetails = cart.getCartDetails();

      double sum = 0;

      for (CartDetail cartDetail : cartDetails) {
        sum += cartDetail.getPrice() * cartDetail.getQuantity();

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
        orderDetail.setPrice(cartDetail.getPrice());
        orderDetail.setQuantity(cartDetail.getQuantity());
        orderDetail.setProduct(cartDetail.getProduct());
        this.orderDetailRepository.save(orderDetail);

        this.cartDetailRepository.delete(cartDetail);
      }
      this.cartRepository.delete(cart);

      order.setTotalPrice(sum);
      this.orderRepository.save(order);
      session.setAttribute("sum", 0);
    }

  }
}
