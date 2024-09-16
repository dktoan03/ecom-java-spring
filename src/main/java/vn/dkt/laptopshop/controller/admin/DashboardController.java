package vn.dkt.laptopshop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import vn.dkt.laptopshop.repository.OrderRepository;
import vn.dkt.laptopshop.repository.ProductRepository;
import vn.dkt.laptopshop.repository.UserRepository;

@Controller
public class DashboardController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public DashboardController(UserRepository userRepository, ProductRepository productRepository,
            OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/admin")
    public String getDashboard(Model model) {
        long countUser = userRepository.count();
        long countProduct = productRepository.count();
        long countOrder = orderRepository.count();

        model.addAttribute("countUser", countUser);
        model.addAttribute("countProduct", countProduct);
        model.addAttribute("countOrder", countOrder);

        return "admin/dashboard/show";
    }
}
