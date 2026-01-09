package com.camicompany.shopping_cart_service.repository;

import com.camicompany.shopping_cart_service.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICartRepository extends JpaRepository<Cart, Long> {

}
