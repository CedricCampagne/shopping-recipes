package com.cedric.shoppingrecipes.config;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.cedric.shoppingrecipes.auth.JwtService;
import com.cedric.shoppingrecipes.recipe.controller.RecipeController;
import com.cedric.shoppingrecipes.recipe.service.RecipeService;
import com.cedric.shoppingrecipes.user.service.UserDetailsServiceImpl;

@WebMvcTest(RecipeController.class)
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void shouldReturn401AndNotReachControllerWhenAnonymousAccessesProtectedRoute() throws Exception {
        mockMvc.perform(get("/recipes"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(recipeService);
    }

    @Test
    void shouldReturn401WhenAnonymousAccessesAuthMe() throws Exception {
        mockMvc.perform(get("/auth/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void shouldAllowAuthenticatedUserOnProtectedRoute() throws Exception {
        mockMvc.perform(get("/recipes"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotBlockPublicAuthRoutes() throws Exception {
        // Aucun contrôleur d'auth n'est chargé dans ce test : si la sécurité laisse passer,
        // Spring répond 404 (route introuvable). Un 401 ou 403 voudrait dire que la sécurité bloque.
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNotFound());
    }
}