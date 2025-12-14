package com.example.demo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SweetsRepository extends MongoRepository<Sweet, String> {
    List<Sweet> findByCategory(String category);
}