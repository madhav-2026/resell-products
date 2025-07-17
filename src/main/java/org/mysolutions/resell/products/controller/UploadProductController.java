package org.mysolutions.resell.products.controller;

import lombok.extern.slf4j.Slf4j;
import org.mysolutions.resell.products.entities.Product;
import org.mysolutions.resell.products.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
@Slf4j
public class UploadProductController {

    @Autowired
    private ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<String> uploadProduct(
            @RequestParam("name") String name,
            @RequestParam("price") double price,
            @RequestParam("quantity") String quantity,
            @RequestParam("image") MultipartFile imageFile) {

        try {
            String uploadDir = "C:/Project/uploads/"; // Make sure this folder exists

            // Create folder if not exists
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            // Save image with UUID
            String imageName = UUID.randomUUID() + "-" + imageFile.getOriginalFilename();
            Path imagePath = Paths.get(uploadDir, imageName);
            Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

            // Save product in DB with image name
            Product product = new Product();
            product.setName(name);
            product.setPrice(price);
            product.setQuantity(quantity);
            product.setImage("images/" + imageName); // Important: set public path

            productRepository.save(product);

            return ResponseEntity.ok("Product uploaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
        }
    }
}
