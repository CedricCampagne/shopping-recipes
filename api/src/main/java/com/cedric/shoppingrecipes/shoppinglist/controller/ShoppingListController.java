package com.cedric.shoppingrecipes.shoppinglist.controller;

import com.cedric.shoppingrecipes.shoppinglist.dto.CreateShoppingListRequest;
import com.cedric.shoppingrecipes.shoppinglist.dto.ShoppingListResponse;

import com.cedric.shoppingrecipes.shoppinglist.dto.UpdateStatusRequest;
import com.cedric.shoppingrecipes.shoppinglist.mapper.ShoppingListMapper;

import com.cedric.shoppingrecipes.shoppinglist.entity.ShoppingList;

import com.cedric.shoppingrecipes.shoppinglist.service.ShoppingListService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shopping-lists")
@RequiredArgsConstructor
public class ShoppingListController {

    private final ShoppingListService shoppingListService;
    private final ShoppingListMapper shoppingListMapper;

    @PostMapping
    public ShoppingListResponse create(@RequestBody CreateShoppingListRequest request) {
        return shoppingListService.createShoppingList(request);
    }


    @GetMapping
    public List<ShoppingListResponse> getAll() {
        return shoppingListService.getAll();
    }

    @GetMapping("/{id}")
    public ShoppingListResponse getById(@PathVariable Long id) {
       return  shoppingListService.getById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteList(@PathVariable Long id) {
        shoppingListService.deleteShoppingList(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status")
    public ShoppingListResponse updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateStatusRequest request
    ) {
        return shoppingListService.updateStatus(id, request.status());
    }
}