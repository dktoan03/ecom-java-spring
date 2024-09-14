package vn.dkt.laptopshop.controller.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import vn.dkt.laptopshop.domain.Cart;
import vn.dkt.laptopshop.domain.CartDetail;
import vn.dkt.laptopshop.domain.Product;
import vn.dkt.laptopshop.domain.User;
import vn.dkt.laptopshop.service.ProductService;

@Controller
public class ItemController {
  private final ProductService productService;

  public ItemController(ProductService productService) {
    this.productService = productService;
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
    this.productService.handleAddProductToCart(email, id, session);
    return "redirect:/";
  }

  @GetMapping("/cart")
  public String getCartPage(Model model, HttpServletRequest request) {
    User currentUser = new User();
    HttpSession session = request.getSession(false);
    long id = (long) session.getAttribute("id");
    currentUser.setId(id);

    Cart cart = this.productService.getCartByUser(currentUser);

    List<CartDetail> cartDetails = new ArrayList<>();

    double totalPrice = 0;

    if (cart != null) {
      cartDetails = cart.getCartDetails();

      for (CartDetail cartDetail : cartDetails) {
        totalPrice += cartDetail.getPrice() * cartDetail.getQuantity();
      }

    }

    model.addAttribute("cartDetails", cartDetails);
    model.addAttribute("totalPrice", totalPrice);

    return "client/cart/show";
  }
}