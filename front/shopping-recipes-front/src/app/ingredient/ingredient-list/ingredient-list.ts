import { Component, computed, inject, signal } from '@angular/core';
import { IngredientsService } from '../services/ingredients.service';
import { IngredientResponse } from '../models/ingredient-response';
import { Router } from '@angular/router';
import { UIStore } from '../../shared/ui.store';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UpdateIngredientRequest } from '../models/update-ingredient.request';
import { UiMessages } from "../../shared/ui-messages/ui-messages";

@Component({
  selector: 'app-ingredient-list',
  standalone: true,
  imports: [ReactiveFormsModule, UiMessages],
  templateUrl: './ingredient-list.html',
  styleUrl: './ingredient-list.css',
})
export class IngredientList {

  private ingredientService = inject(IngredientsService);
  private router = inject(Router);
  ui = inject(UIStore);

  ingredients = signal<IngredientResponse[]>([]);
  units = signal<string[]>([]);
  editingId = signal<number | null>(null);

  // Formulaire edition inline
  editForm = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.minLength(3)]),
    unit: new FormControl('', [Validators.required]),
  });

  sortedIngredients = computed(()=>
    [...this.ingredients()].sort((a, b)=>
      a.name.localeCompare(b.name)
    )
  );

  constructor(){
    this.loadIngredients();
  }

  ngOnInit() {
    this.ingredientService.getUnits().subscribe(units =>{
      this.units.set(units);
    });
  }

  loadIngredients() {
    this.ingredientService.getAllIngredients().subscribe(res => {
      this.ingredients.set(res);
    });
  }

  goCreate() {
    this.router.navigateByUrl('/app/ingredients/create');
  }

  goUpdate(id: number) {
    const ingredient = this.ingredients().find(i => i.id === id);
    if(!ingredient) return;

    this.editingId.set(id);
    this.editForm.setValue({
      name: ingredient.name,
      unit: ingredient.unit
    });

  }
  
  cancelEdit(){
    this.editingId.set(null);
  }

  saveEdit(id:number){
    if(this.editForm.invalid){
      this.editForm.markAllAsTouched();
      return;
    }

    const request: UpdateIngredientRequest = {
      name: this.editForm.controls.name.value!,
      unit: this.editForm.controls.unit.value!
    };

    this.ui.startLoading();

    this.ingredientService.updateIngredient(id, request).subscribe({
      next: () => {
        setTimeout(() => {
          this.ui.stopLoading();
          this.ui.showSuccess("Ingrédient mis à jour !");

          setTimeout(() => {
            this.editingId.set(null);
            this.loadIngredients();
          }, 1200);
        }, 800);
      },
      error: (err) => {
        this.ui.stopLoading();
        this.ui.showError("Erreur lorrs de la mise à jour.");
        console.error(err);
      }
    });
  }

  delete(id: number){
    console.log("DELETE", id)
    this.ingredientService.deleteIngredient(id).subscribe({
      next: () => {
        this.loadIngredients();
      },
      error: (err) => {
        console.error("Erreur lors de la suppression", err);
      }
    });
  }
}
