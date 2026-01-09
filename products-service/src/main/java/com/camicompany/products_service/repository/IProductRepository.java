package com.camicompany.products_service.repository;

import com.camicompany.products_service.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {
    Product findByCode(String code);
    List<Product> findByStockLessThanEqual(Integer stock);

    boolean existsByCode(String code);

}
