import { Component, inject, signal, computed } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RecipesServices } from '../services/recipes.service';
import { toSignal } from '@angular/core/rxjs-interop';
import { CommonModule } from '@angular/common';
import { RecipeIgredientService } from '../services/recipe-ingredient.service';
import { RecipeIngredient } from '../models/recipe-ingredient';
import { shoppingListService } from '../../shopping-list/services/shopping-list.service';
import { UIStore } from '../../shared/ui.store';

@Component({
  selector: 'app-recipe-detail',
  standalone: true,
  imports: [CommonModule],
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
    {initialValue: null}
  );

  // Ingredients
  recipeIngredient = toSignal(
    this.recipeIngredientSerivce.getByRecipeId(this.id),
    { initialValue: [] as RecipeIngredient[] }
  );

  // Portions modifables par le user
  servings = signal(4);

  // Quantités recalculées si changement de serving
  ingredientsWithTotal = computed(()=>{
    const base: RecipeIngredient[] = this.recipeIngredient();
    const s = this.servings();
    return base.map(ing =>({
      ...ing,
      total: ing.quantityPerPerson * s
    }));
  });

  onServingsChange(event: Event) {
    const input = event.target as HTMLInputElement;
    this.servings.set(Number(input.value));
  }

  shoppingList = signal<RecipeIngredient[]>([]);

  addToShoppingList() {
    this.ui.startLoading();

    const items = this.ingredientsWithTotal();

    try {
      this.shoppingListService.addRecipe(
        this.id,
        this.servings(),
        this.recipe()!.name,
        items
      );
    
    this.ui.showSuccess("Recette ajoutée à la liste !");
    
    setTimeout(()=> {
      this.ui.stopLoading();
      this.router.navigate(['/app/recipes']);
    }, 1200 );
    } catch {
      this.ui.showError("Erreur lors de l'ajout à la liste");
      this.ui.stopLoading();
    }
  }

  createShoppingList() {
    // creation objet request
    const request = {
      recipes: [
        {
          recipeId: this.id,
          servings: this.servings()
        }
      ]
    };

    this.shoppingListService.createShoppingList(request).subscribe({
      next: (response) => {
        console.log("Liste créée :", response);
      }
    });
  }
}
