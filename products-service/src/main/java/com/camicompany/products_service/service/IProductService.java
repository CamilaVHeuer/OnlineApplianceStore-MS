package com.camicompany.products_service.service;

import com.camicompany.products_service.dto.CreateProductDTO;
import com.camicompany.products_service.dto.ProductResponseDTO;
import com.camicompany.products_service.dto.UpdateProductDTO;

import java.util.List;


public interface IProductService {
    public List<ProductResponseDTO> getProducts();

    public ProductResponseDTO getProductById(Long productId);

    public ProductResponseDTO getProductByCode(String code);

    public ProductResponseDTO createProduct(CreateProductDTO productDTO);

    public ProductResponseDTO updateProduct(Long productId, UpdateProductDTO productDTO);

    public ProductResponseDTO discontinueProduct(Long productId);

    public ProductResponseDTO activateProduct(Long productId);

    public List<ProductResponseDTO> getProductsLowStock();

    public ProductResponseDTO decreaseProductStock(Long productId, Integer quantity);

    public ProductResponseDTO increaseProductStock(Long productId, Integer quantity);


}
