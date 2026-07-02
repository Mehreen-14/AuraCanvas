package com.example.auracanvas.service;

import com.example.auracanvas.dto.CartItemDto;
import com.example.auracanvas.model.CartItem;
import com.example.auracanvas.model.Product;
import com.example.auracanvas.model.User;
import com.example.auracanvas.repository.CartItemRepository;
import com.example.auracanvas.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartService(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public List<CartItemDto> getCartItems(Long userId) {
        return cartItemRepository.findByUserId(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public CartItemDto addToCart(Long userId, Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        var existing = cartItemRepository.findByUserIdAndProductId(userId, productId);
        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
            return toDto(cartItemRepository.save(item));
        }
        CartItem item = new CartItem();
        item.setUser(new User(userId));
        item.setProduct(product);
        item.setQuantity(quantity);
        return toDto(cartItemRepository.save(item));
    }

    public CartItemDto updateCartItem(Long userId, Long cartItemId, Integer quantity) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));
        if (!item.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Unauthorized");
        }
        item.setQuantity(quantity);
        return toDto(cartItemRepository.save(item));
    }

    public void removeFromCart(Long userId, Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));
        if (!item.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Unauthorized");
        }
        cartItemRepository.delete(item);
    }

    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }

    private CartItemDto toDto(CartItem item) {
        CartItemDto dto = new CartItemDto();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setProductPrice(item.getProduct().getPrice());
        dto.setProductImage(item.getProduct().getImageUrl());
        dto.setQuantity(item.getQuantity());
        dto.setStockQuantity(item.getProduct().getStockQuantity());
        return dto;
    }
}
