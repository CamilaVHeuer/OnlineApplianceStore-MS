package com.camicompany.shopping_cart_service.service;

import com.camicompany.shopping_cart_service.dto.CartDTO;

import java.util.List;

public interface ICartService {

    public List<CartDTO> getAllCarts();

    public CartDTO getCartById(Long cartId);

    public void deleteCart(Long cartId);

    public CartDTO createCart(CartDTO cartDTO);

    public CartDTO updateCart(Long cartId, CartDTO cartDTO);

    public void markCartAsSold(Long cartId);
}
