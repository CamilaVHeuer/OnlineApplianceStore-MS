package com.camicompany.products_service.controller;

import com.camicompany.products_service.dto.CreateProductDTO;
import com.camicompany.products_service.dto.ProductResponseDTO;
import com.camicompany.products_service.dto.UpdateProductDTO;
import com.camicompany.products_service.service.IProductService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
@Tag(
        name = "Products",
        description = "Public operations for product catalog and inventory"
)
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private IProductService prodServ;


    @Operation(summary = "Get all products")
    @ApiResponse(responseCode = "200", description = "List of products returned")
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {

        return ResponseEntity.ok(prodServ.getProducts());
    }

    @Operation(
            summary = "Get product by ID",
            description = "Returns a product based on its unique identifier"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping ("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(prodServ.getProductById(id));
    }

    @Operation(
            summary = "Get product by code",
            description = "Returns a product using its business code"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping ("/code/{code}")
    public ResponseEntity<ProductResponseDTO> getProductByCode(@PathVariable String code) {
        return ResponseEntity.ok(prodServ.getProductByCode(code));}

    @Operation(
            summary = "Get products with low stock",
            description = "Returns products whose stock is below the defined threshold"
    )
    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductResponseDTO>> getProductsLowStock() {
        return ResponseEntity.ok(prodServ.getProductsLowStock());
    }

    @Operation(
            summary = "Create a new product",
            description = "Creates a product. The ID is generated automatically by the system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product data"),
            @ApiResponse(responseCode = "409", description = "Product code already exists")
    })
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody @Valid CreateProductDTO productDTO) {
        ProductResponseDTO createdProduct = prodServ.createProduct(productDTO);
        return ResponseEntity.created(URI.create("/api/products/" + createdProduct.productId())).body(createdProduct);
    }

    @Operation(
            summary = "Update a product",
            description = "Replaces the full product information. The product ID is taken from the path."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode= "409", description = "Product code already exists or product is discontinued")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @RequestBody UpdateProductDTO productDTO) {
        return ResponseEntity.ok(prodServ.updateProduct(id, productDTO));
    }

    @Operation(
            summary = "Discontinue a product",
            description = "Discontinue a product by its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product discontinued"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode= "409", description = "Product already discontinued")
    })
    @PutMapping("/discontinue/{id}")
    public ResponseEntity<ProductResponseDTO> discontinueProduct(@PathVariable Long id) {
        return ResponseEntity.ok().body(prodServ.discontinueProduct(id));

    }

    @Operation(
            summary = "Activate a product",
            description = "Activate a discontinued product by its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product activated"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode= "409", description = "Product is already active")
    })
    @PutMapping("/activate/{id}")
    public ResponseEntity<ProductResponseDTO> activateProduct(@PathVariable Long id) {
        return ResponseEntity.ok().body(prodServ.activateProduct(id));
    }

    @Hidden
    @PutMapping("/decrease-stock/{id}")
    public ResponseEntity<ProductResponseDTO> decreaseProductStock(@PathVariable Long id, @RequestBody Integer quantity) {
        return ResponseEntity.ok(prodServ.decreaseProductStock(id, quantity));
    }

    @Hidden
    @PutMapping("/restore-stock/{id}")
    public ResponseEntity<ProductResponseDTO> increaseProductStock(@PathVariable Long id, @RequestBody Integer quantity) {
        return ResponseEntity.ok(prodServ.increaseProductStock(id, quantity));
    }

}
