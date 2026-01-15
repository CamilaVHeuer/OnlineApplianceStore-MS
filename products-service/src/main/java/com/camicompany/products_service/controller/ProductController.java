package com.camicompany.products_service.controller;

import com.camicompany.products_service.dto.ProductDTO;
import com.camicompany.products_service.service.IProductService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    public ResponseEntity<List<ProductDTO>> getAllProducts() {

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
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
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
    public ResponseEntity<ProductDTO> getProductByCode(@PathVariable String code) {
        return ResponseEntity.ok(prodServ.getProductByCode(code));}

    @Operation(
            summary = "Get products with low stock",
            description = "Returns products whose stock is below the defined threshold"
    )
    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductDTO>> getProductsLowStock() {
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
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = prodServ.createProduct(productDTO);
        return ResponseEntity.created(URI.create("/api/products/" + createdProduct.getProductId())).body(createdProduct);
    }

    @Operation(
            summary = "Update a product",
            description = "Replaces the full product information. The product ID is taken from the path."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode= "409", description = "Product code already exists")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(prodServ.updateProduct(id, productDTO));
    }

    @Operation(
            summary = "Delete a product",
            description = "Deletes a product by its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        prodServ.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Hidden
    @PutMapping("/decrease-stock/{id}")
    public ResponseEntity<ProductDTO> decreaseProductStock(@PathVariable Long id, @RequestBody Integer quantity) {
        return ResponseEntity.ok(prodServ.decreaseProductStock(id, quantity));
    }

    @Hidden
    @PutMapping("/restore-stock/{id}")
    public ResponseEntity<ProductDTO> increaseProductStock(@PathVariable Long id, @RequestBody Integer quantity) {
        return ResponseEntity.ok(prodServ.increaseProductStock(id, quantity));
    }

}
