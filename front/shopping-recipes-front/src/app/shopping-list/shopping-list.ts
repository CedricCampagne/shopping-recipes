import { Component, inject, signal } from '@angular/core';
import { shoppingListService } from './services/shopping-list.service';
import { CreateShoppingRequest } from './models/create-shopping-list-request';
import { UIStore } from '../shared/ui.store';

@Component({
  selector: 'app-shopping-list',
  standalone: true,
  imports: [],
  templateUrl: './shopping-list.html',
  styleUrl: './shopping-list.css',
})
export class ShoppingList {
  private shoppingListService = inject(shoppingListService);
  ui = inject(UIStore);

  // ingredients fussionnés
  //items = this.shoppingListService.mergedItems;
  items = this.shoppingListService.sortedMergedItems;

  // recetttes ajoutées
  recipes = this.shoppingListService.recipes;

  saved = signal(false);

  // sauvegarder la list
  saveList() {
    this.ui.clearMessage();
    this.ui.startLoading();

    const request: CreateShoppingRequest = {
      recipes: this.recipes().map((r) => ({
        recipeId: r.recipeId,
        servings: r.servings,
      })),
    };

    this.shoppingListService.createShoppingList(request).subscribe({
      next: (res) => {
        setTimeout(() => {
          this.ui.stopLoading();
          console.log('Liste sauvegardée :', res);
          this.ui.showSuccess('Liste sauvegardée avec succès.');
        }, 800);

        setTimeout(() => {
          // refresh des listes sauvegardées
          this.shoppingListService.refreshSavedLists();
          // this.saved.set(false);
          this.shoppingListService.clear();
        }, 1400);
      },
      error: (err) => {
        console.error('Erreur lors de la sauvegarde :', err);
        setTimeout(() => {
          this.ui.stopLoading();
          if (err.status === 403 || err.status === 404) {
            const recipeId = err.error.resourceId;
            const recipe = this.recipes().find((r) => r.recipeId === recipeId);

            if (recipe) {
              this.shoppingListService.deleteRecipeAndItems(recipe.uid);
            }

            this.ui.showError(
              `La recette (${recipe?.name}) n'existe plus. Elle a été retirée de la liste`,
            );
          }
        }, 800);
        return;
      },
    });
  }

  deleteRecipe(uid: number) {
    console.log('Supprimer la recette de la liste');
    this.shoppingListService.deleteRecipeAndItems(uid);
  }

  clear() {
    this.shoppingListService.clear();
  }

  increaseServing(uid: number) {
    const recipe = this.recipes().find((r) => r.uid === uid);
    if (!recipe) return;
    this.shoppingListService.updateServings(uid, recipe.servings + 1);
  }

  decreaseServings(uid: number) {
    const recipe = this.recipes().find((r) => r.uid === uid);
    if (!recipe) return;
    const newValue = recipe.servings > 1 ? recipe.servings - 1 : 1;
    this.shoppingListService.updateServings(uid, newValue);
  }
}
