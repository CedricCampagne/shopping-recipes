package com.cedric.shoppingrecipes.shoppinglist.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import com.cedric.shoppingrecipes.shoppinglist.ShoppingListStatus;
import com.cedric.shoppingrecipes.shoppinglist.exception.ShoppingListNotFoundException;
import com.cedric.shoppingrecipes.shoppinglist.repository.ShoppingListRepository;
import com.cedric.shoppingrecipes.user.entity.User;
import com.cedric.shoppingrecipes.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class ShoppingListServiceOwnershipTest {

    private static final Long LIST_ID = 10L;
    private static final Long USER_B_ID = 2L;

    @Mock private UserRepository userRepository;
    @Mock private ShoppingListRepository shoppingListRepository;

    @InjectMocks private ShoppingListService shoppingListService;

    @BeforeEach
    void setUp() {
        // L'utilisateur B est connecté
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("userb@example.com", null, List.of()));

        User userB = new User();
        ReflectionTestUtils.setField(userB, "id", USER_B_ID);
        userB.setEmail("userb@example.com");
        when(userRepository.findByEmail("userb@example.com")).thenReturn(Optional.of(userB));

        // Pour B, la liste 10 (qui appartient à quelqu'un d'autre) n'existe pas
        when(shoppingListRepository.findByIdAndUserId(LIST_ID, USER_B_ID)).thenReturn(Optional.empty());
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getById_shouldThrowNotFound_whenListBelongsToAnotherUser() {
        assertThrows(ShoppingListNotFoundException.class,
                () -> shoppingListService.getById(LIST_ID));

        verify(shoppingListRepository).findByIdAndUserId(LIST_ID, USER_B_ID);
    }

    @Test
    void deleteShoppingList_shouldThrowNotFoundAndDeleteNothing_whenListBelongsToAnotherUser() {
        assertThrows(ShoppingListNotFoundException.class,
                () -> shoppingListService.deleteShoppingList(LIST_ID));

        verify(shoppingListRepository, never()).delete(any());
    }

    @Test
    void updateStatus_shouldThrowNotFound_whenListBelongsToAnotherUser() {
        assertThrows(ShoppingListNotFoundException.class,
                () -> shoppingListService.updateStatus(LIST_ID, ShoppingListStatus.ARCHIVE));
    }
}