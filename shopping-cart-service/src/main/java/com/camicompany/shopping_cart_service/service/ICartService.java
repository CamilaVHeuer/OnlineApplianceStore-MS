package com.camicompany.shopping_cart_service.service;

import com.camicompany.shopping_cart_service.dto.CartResponseDTO;
import com.camicompany.shopping_cart_service.dto.CreateCartDTO;
import com.camicompany.shopping_cart_service.dto.UpdateCartDTO;

import java.util.List;

public interface ICartService {

    public List<CartResponseDTO> getAllCarts();


    public CartResponseDTO getCartById(Long cartId);
    public CartResponseDTO getCartByIdForSale(Long cartId);

    public void deleteCart(Long cartId);

    public CartResponseDTO createCart(CreateCartDTO createCartDTO);

    public CartResponseDTO updateCart(Long cartId, UpdateCartDTO updateCartDTO);

    public void markCartAsSold(Long cartId);
}
