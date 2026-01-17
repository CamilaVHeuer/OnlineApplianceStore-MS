package com.camicompany.products_service.service;

import com.camicompany.products_service.dto.ProductDTO;

import java.util.List;


public interface IProductService {
    public List<ProductDTO> getProducts();

    public ProductDTO getProductById(Long productId);

    public ProductDTO getProductByCode(String code);

    public ProductDTO createProduct(ProductDTO productDTO);

    public ProductDTO updateProduct(Long productId, ProductDTO productDTO);

    public ProductDTO discontinueProduct(Long productId);

    public ProductDTO activateProduct(Long productId);

    public List<ProductDTO> getProductsLowStock();

    public ProductDTO decreaseProductStock(Long productId, Integer quantity);

    public ProductDTO increaseProductStock(Long productId, Integer quantity);


}
