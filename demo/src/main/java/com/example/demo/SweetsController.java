package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.access.prepost.PreAuthorize;
@RestController
@RequestMapping("/api/sweets")

public class SweetsController {

    private final SweetsService sweetsService;

    public SweetsController(SweetsService sweetsService) {
        this.sweetsService = sweetsService;
    }

    @GetMapping
    public List<Sweet> getAllSweets() {
        return sweetsService.getAllSweets();
    }
    @GetMapping("/search")
    public List<Sweet> searchSweets(@RequestParam String category) {
        return sweetsService.searchSweets(category);
    }
    @GetMapping("/{id}")
    public Sweet getSweetById(@PathVariable String id) {
        return sweetsService.getSweetById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sweet not found"));
    }
    @PostMapping("/{id}/purchase")
    public Sweet purchaseSweet(@PathVariable String id, @RequestParam int amount) {
        return sweetsService.reduceStock(id, amount);
    }
    @PostMapping("/{id}/restock")
    @PreAuthorize("hasRole('ADMIN')")
    public Sweet restockSweet(@PathVariable String id, @RequestParam int amount) {
        return sweetsService.restockSweet(id, amount);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Sweet addSweet(@Valid @RequestBody Sweet sweet) { // Added @Valid here
        return sweetsService.addSweet(sweet);
    }
    @PutMapping("/{id}")
    public Sweet updateSweet(@PathVariable String id, @Valid @RequestBody Sweet sweet) {
        return sweetsService.updateSweet(id, sweet);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteSweet(@PathVariable String id) {
        sweetsService.deleteSweet(id);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequests(IllegalArgumentException e) {
        return e.getMessage();
    }
}