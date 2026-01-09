package com.camicompany.shopping_cart_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;
    private Long productId;
    private Integer quantity;
    private Double unitPrice;
    @ManyToOne
    @JoinColumn(name="cart_id", nullable=false)
    private Cart cart;
}
