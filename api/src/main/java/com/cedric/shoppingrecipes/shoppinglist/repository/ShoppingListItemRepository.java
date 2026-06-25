package com.cedric.shoppingrecipes.shoppinglist.repository;

import com.cedric.shoppingrecipes.shoppinglist.entity.ShoppingListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, Long> {
}