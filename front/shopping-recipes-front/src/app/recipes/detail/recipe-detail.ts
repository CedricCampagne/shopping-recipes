import { Component, inject, signal, computed } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RecipesServices } from '../services/recipes.service';
import { toSignal } from '@angular/core/rxjs-interop';
import { CommonModule } from '@angular/common';
import { RecipeIgredientService } from '../services/recipe-ingredient.service';
import { RecipeIngredient } from '../models/recipe-ingredient';
import { shoppingListService } from '../../shopping-list/services/shopping-list.service';

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

  // si recette deja ajoutée
  added = signal(false);

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
    this.shoppingListService.addRecipe(
      this.id,
      this.servings(),
      this.recipe()!.name,
      items
    );
    
    this.added.set(true);
    setTimeout(()=> {
      this.added.set(false), 
      this.router.navigate(['/app/recipes']);
    }, 1000 );
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
