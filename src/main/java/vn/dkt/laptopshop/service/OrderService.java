package vn.dkt.laptopshop.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.dkt.laptopshop.domain.Order;
import vn.dkt.laptopshop.domain.OrderDetail;
import vn.dkt.laptopshop.repository.OrderDetailRepository;
import vn.dkt.laptopshop.repository.OrderRepository;

@Service
public class OrderService {
  private final OrderRepository orderRepository;
  private final OrderDetailRepository orderDetailRepository;

  public OrderService(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository) {
    this.orderRepository = orderRepository;
    this.orderDetailRepository = orderDetailRepository;
  }

  public List<Order> findAllOrders() {
    return this.orderRepository.findAll();
  }

  public Page<Order> findAllOrders(Pageable pageable) {
    return this.orderRepository.findAll(pageable);
  }

  public Order getOrderById(long id) {
    return this.orderRepository.findById(id);
  }

  public void deleteOrderById(long id) {
    List<OrderDetail> orderDetails = this.getOrderById(id).getOrderDetails();
    for (OrderDetail orderDetail : orderDetails) {
      this.orderDetailRepository.delete(orderDetail);
    }
    this.orderRepository.deleteById(id);
  }

  public Order updateOrder(Order order) {
    Order currentOrder = this.getOrderById(order.getId());
    currentOrder.setStatus(order.getStatus());
    return this.orderRepository.save(currentOrder);
  }

}
