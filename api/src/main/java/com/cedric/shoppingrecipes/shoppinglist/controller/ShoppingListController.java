package com.cedric.shoppingrecipes.shoppinglist.controller;

import com.cedric.shoppingrecipes.shoppinglist.dto.CreateShoppingListRequest;
import com.cedric.shoppingrecipes.shoppinglist.dto.ShoppingListResponse;

import com.cedric.shoppingrecipes.shoppinglist.mapper.ShoppingListMapper;

import com.cedric.shoppingrecipes.shoppinglist.entity.ShoppingList;

import com.cedric.shoppingrecipes.shoppinglist.service.ShoppingListService;

import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shopping-lists")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShoppingListController {

    private final ShoppingListService shoppingListService;
    private final ShoppingListMapper shoppingListMapper;

    @PostMapping
    public ShoppingListResponse create(@RequestBody CreateShoppingListRequest request) {
        // System.out.println("RECIPES = " + request.recipes());
        ShoppingList list = shoppingListService.createShoppingList(request);

        return shoppingListMapper.toResponse(list);
    }

    @GetMapping
    public List<ShoppingListResponse> getAll() {

        return shoppingListService.getAll()
                .stream()
                .map(shoppingListMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ShoppingListResponse getById(@PathVariable Long id) {
        ShoppingList list = shoppingListService.getById(id);
        return shoppingListMapper.toResponse(list);
    }

}