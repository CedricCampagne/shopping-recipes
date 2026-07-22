import { Component, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { RecipesServices } from '../services/recipes.service';
import { IngredientsService } from '../../ingredient/services/ingredients.service';
import { IngredientResponse } from '../../ingredient/models/ingredient-response';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RecipeIngredientFront } from '../models/recipe-ingredient-front';
import { UIStore } from '../../shared/ui.store';
import { CreateRecipeRequest } from '../models/create-recipe-request';
import { IngredientModal } from '../../ingredient/ingredient-modal/ingredient-modal';

@Component({
  selector: 'app-recipe-form',
  imports: [ReactiveFormsModule, IngredientModal],
  templateUrl: './recipe-form.html',
  styleUrl: './recipe-form.css',
})
export class RecipeForm {

  private router = inject(Router) ;
  private recipesService = inject(RecipesServices);
  private ingredientsService = inject(IngredientsService);
  ui = inject(UIStore);

  ingredients = signal<IngredientResponse[]>([]);
  units = signal<string[]>([]);
  recipeIngredients = signal<RecipeIngredientFront[]>([]);

  selectedUnit = signal('');
  showIngredientModal = signal(false);

  form = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.minLength(5), Validators.pattern(/^\S.*$/)]),
    description: new FormControl('', [Validators.required, Validators.minLength(15), Validators.pattern(/^\S.*$/)]),
    servings: new FormControl(4,[Validators.required,Validators.min(1)]),
    ingredientSelect: new FormControl<number | null>(null),
    ingredientQuantityPerPerson: new FormControl(null, [Validators.min(1)])
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
    
    if(this.form.invalid) {
      this.ui.showError("Le formulaire est incomplet ou invalide.");
      this.form.markAllAsTouched();
      return;
    }
    
    if (this.recipeIngredients().length === 0) {
      this.ui.showError("Ajoute au moins un ingrédient à la recette.");
      return;
    }
    
    this.ui.startLoading();

    const name = this.form.controls.name.value!.trim();
    const description = this.form.controls.description.value!.trim();

    const request: CreateRecipeRequest = {
      name,
      description,
      servings: this.form.controls.servings.value!,
      ingredients: this.recipeIngredients().map(ri => ({
        ingredientId: ri.ingredientId,
        quantityPerPerson: ri.quantityPerPerson,
        unit: this.normalizeUnit(ri.unit)
      }))
    };
    console.log(request);
    
    this.recipesService.createRecipe(request).subscribe({
      next: () => {
        setTimeout(() => {
          this.ui.stopLoading();
          this.ui.showSuccess("Recette créée avec succès !");
        }, 800);

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
        }, 1400);
      },
      error: (err) => {
        setTimeout(()=>{
          this.ui.stopLoading();
          this.ui.showError("Erreur lors de la création de la recette.");
          console.error(err);
        },800);
      }
    });
  }

  addIngredient(){
    const ingreduentId = this.form.value.ingredientSelect;
    const quantity = this.form.value.ingredientQuantityPerPerson;

    if(!ingreduentId) {
      this.ui.showError("Sélectionne un ingrédient.");
      return;
    }

    if(!quantity) {
      this.ui.showError("Sélectionne une quantité.");
      return;
    }

    const ingredient = this.ingredients().find(i => i.id === ingreduentId);
    if(!ingredient) return;

    if(this.recipeIngredients().some(ri => ri.ingredientId === ingredient.id)){
      this.ui.showError("Cet ingrédient est déjà ajouté.");
      return;
    }

    this.recipeIngredients.update(list => [
      ...list,
      {
        index: list.length,
        ingredientId: ingredient.id,
        name: ingredient.name,
        unit: this.normalizeUnit(ingredient.unit),
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

  onCreatedIngredient(newIngredient: IngredientResponse) {
    this.ingredients.update(list => [...list, newIngredient]);
    this.form.controls.ingredientSelect.setValue(newIngredient.id);
    this.selectedUnit.set(newIngredient.unit);
    this.form.controls.ingredientQuantityPerPerson.setValue(null);
  }

  // normalisation des units
  private normalizeUnit(u: string): string {
    const lower = u.toLowerCase();

    if (lower === 'piece') return 'pièce';
    if (lower === 'tranche') return 'tranche';

    return lower;
  }

}
