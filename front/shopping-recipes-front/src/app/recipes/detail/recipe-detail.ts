import { Component, inject, signal, computed } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RecipesServices } from '../services/recipes.service';
import { toSignal } from '@angular/core/rxjs-interop';
import { CommonModule } from '@angular/common';
import { RecipeIgredientService } from '../services/recipe-ingredient.service';
import { RecipeIngredient } from '../models/recipe-ingredient';
import { shoppingListService } from '../../shopping-list/services/shopping-list.service';
import { UIStore } from '../../shared/ui.store';
import { UiButton } from '../../shared/ui/ui-button/ui-button';
import { UiLink } from '../../shared/ui/ui-link/ui-link';

@Component({
  selector: 'app-recipe-detail',
  standalone: true,
  imports: [CommonModule, UiButton, UiLink],
  templateUrl: './recipe-detail.html',
  styleUrl: './recipe-detail.css',
})
export class RecipeDetail {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private recipeService = inject(RecipesServices);
  private recipeIngredientSerivce = inject(RecipeIgredientService);
  private shoppingListService = inject(shoppingListService);

  private id = Number(this.route.snapshot.paramMap.get('id'));

  ui = inject(UIStore);

  // Recette
  recipe = toSignal(
    this.recipeService.getById(this.id), 
    { initialValue: null }
  );

  // Ingredients
  recipeIngredient = toSignal(
    this.recipeIngredientSerivce.getByRecipeId(this.id),
    {initialValue: [] as RecipeIngredient[],}
  );

  // Portions modifables par le user
  servings = signal(4);

  // Quantités recalculées si changement de serving
  ingredientsWithTotal = computed(() => {
    const base: RecipeIngredient[] = this.recipeIngredient();
    const s = this.servings();
    return base.map((ing) => ({
      ...ing,
      total: ing.quantityPerPerson * s,
    }));
  });

  addToShoppingList() {
    this.ui.clearMessage();
    this.ui.startLoading();

    const items = this.ingredientsWithTotal();

    this.shoppingListService.addRecipe(this.id, this.servings(), this.recipe()!.name, items);

    setTimeout(() => {
      this.ui.stopLoading();
      this.ui.showSuccess('Recette ajoutée à la liste !');
    }, 800);

    setTimeout(() => {
      this.router.navigate(['/app/recipes']);
    }, 1400);
  }

  goBackToRecipes() {
    this.router.navigateByUrl('/app/recipes');
  }

}
