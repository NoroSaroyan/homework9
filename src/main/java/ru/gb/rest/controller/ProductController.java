package ru.gb.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.rest.exception.ApiError;
import ru.gb.rest.repository.ProductRepository;
import ru.gb.rest.entity.Product;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> findAll() {
        List<Product> allProducts = new ArrayList<>();
        repository.findAll().forEach(p -> allProducts.add(p));
        return ResponseEntity.ok(allProducts);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> findById(@PathVariable long id) {
        Optional<Product> maybeProduct = repository.findById((int) id);
        if (maybeProduct.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(maybeProduct.get());
    }

    @PostMapping("/products")
    public ResponseEntity<Product> save(@RequestBody Product product) {
        Product newProduct = repository.save(product);
        return ResponseEntity
                .created(URI.create("/products" + newProduct.getId())).body(newProduct);

    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Product> deleteById(@PathVariable long id) {
        Optional<Product> maybeProduct = repository.findById((int) id);
        if (maybeProduct.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById((int) id); // сомневаюсь, что это правильно
        return ResponseEntity.ok(maybeProduct.get());
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handlerException(RuntimeException exception) {
        return ResponseEntity.internalServerError()
                .body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage()));
    }

}
