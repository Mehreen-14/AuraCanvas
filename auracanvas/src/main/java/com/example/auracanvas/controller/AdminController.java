package com.example.auracanvas.controller;

import com.example.auracanvas.dto.DashboardStats;
import com.example.auracanvas.dto.OrderDto;
import com.example.auracanvas.dto.ProductDto;
import com.example.auracanvas.model.Order;
import com.example.auracanvas.model.Product;
import com.example.auracanvas.model.User;
import com.example.auracanvas.repository.OrderRepository;
import com.example.auracanvas.repository.ProductRepository;
import com.example.auracanvas.repository.UserRepository;
import com.example.auracanvas.service.OrderService;
import com.example.auracanvas.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final ProductService productService;
    private final OrderService orderService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public AdminController(ProductService productService, OrderService orderService,
                           UserRepository userRepository, ProductRepository productRepository,
                           OrderRepository orderRepository) {
        this.productService = productService;
        this.orderService = orderService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStats> getDashboardStats() {
        List<Product> products = productRepository.findAll();
        List<Order> orders = orderRepository.findAll();

        DashboardStats stats = new DashboardStats();
        stats.setTotalProducts(products.size());
        stats.setLowStockProducts(products.stream().filter(p -> p.getStockQuantity() <= 5 && p.getStockQuantity() > 0).count());
        stats.setOutOfStockProducts(products.stream().filter(p -> p.getStockQuantity() == 0).count());
        stats.setTotalOrders(orders.size());
        stats.setOrdersByStatus(orders.stream().collect(Collectors.groupingBy(
                o -> o.getStatus().name(), Collectors.counting())));
        stats.setTotalRevenue(orders.stream().mapToDouble(Order::getTotalAmount).sum());
        stats.setTotalUsers(userRepository.count());
        return ResponseEntity.ok(stats);
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto dto) {
        return ResponseEntity.ok(productService.createProduct(dto));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto dto) {
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/orders/recent")
    public ResponseEntity<List<OrderDto>> getRecentOrders() {
        List<Order> orders = orderRepository.findAll();
        orders.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        List<OrderDto> recent = orders.stream().limit(5).map(o -> orderService.toDto(o)).collect(Collectors.toList());
        return ResponseEntity.ok(recent);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable Long orderId,
                                                       @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, body.get("status")));
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}
