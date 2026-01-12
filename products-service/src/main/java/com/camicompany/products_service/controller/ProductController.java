package com.camicompany.products_service.controller;

import com.camicompany.products_service.dto.ProductDTO;
import com.camicompany.products_service.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private IProductService prodServ;



    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {

        return ResponseEntity.ok(prodServ.getProducts());
    }

    @GetMapping ("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(prodServ.getProductById(id));
    }

    @GetMapping ("/code/{code}")
    public ResponseEntity<ProductDTO> getProductByCode(@PathVariable String code) {
        return ResponseEntity.ok(prodServ.getProductByCode(code));}

    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductDTO>> getProductsLowStock() {
        return ResponseEntity.ok(prodServ.getProductsLowStock());
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = prodServ.createProduct(productDTO);
        return ResponseEntity.created(URI.create("/api/products/" + createdProduct.getProductId())).body(createdProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(prodServ.updateProduct(id, productDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        prodServ.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/decrease-stock/{id}")
    public ResponseEntity<ProductDTO> decreaseProductStock(@PathVariable Long id, @RequestBody Integer quantity) {
        return ResponseEntity.ok(prodServ.decreaseProductStock(id, quantity));
    }

    @PutMapping("/restore-stock/{id}")
    public ResponseEntity<ProductDTO> increaseProductStock(@PathVariable Long id, @RequestBody Integer quantity) {
        return ResponseEntity.ok(prodServ.increaseProductStock(id, quantity));
    }

}
