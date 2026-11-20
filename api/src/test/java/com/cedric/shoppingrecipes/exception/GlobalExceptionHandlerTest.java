package com.cedric.shoppingrecipes.exception;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cedric.shoppingrecipes.ingredient.controller.IngredientController;
import com.cedric.shoppingrecipes.ingredient.service.IngredientService;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        IngredientController controller = new IngredientController(mock(IngredientService.class));
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldReturnErrorResponseWhenJsonIsMalformed() throws Exception {
        mockMvc.perform(post("/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Le corps de la requête est invalide ou illisible"))
                .andExpect(jsonPath("$.resourceId").doesNotExist());
    }

    @Test
    void shouldReturnErrorResponseWhenEnumValueIsUnknown() throws Exception {
        mockMvc.perform(post("/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test\",\"unit\":\"banane\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Le corps de la requête est invalide ou illisible"));
    }

    @Test
    void shouldReturnErrorResponseWhenValidationFails() throws Exception {
        mockMvc.perform(post("/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"unit\":\"g\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}