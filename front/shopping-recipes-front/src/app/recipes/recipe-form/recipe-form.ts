import { Component, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { RecipesServices } from '../services/recipes.service';
import { IngredientsService } from '../../ingredient/services/ingredients.service';
import { IngredientResponse } from '../../ingredient/models/ingredient-response';
import { FormControl, FormControlName, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RecipeIngredientFront } from '../models/recipe-ingredient-front';
import { UIStore } from '../../shared/ui.store';
import { UiMessages } from "../../shared/ui-messages/ui-messages";
import { CreateRecipeRequest } from '../models/create-recipe-request';

@Component({
  selector: 'app-recipe-form',
  imports: [ReactiveFormsModule, UiMessages],
  templateUrl: './recipe-form.html',
  styleUrl: './recipe-form.css',
})
export class RecipeForm {

  router = inject(Router) ;
  recipesService = inject(RecipesServices);
  ingredientsService = inject(IngredientsService);
  ui = inject(UIStore);

  ingredients = signal<IngredientResponse[]>([]);
  units = signal<string[]>([]);
  recipeIngredients = signal<RecipeIngredientFront[]>([]);

  selectedUnit = signal('');

  form = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.minLength(5)]),
    description: new FormControl('', [Validators.required, Validators.minLength(15)]),
    servings: new FormControl(4,[Validators.required,Validators.min(1)]),
    ingredientSelect: new FormControl<number | null>(null),
    ingredientQuantityPerPerson: new FormControl(null, [Validators.required, Validators.min(0.01)])
  });

  constructor(){
    this.loadIngredients();
  }

  ngOnInit() {
    this.ingredientsService.getUnits().subscribe(units =>{
      this.units.set(units);
    });

    this.form.controls.ingredientSelect.valueChanges.subscribe(id => {
    const ingredient = this.ingredients().find(i => i.id === id);
    this.selectedUnit.set(ingredient?.unit ?? '');

    this.form.controls.ingredientQuantityPerPerson.setValue(null);
    });
  }

  loadIngredients() {
    this.ingredientsService.getAllIngredients().subscribe(res => {
      this.ingredients.set(res);

      if (res.length > 0) {
      this.form.patchValue({
        ingredientSelect: res[0].id
      });
    }
    });
  }

  onSubmit() {
    const request: CreateRecipeRequest = {
      name: this.form.controls.name.value!,
      description: this.form.controls.description.value!,
      servings: this.form.controls.servings.value!,
      ingredients: this.recipeIngredients().map(ri => ({
        ingredientId: ri.ingredientId,
        quantityPerPerson: ri.quantityPerPerson,
        unit: ri.unit
      }))
    };
    console.log(request);
    
    this.ui.startLoading();

    this.recipesService.createRecipe(request).subscribe({
      next: () => {
        setTimeout(() => {
          this.ui.stopLoading();
          this.ui.showSuccess("Recette créée avec succès !");

          setTimeout(() => {
            this.form.reset({
              servings: 4,
              ingredientSelect: this.ingredients()[0]?.id ?? null,
              ingredientQuantityPerPerson: null
            });

            this.recipeIngredients.set([]);
            this.selectedUnit.set('');

            this.loadIngredients();

            this.router.navigate(['/app/recipes']);
          }, 1200);
        }, 800);
      },
      error: (err) => {
        this.ui.stopLoading();
        this.ui.showError("Erreur lorrs de la création de la recette.");
        console.error(err);
      }
    });
  }

  addIngredient(){
    const ingreduentId = this.form.value.ingredientSelect;
    const quantity = this.form.value.ingredientQuantityPerPerson;

    if(!ingreduentId || !quantity) {
      this.ui.showError("Sélectionne un ingrédient et une quantité.");
      return;
    }

    const ingredient = this.ingredients().find(i => i.id === ingreduentId);
    if(!ingredient) return;

    this.recipeIngredients.update(list => [
      ...list,
      {
        index: list.length,
        ingredientId: ingredient.id,
        name: ingredient.name,
        unit: ingredient.unit,
        quantityPerPerson: quantity
      }
    ]);

    this.form.controls.ingredientQuantityPerPerson.setValue(null);
  }

  removeIngredient(index: number) {
    this.recipeIngredients.update(list =>
      list.filter(i => i.index !== index)
    );
  }

}
