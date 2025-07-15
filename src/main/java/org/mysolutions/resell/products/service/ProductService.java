package org.mysolutions.resell.products.service;

import org.mysolutions.resell.products.entities.Product;
import org.mysolutions.resell.products.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        setDate(product);
        return productRepository.save(product);
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    private void setDate(Product product) {
        product.setCreatedDate(LocalDateTime.now().now());
        product.setUpdatedDate(LocalDateTime.now());
    }

}
