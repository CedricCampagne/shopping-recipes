package com.cedric.shoppingrecipes.shoppinglist.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cedric.shoppingrecipes.exception.GlobalExceptionHandler;
import com.cedric.shoppingrecipes.shoppinglist.ShoppingListStatus;
import com.cedric.shoppingrecipes.shoppinglist.mapper.ShoppingListMapper;
import com.cedric.shoppingrecipes.shoppinglist.service.ShoppingListService;

class ShoppingListControllerTest {

    private MockMvc mockMvc;
    private ShoppingListService shoppingListService;

    @BeforeEach
    void setUp() {
        shoppingListService = mock(ShoppingListService.class);
        ShoppingListController controller =
                new ShoppingListController(shoppingListService, mock(ShoppingListMapper.class));
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void updateStatus_shouldReturn400_whenStatusIsMissing() throws Exception {
        mockMvc.perform(put("/shopping-lists/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Le statut est obligatoire"));

        verifyNoInteractions(shoppingListService);
    }

    @Test
    void updateStatus_shouldCallService_whenStatusIsValid() throws Exception {
        mockMvc.perform(put("/shopping-lists/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"ARCHIVE\"}"))
                .andExpect(status().isOk());

        verify(shoppingListService).updateStatus(1L, ShoppingListStatus.ARCHIVE);
    }
}