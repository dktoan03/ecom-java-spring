package vn.hoidanit.laptopshop.controller.client;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.domain.dto.RegisterDTO;
import vn.hoidanit.laptopshop.service.ProductService;
import vn.hoidanit.laptopshop.service.UserService;

@Controller
public class HomePageController {
  private final ProductService productService;
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  public HomePageController(ProductService productService, UserService userService, PasswordEncoder passwordEncoder) {
    this.productService = productService;
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
  }

  @GetMapping("/")
  public String getHomePage(Model model) {
    List<Product> products = productService.getAllProducts();
    model.addAttribute("products", products);
    return "client/homepage/show";
  }

  @GetMapping("register")
  public String getRegister(Model model) {
    model.addAttribute("registerUser", new RegisterDTO());
    return "client/auth/register";
  }

  @PostMapping("register")
  public String handlePostRegister(@ModelAttribute("userRegister") RegisterDTO register) {
    User user = userService.RegisterDTOtoUser(register);

    String hashedPassword = this.passwordEncoder.encode(user.getPassword());

    user.setPassword(hashedPassword);
    user.setRole(this.userService.getRoleByName("USER"));
    this.userService.handleSaveUser(user);

    return "redirect:/login";

  }

  @GetMapping("login")
  public String getLogin(Model model) {
    return "client/auth/login";
  }
}
