package vn.dkt.laptopshop.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import vn.dkt.laptopshop.domain.Order;
import vn.dkt.laptopshop.domain.OrderDetail;
import vn.dkt.laptopshop.repository.OrderDetailRepository;
import vn.dkt.laptopshop.service.OrderService;

@Controller
public class OrderController {
    private final OrderService orderService;
    private final OrderDetailRepository orderDetailRepository;

    public OrderController(OrderService orderService, OrderDetailRepository orderDetailRepository) {
        this.orderService = orderService;
        this.orderDetailRepository = orderDetailRepository;
    }

    @GetMapping("/admin/order")
    public String getDashboard(Model model) {
        List<Order> orders = this.orderService.findAllOrders();
        model.addAttribute("orders", orders);
        return "admin/order/show";
    }

    @GetMapping("/admin/order/{id}")
    public String getUserDetailPage(Model model, @PathVariable long id) {
        Order order = this.orderService.getOrderById(id);
        List<OrderDetail> orderDetails = order.getOrderDetails();
        model.addAttribute("order", order);
        model.addAttribute("orderDetails", orderDetails);

        model.addAttribute("id", id);

        return "admin/order/detail";
    }

    @GetMapping("/admin/order/delete/{id}")
    public String getDeleteProductPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("newOrder", new Order());
        return "admin/order/delete";
    }

    @PostMapping("/admin/order/delete")
    public String postDeleteProduct(@ModelAttribute("newOrder") Order order) {
        this.orderService.deleteOrderById(order.getId());
        return "redirect:/admin/order";
    }

    @GetMapping("/admin/order/update/{id}")
    public String getUpdateOrderPage(Model model, @PathVariable long id) {
        Order currentOrder = this.orderService.getOrderById(id);
        model.addAttribute("newOrder", currentOrder);
        return "admin/order/update";
    }

    @PostMapping("/admin/order/update")
    public String handleUpdateOrder(@ModelAttribute("newOrder") Order order) {
        this.orderService.updateOrder(order);
        return "redirect:/admin/order";
    }
}
