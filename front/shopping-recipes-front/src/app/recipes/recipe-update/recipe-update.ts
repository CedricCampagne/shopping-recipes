import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RecipesServices } from '../services/recipes.service';
import { RecipeIgredientService } from '../services/recipe-ingredient.service';
import { Recipe } from '../models/recipe';
import { RecipeIngredient } from '../models/recipe-ingredient';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { FormGroup, FormControl } from '@angular/forms';
import { UpdateRecipeIngredientRequest } from '../models/update-recipe-ingredient-request';
import { Validators } from '@angular/forms';
import { UpdateRecipeRequest } from '../models/update-recipe-request';
import { UiMessages } from "../../shared/ui-messages/ui-messages";

@Component({
  selector: 'app-recipe-update',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, UiMessages],
  templateUrl: './recipe-update.html',
  styleUrl: './recipe-update.css',
})
export class RecipeUpdate {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private recipesService = inject(RecipesServices);
  private recipeIngredientService = inject(RecipeIgredientService);

  recipeToUpdate = signal<Recipe | null>(null);
  recipeIngredients = signal<RecipeIngredient[]>([]);
  updateIngredients = signal<UpdateRecipeIngredientRequest[]>([]);

  id = 0;

  form = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.minLength(5), Validators.pattern(/^\S.*$/)]),
    description: new FormControl('', [Validators.required, Validators.minLength(15), Validators.pattern(/^\S.*$/)]),
    servings: new FormControl(4, [Validators.required,Validators.min(1)]),
  });

  ngOnInit(): void {
    this.id = Number(this.route.snapshot.params['id']);

    // Charge la recette (sans les ingrédients)
    this.recipesService.getById(this.id).subscribe(res => {
      this.recipeToUpdate.set(res);
      this.patchFormRecipe(res);
    });

    //Charge les ingrédients
    this.recipeIngredientService.getByRecipeId(this.id).subscribe(res =>{
      this.recipeIngredients.set(res);
      this.patchFormIngredients(res);
    });
  }

  // Remplit les champs simples du formulaire
  patchFormRecipe(recipe: Recipe) {
    this.form.patchValue({
      name: recipe.name,
      description: recipe.description,
      servings: recipe.servings
    });
  }

  // Convertit les RecipeIngredient → UpdateRecipeIngredientRequest
  patchFormIngredients(ingredients: RecipeIngredient[]) {
    this.updateIngredients.set(
      ingredients.map(ing =>({
        id: ing.id,
        ingredientId: ing.ingredient.id,
        ingredientName: ing.ingredient.name,
        quantityPerPerson: ing.quantityPerPerson,
        unit: ing.unit
      }))
    );
  }

  onQuantityChange(id: number, event: Event) {
    const value = Number((event.target as HTMLInputElement).value);

    this.updateIngredients.update(list =>
      list.map(ing =>
        ing.id === id
          ? { ...ing, quantityPerPerson: value }
          : ing
      )
    );
  }

  // Envoi du PUT au backend
  onSubmit() {
    const request: UpdateRecipeRequest = {
      name: this.form.value.name!,
      description: this.form.value.description!,
      servings: Number(this.form.value.servings),
      ingredients: this.updateIngredients()
    };

    this.recipesService.updateRecipe(this.id, request).subscribe({
      next: () => {
        this.router.navigate(['/app/recipes']);
      }
    });
  }
}
