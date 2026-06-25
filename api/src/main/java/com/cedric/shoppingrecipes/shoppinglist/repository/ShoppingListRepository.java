package com.cedric.shoppingrecipes.shoppinglist.repository;

import com.cedric.shoppingrecipes.shoppinglist.entity.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {
}