import { Component, inject, signal, computed } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RecipesServices } from '../services/recipes.service';
import { toSignal } from '@angular/core/rxjs-interop';
import { CommonModule } from '@angular/common';
import { RecipeIgredientService } from '../services/recipe-ingredient.service';
import { RecipeIngredient } from '../models/recipe-ingredient';
import { shoppingListService } from '../../shopping-list/services/shopping-list.service';

@Component({
  selector: 'app-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './detail.html',
  styleUrl: './detail.css',
})
export class Detail {

  private route = inject(ActivatedRoute);
  private recipeService = inject(RecipesServices);
  private recipeIngredientSerivce = inject(RecipeIgredientService);
  private shoppingListService = inject(shoppingListService);

  private id = Number(this.route.snapshot.paramMap.get('id'));

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
    const items = this.ingredientsWithTotal();
    this.shoppingListService.addItems(items);
    this.shoppingListService.addRecipe(
      this.id,
      this.servings(),
      this.recipe()!.name
    );
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
