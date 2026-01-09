package com.camicompany.products_service.service;

import com.camicompany.products_service.dto.ProductDTO;
import com.camicompany.products_service.mapper.Mapper;
import com.camicompany.products_service.model.Product;
import com.camicompany.products_service.repository.IProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductService implements IProductService {
    @Autowired
    private IProductRepository prodRepo;

    @Override
    public List<ProductDTO> getProducts() {
        return prodRepo.findAll().stream().map(Mapper::toDTO).toList();
    }

    @Override
    public ProductDTO getProductById(Long productId) {
        Product product= prodRepo.findById(productId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        return Mapper.toDTO(product);
    }

    @Override
    public ProductDTO getProductByCode(String code) {
        Product product = prodRepo.findByCode(code);
        if (product == null){ throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        return Mapper.toDTO(product);
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        if (productDTO == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product data is required");
        }
        if (productDTO.getCode() == null || productDTO.getCode().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product code is required");
        }
        if (prodRepo.existsByCode(productDTO.getCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product code already exists");
        }
        if (productDTO.getName()== null || productDTO.getName().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name is required");
        }
        if(productDTO.getBrand()== null || productDTO.getBrand().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product brand is required");
        }
        if (productDTO.getUnitPrice() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unit price is required");
        }
        if (productDTO.getStock() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock is required");
        }
        if (productDTO.getUnitPrice() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unit price cannot be negative");
        }
        if (productDTO.getStock() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock cannot be negative");
        }

        Product product = Product.builder()
                .code(productDTO.getCode())
                .name(productDTO.getName())
                .brand(productDTO.getBrand())
                .unitPrice(productDTO.getUnitPrice())
                .stock(productDTO.getStock())
                .build();
        Product savedProduct = prodRepo.save(product);
        return Mapper.toDTO(savedProduct);
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product existingProduct = prodRepo.findById(productId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        String newCode = productDTO.getCode();

        if (newCode != null && !newCode.isEmpty()) {
            boolean codeChanged = !existingProduct.getCode().equals(newCode);
            boolean codeExists = prodRepo.existsByCode(newCode);

            if (codeChanged && codeExists) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT, "Product code already exists"
                );
            }
            existingProduct.setCode(newCode);
        }
        if(productDTO.getName()!= null && !productDTO.getName().isEmpty()){
            existingProduct.setName(productDTO.getName());
        }
        if(productDTO.getBrand()!= null && !productDTO.getBrand().isEmpty()){
            existingProduct.setBrand(productDTO.getBrand());}

        if(productDTO.getUnitPrice()!= null){
            if (productDTO.getUnitPrice() < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unit price cannot be negative");}
            existingProduct.setUnitPrice(productDTO.getUnitPrice());}
        if(productDTO.getStock()!= null){
            if (productDTO.getStock() < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock cannot be negative");}
            existingProduct.setStock(productDTO.getStock());}

        Product updatedProduct = prodRepo.save(existingProduct);
        return Mapper.toDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(Long productId) {
        if(!prodRepo.existsById(productId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        prodRepo.deleteById(productId);

    }

    @Override
    public List<ProductDTO> getProductsLowStock() {

        return prodRepo.findByStockLessThanEqual(5).stream().map(Mapper::toDTO).toList();
    }

    @Transactional
    @Override
    public ProductDTO decreaseProductStock(Long productId, Integer quantity) {
        Product product = prodRepo.findById(productId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        if (quantity == null || quantity <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be greater than zero");
        }
        if (product.getStock() < quantity) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Insufficient stock");
        }
        product.setStock(product.getStock() - quantity);
        Product updatedProduct = prodRepo.save(product);
        return Mapper.toDTO(updatedProduct);
    }
    @Transactional
    @Override
    public ProductDTO increaseProductStock(Long productId, Integer quantity) {
        Product product = prodRepo.findById(productId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        if (quantity == null || quantity <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be greater than zero");
        }
        product.setStock(product.getStock() + quantity);
        Product updatedProduct = prodRepo.save(product);
        return Mapper.toDTO(updatedProduct);
    }

}



