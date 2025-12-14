package com.example.demo;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SweetsService {

    private final SweetsRepository sweetsRepository;

    public SweetsService(SweetsRepository sweetsRepository) {
        this.sweetsRepository = sweetsRepository;
    }

    public List<Sweet> getAllSweets() {
        return sweetsRepository.findAll();
    }
    public Sweet addSweet(Sweet sweet) {
        return sweetsRepository.save(sweet);
    }
    public Sweet updateSweet(String id, Sweet sweet) {
        sweet.setId(id);
        return sweetsRepository.save(sweet);
    }
    public void deleteSweet(String id) {
        sweetsRepository.deleteById(id);
    }
    public List<Sweet> searchSweets(String category) {
        return sweetsRepository.findByCategory(category);
    }
    public Sweet reduceStock(String id, int amount) {
        Sweet sweet = sweetsRepository.findById(id).orElseThrow();
        if (amount > sweet.getQuantity()) {
            throw new IllegalArgumentException("Not enough stock");
        }

        sweet.setQuantity(sweet.getQuantity() - amount);
        return sweetsRepository.save(sweet);
    }
    public java.util.Optional<Sweet> getSweetById(String id) {
        return sweetsRepository.findById(id);
    }
    public Sweet restockSweet(String id, int amount) {
        Sweet sweet = sweetsRepository.findById(id).orElseThrow();
        sweet.setQuantity(sweet.getQuantity() + amount);
        return sweetsRepository.save(sweet);
    }
}