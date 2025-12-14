package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc; // NEW IMPORT
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional; // Needed for Optional.of()

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@WebMvcTest(SweetsController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SweetsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SweetsService sweetsService;

    @Test
    void shouldReturnListContainingSweets() throws Exception {
        when(sweetsService.getAllSweets()).thenReturn(Arrays.asList(
                new Sweet("1", "Chocolate", "Candy", 2.99, 100)
        ));

        mockMvc.perform(get("/api/sweets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Chocolate")));
    }
    @Test
    void shouldCreateNewSweet() throws Exception {
        Sweet savedSweet = new Sweet("2", "Lollipop", "Candy", 0.50, 200);
        when(sweetsService.addSweet(any(Sweet.class))).thenReturn(savedSweet);
        mockMvc.perform(post("/api/sweets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Lollipop\",\"category\":\"Candy\",\"price\":0.50,\"quantity\":200}"))
                .andExpect(status().isCreated()) // Expect HTTP 201 Created
                .andExpect(jsonPath("$.id", is("2")))
                .andExpect(jsonPath("$.name", is("Lollipop")));
    }
    @Test
    void shouldReturn400WhenCreateInvalidSweet() throws Exception {
        mockMvc.perform(post("/api/sweets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"category\":\"Candy\",\"price\":-10.0,\"quantity\":200}"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void shouldUpdateExistingSweet() throws Exception {
        Sweet updatedSweet = new Sweet("1", "Dark Chocolate", "Candy", 3.50, 50);
        when(sweetsService.updateSweet(eq("1"), any(Sweet.class))).thenReturn(updatedSweet);

        mockMvc.perform(put("/api/sweets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Dark Chocolate\",\"category\":\"Candy\",\"price\":3.50,\"quantity\":50}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Dark Chocolate")))
                .andExpect(jsonPath("$.price", is(3.50)));
    }

    @Test
    void shouldDeleteSweet() throws Exception {
        mockMvc.perform(delete("/api/sweets/1"))
                .andExpect(status().isNoContent());
    }
    @Test
    void shouldSearchSweetsByCategory() throws Exception {
        when(sweetsService.searchSweets("Candy")).thenReturn(Arrays.asList(
                new Sweet("1", "Chocolate", "Candy", 2.99, 100)
        ));

        mockMvc.perform(get("/api/sweets/search?category=Candy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category", is("Candy")));
    }
    @Test
    void shouldReduceStockWhenOrderingSweet() throws Exception {
        Sweet updatedSweet = new Sweet("1", "Chocolate", "Candy", 2.99, 90);
        when(sweetsService.reduceStock("1", 10)).thenReturn(updatedSweet);
        mockMvc.perform(post("/api/sweets/1/purchase?amount=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity", is(90)));
    }
    @Test
    void shouldReturn400WhenOrderingTooMany() throws Exception {
        when(sweetsService.reduceStock("1", 1000))
                .thenThrow(new IllegalArgumentException("Not enough stock"));

        mockMvc.perform(post("/api/sweets/1/purchase?amount=1000"))
                .andExpect(status().isBadRequest()); // Expect 400
    }
    @Test
    void shouldReturnSingleSweet() throws Exception {
        Sweet sweet = new Sweet("1", "Chocolate", "Candy", 2.99, 100);
        when(sweetsService.getSweetById("1")).thenReturn(java.util.Optional.of(sweet));

        mockMvc.perform(get("/api/sweets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Chocolate")));
    }
    @Test
    void shouldRestockSweet() throws Exception {
        Sweet restockedSweet = new Sweet("1", "Chocolate", "Candy", 2.99, 60);
        when(sweetsService.restockSweet("1", 50)).thenReturn(restockedSweet);

        mockMvc.perform(post("/api/sweets/1/restock?amount=50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity", is(60)));
    }
}