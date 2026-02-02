package com.camicompany.products_service.service;

import com.camicompany.products_service.dto.CreateProductDTO;
import com.camicompany.products_service.dto.ProductResponseDTO;
import com.camicompany.products_service.dto.UpdateProductDTO;
import com.camicompany.products_service.mapper.Mapper;
import com.camicompany.products_service.model.Product;
import com.camicompany.products_service.model.ProductStatus;
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
    public List<ProductResponseDTO> getProducts() {
        return prodRepo.findAll().stream().map(Mapper::toDTO).toList();
    }

    @Override
    public ProductResponseDTO getProductById(Long productId) {
        Product product= findProductOrThrow(productId);
        return Mapper.toDTO(product);
    }

    @Override
    public ProductResponseDTO getProductByCode(String code) {
        Product product = prodRepo.findByCode(code);
        if (product == null){ throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        return Mapper.toDTO(product);
    }

    @Override
    public ProductResponseDTO createProduct(CreateProductDTO productDTO) {

        if (prodRepo.existsByCode(productDTO.code())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product code already exists");
        }

        Product product = Product.builder()
                .code(productDTO.code())
                .name(productDTO.name())
                .brand(productDTO.brand())
                .unitPrice(productDTO.unitPrice())
                .stock(productDTO.stock())
                .status(ProductStatus.ACTIVE)
                .build();
        Product savedProduct = prodRepo.save(product);
        return Mapper.toDTO(savedProduct);
    }

    @Override
    public ProductResponseDTO updateProduct(Long productId, UpdateProductDTO productDTO) {
        Product existingProduct = findProductOrThrow(productId);

        ensureProductIsActive(existingProduct);

        String newCode = productDTO.code();

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
        if(productDTO.name()!= null && !productDTO.name().isEmpty()){
            existingProduct.setName(productDTO.name());
        }
        if(productDTO.brand()!= null && !productDTO.brand().isEmpty()){
            existingProduct.setBrand(productDTO.brand());}

        if(productDTO.unitPrice()!= null){
            if (productDTO.unitPrice() < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unit price cannot be negative");}
            existingProduct.setUnitPrice(productDTO.unitPrice());}
        if(productDTO.stock()!= null){
            if (productDTO.stock() < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock cannot be negative");}
            existingProduct.setStock(productDTO.stock());}

        Product updatedProduct = prodRepo.save(existingProduct);
        return Mapper.toDTO(updatedProduct);
    }

    @Override
    public ProductResponseDTO discontinueProduct(Long productId) {

        Product prod = findProductOrThrow(productId);

        ensureProductIsActive(prod);

        prod.setStatus(ProductStatus.DISCONTINUED);
        return Mapper.toDTO(prodRepo.save(prod));

    }

    @Override
    public ProductResponseDTO activateProduct(Long productId) {
        Product prod = findProductOrThrow(productId);

        if (prod.getStatus() == ProductStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product is already active");
        }
        prod.setStatus(ProductStatus.ACTIVE);
        return Mapper.toDTO(prodRepo.save(prod));
    }

    @Override
    public List<ProductResponseDTO> getProductsLowStock() {

        return prodRepo.findByStockLessThanEqual(5).stream().map(Mapper::toDTO).toList();
    }

    @Transactional
    @Override
    public ProductResponseDTO decreaseProductStock(Long productId, Integer quantity) {
        Product product = findProductOrThrow(productId);

        validateQuantity(quantity);

        if (product.getStock() < quantity) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Insufficient stock");
        }
        product.setStock(product.getStock() - quantity);
        Product updatedProduct = prodRepo.save(product);
        return Mapper.toDTO(updatedProduct);
    }

    @Transactional
    @Override
    public ProductResponseDTO increaseProductStock(Long productId, Integer quantity) {
        Product product = findProductOrThrow(productId);

        validateQuantity(quantity);

        product.setStock(product.getStock() + quantity);
        Product updatedProduct = prodRepo.save(product);
        return Mapper.toDTO(updatedProduct);
    }

    //Helpers methods

    //find product by id or throw 404
    private Product findProductOrThrow(Long productId) {
        return prodRepo.findById(productId).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    //validate quantity is positive
    private void validateQuantity(Integer quantity){
        if (quantity == null || quantity <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be greater than zero");
        }
    }

    //validate product is active
    private void ensureProductIsActive(Product product) {
        if (product.getStatus() == ProductStatus.DISCONTINUED) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Product is discontinued"
            );
        }
    }




}



