import { Component, inject } from '@angular/core';
import { shoppingListService } from './services/shopping-list.service';

@Component({
  selector: 'app-shopping-list',
  imports: [],
  templateUrl: './shopping-list.html',
  styleUrl: './shopping-list.css',
})
export class ShoppingList {

  private shoppingListService = inject(shoppingListService);

  // ingredients fussionnés
  items = this.shoppingListService.mergedItems;

  // recetttes ajoutées
  recipes = this.shoppingListService.recipes;

  // sauvegarder la list
  saveList(){

  }

  
  clear() {
    this.shoppingListService.clear();
  }


}
