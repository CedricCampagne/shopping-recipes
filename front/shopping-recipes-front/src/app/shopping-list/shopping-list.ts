import { Component, inject, signal } from '@angular/core';
import { shoppingListService } from './services/shopping-list.service';
import { CreateShoppingRequest } from './models/create-shopping-list-request';

@Component({
  selector: 'app-shopping-list',
  standalone: true,
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

  saved = signal(false);

  // sauvegarder la list
  saveList(){
    const request: CreateShoppingRequest = {
      recipes: this.recipes().map(r=> ({
        recipeId: r.recipeId,
        servings: r.servings
      }))
    };

    this.shoppingListService.createShoppingList(request).subscribe({
      next:(res) => {
        console.log("Liste sauvegardée :", res);
        this.saved.set(true);
        
        setTimeout(() =>{
          this.saved.set(false);
          this.shoppingListService.clear();
        } , 2000);
      },
      error: (err) => {
        console.error("Erreur lors de la sauvegarde :", err);
      }
    });
  }

  deleteRecipe(uid:number) {
    console.log("Supprimer la recette de la liste");
    this.shoppingListService.deleteRecipeAndItems(uid);
  }

  clear() {
    this.shoppingListService.clear();
  }

}
