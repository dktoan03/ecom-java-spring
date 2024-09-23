package vn.dkt.laptopshop.controller.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import vn.dkt.laptopshop.domain.Cart;
import vn.dkt.laptopshop.domain.CartDetail;
import vn.dkt.laptopshop.domain.Order;
import vn.dkt.laptopshop.domain.Product;
import vn.dkt.laptopshop.domain.User;
import vn.dkt.laptopshop.service.ProductService;
import vn.dkt.laptopshop.service.UserService;

@Controller
public class ItemController {
  private final ProductService productService;
  private final UserService userService;

  public ItemController(ProductService productService, UserService userService) {
    this.productService = productService;
    this.userService = userService;
  }

  @GetMapping("product/{id}")
  public String getProductPage(@PathVariable long id, Model model) {
    Product product = productService.getProductById(id);
    model.addAttribute("product", product);
    return "client/product/detail";
  }

  @PostMapping("/add-product-to-cart/{id}")
  public String addProductToCart(@PathVariable long id, HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    String email = (String) session.getAttribute("email");
    this.productService.handleAddProductToCart(email, id, session, 1);
    return "redirect:/";
  }

  @GetMapping("/cart")
  public String getCartPage(Model model, HttpServletRequest request) {
    User currentUser = new User();
    HttpSession session = request.getSession(false);
    long id = (long) session.getAttribute("id");
    currentUser.setId(id);

    Cart cart = this.productService.getCartByUser(currentUser);

    List<CartDetail> cartDetails = cart == null ? new ArrayList<>() : cart.getCartDetails();

    double totalPrice = 0;

    for (CartDetail cartDetail : cartDetails) {
      totalPrice += cartDetail.getPrice() * cartDetail.getQuantity();

    }

    model.addAttribute("cart", cart);
    model.addAttribute("cartDetails", cartDetails);
    model.addAttribute("totalPrice", totalPrice);

    return "client/cart/show";
  }

  @PostMapping("/delete-cart-product/{id}")
  public String handleDeleteCardProduct(@PathVariable long id, HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    this.productService.handleDeleteCartDetail(id, session);
    return "redirect:/cart";
  }

  @GetMapping("/checkout")
  public String getCheckOutPage(Model model, HttpServletRequest request) {
    User currentUser = new User();// null
    HttpSession session = request.getSession(false);
    long id = (long) session.getAttribute("id");
    currentUser.setId(id);

    Cart cart = this.productService.getCartByUser(currentUser);

    List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails();

    double totalPrice = 0;
    for (CartDetail cd : cartDetails) {
      totalPrice += cd.getPrice() * cd.getQuantity();
    }

    model.addAttribute("cartDetails", cartDetails);
    model.addAttribute("totalPrice", totalPrice);

    return "client/cart/checkout";
  }

  @PostMapping("/confirm-checkout")
  public String getCheckOutPage(@ModelAttribute("cart") Cart cart) {
    List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails();
    this.productService.handleUpdateCartBeforeCheckout(cartDetails);
    return "redirect:/checkout";
  }

  @PostMapping("/place-order")
  public String handlePlaceOrder(
      HttpServletRequest request,
      @RequestParam("receiverName") String receiverName,
      @RequestParam("receiverAddress") String receiverAddress,
      @RequestParam("receiverPhone") String receiverPhone) {

    User currentUser = new User();// null
    HttpSession session = request.getSession(false);
    long id = (long) session.getAttribute("id");
    currentUser.setId(id);

    this.productService.handlePlaceOrder(currentUser, session, receiverName, receiverAddress, receiverPhone);

    return "redirect:/thank-you";
  }

  @GetMapping("/thank-you")
  public String getThankYouPage() {
    return "client/cart/thanks";
  }

  @GetMapping("/order-history")
  public String getOrderHistoryPage(HttpServletRequest request, Model model) {

    HttpSession session = request.getSession(false);
    long id = (long) session.getAttribute("id");
    User user = this.userService.getUserById(id);

    List<Order> orders = user.getOrders();
    model.addAttribute("orders", orders);
    return "client/cart/orderhistory";
  }

  @PostMapping("/add-more-product/{id}")
  public String addMoreProduct(@PathVariable long id, HttpServletRequest request,
      @RequestParam(value = "quantity", required = false, defaultValue = "1") long quantity) {
    HttpSession session = request.getSession(false);
    String email = (String) session.getAttribute("email");

    this.productService.handleAddProductToCart(email, id, session, quantity);

    return "redirect:/cart";
  }

  @GetMapping("/products")
  public String getProducts(Model model,
      @RequestParam(value = "page", required = false, defaultValue = "1") String pageOptional,
      @RequestParam(value = "name", required = false, defaultValue = "") String nameOptional) {

    int page = 1;
    final int pageSize = 6;
    try {
      page = Integer.parseInt(pageOptional);
    } catch (Exception e) {

    }

    Pageable pageable = PageRequest.of(page - 1, pageSize);
    Page<Product> productsPage = this.productService.getAllProducts(pageable, nameOptional);
    List<Product> products = productsPage.getContent();

    model.addAttribute("products", products);
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", productsPage.getTotalPages());
    return "client/product/show";
  }
}
