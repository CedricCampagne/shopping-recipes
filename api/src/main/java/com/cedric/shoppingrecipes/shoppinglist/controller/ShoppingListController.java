package com.cedric.shoppingrecipes.shoppinglist.controller;

import com.cedric.shoppingrecipes.shoppinglist.dto.CreateShoppingListRequest;
import com.cedric.shoppingrecipes.shoppinglist.dto.ShoppingListResponse;

import com.cedric.shoppingrecipes.shoppinglist.mapper.ShoppingListMapper;

import com.cedric.shoppingrecipes.shoppinglist.entity.ShoppingList;

import com.cedric.shoppingrecipes.shoppinglist.service.ShoppingListService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shopping-lists")
@RequiredArgsConstructor
public class ShoppingListController {

    private final ShoppingListService shoppingListService;
    private final ShoppingListMapper shoppingListMapper;

    @PostMapping
    public ShoppingListResponse create(@RequestBody CreateShoppingListRequest request) {
        System.out.println("RECIPES = " + request.recipes());
        ShoppingList list = shoppingListService.createShoppingList(request);

        return shoppingListMapper.toResponse(list);
    }
}