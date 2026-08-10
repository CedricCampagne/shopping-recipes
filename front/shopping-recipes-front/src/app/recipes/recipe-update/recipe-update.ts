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
import { Ingredient } from '../models/ingredient';
import { IngredientsService } from '../../ingredient/services/ingredients.service';
import { UIStore } from '../../shared/ui.store';

@Component({
  selector: 'app-recipe-update',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './recipe-update.html',
  styleUrl: './recipe-update.css',
})
export class RecipeUpdate {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private recipesService = inject(RecipesServices);
  private recipeIngredientService = inject(RecipeIgredientService);
  private ingredientsService = inject(IngredientsService);

  ui = inject(UIStore);

  recipeToUpdate = signal<Recipe | null>(null);
  recipeIngredients = signal<RecipeIngredient[]>([]);
  updateIngredients = signal<UpdateRecipeIngredientRequest[]>([]);

  allIngredients = signal<Ingredient[]>([]);
  selectedIngredient = signal<number | null>(null);

  id = 0;

  form = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.minLength(5),
      Validators.pattern(/^\S.*$/),
    ]),
    description: new FormControl('', [
      Validators.required,
      Validators.minLength(15),
      Validators.pattern(/^\S.*$/),
    ]),
    servings: new FormControl(4, [Validators.required, Validators.min(1)]),
  });

  ngOnInit(): void {
    this.id = Number(this.route.snapshot.params['id']);

    // Charge la recette (sans les ingrédients)
    this.recipesService.getById(this.id).subscribe((res) => {
      this.recipeToUpdate.set(res);
      this.patchFormRecipe(res);
    });

    //Charge les ingrédients de la recette
    this.recipeIngredientService.getByRecipeId(this.id).subscribe((res) => {
      this.recipeIngredients.set(res);
      this.patchFormIngredients(res);
    });

    //Charge tousles ingrédients dela bdd (pour pouvoir les ajouter)
    this.ingredientsService.getAllIngredients().subscribe((res) => {
      this.allIngredients.set(res);
    });
  }

  // Remplit les champs simples du formulaire
  patchFormRecipe(recipe: Recipe) {
    this.form.patchValue({
      name: recipe.name,
      description: recipe.description,
      servings: recipe.servings,
    });
  }

  // Convertit les RecipeIngredient → UpdateRecipeIngredientRequest
  patchFormIngredients(ingredients: RecipeIngredient[]) {
    this.updateIngredients.set(
      ingredients.map((ing) => ({
        id: ing.id,
        ingredientId: ing.ingredient.id,
        ingredientName: ing.ingredient.name,
        quantityPerPerson: ing.quantityPerPerson,
        unit: ing.unit,
      })),
    );
  }

  onQuantityChange(index: number, event: Event) {
    const value = Number((event.target as HTMLInputElement).value);

    this.updateIngredients.update((list) =>
      list.map((ing, i) => (i === index ? { ...ing, quantityPerPerson: value } : ing)),
    );
  }

  // Envoi du PUT au backend
  onSubmit() {
    this.ui.clearMessage();
    this.ui.startLoading();

    //Validation du form
    if (this.form.invalid) {
      this.ui.showError('Le formulaire est incomplet ou invalide.');
      this.form.markAllAsTouched();
      return;
    }

    //Validation liste de RI non vide
    const emptyList = this.updateIngredients().length === 0;

    //validations des quantiés
    const invalidQuantity = this.updateIngredients().some(
      (ing) => ing.quantityPerPerson === null || ing.quantityPerPerson <= 0,
    );

    const invalid = emptyList || invalidQuantity;

    if (invalid) {
      setTimeout(() => {
        this.ui.stopLoading();
        this.ui.showError(
          'Veuillez remplir une quantité valide (> 0) et ajouter au moins un ingrédient.',
        );
      }, 800);
      return;
    }

    const request: UpdateRecipeRequest = {
      name: this.form.value.name!,
      description: this.form.value.description!,
      servings: Number(this.form.value.servings),
      ingredients: this.updateIngredients(),
    };

    this.recipesService.updateRecipe(this.id, request).subscribe({
      next: () => {
        setTimeout(() => {
          this.ui.stopLoading();
          this.ui.showSuccess('Recette mise a jour avec succès !');
        }, 800);

        setTimeout(() => {
          this.router.navigate(['/app/recipes']);
        }, 1400);
      },
      error: (err) => {
        setTimeout(() => {
          this.ui.showError('Erreur lors de la mise à jour.');
          console.error(err);
        }, 800);
      },
    });
  }

  onSelectIngredient(event: Event) {
    const value = Number((event.target as HTMLSelectElement).value);
    // console.log("SELECTED", event.target);
    // console.log("VALUE", value);
    this.selectedIngredient.set(value);
  }

  addIngredientFromDb() {
    const id = this.selectedIngredient();
    if (!id) return;

    const ing = this.allIngredients().find((i) => i.id === id);
    if (!ing) return;

    this.updateIngredients.update((list) => [
      ...list,
      {
        id: null,
        ingredientId: ing.id,
        ingredientName: ing.name,
        quantityPerPerson: null,
        unit: ing.unit,
      },
    ]);

    this.selectedIngredient.set(null);
  }

  removeIngredient(id: number) {
    this.updateIngredients.update((list) => list.filter((ing) => ing.id != id));
  }

  goToRecipe() {
    this.router.navigateByUrl('/app/recipes');
  }
}
