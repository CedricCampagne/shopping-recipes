import { Component, computed, inject, signal } from '@angular/core';
import { IngredientsService } from '../services/ingredients.service';
import { IngredientResponse } from '../models/ingredient-response';
import { Router } from '@angular/router';
import { UIStore } from '../../shared/ui.store';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UpdateIngredientRequest } from '../models/update-ingredient.request';
import { normalizeText } from '../../shared/utils/string.utils';
import { toSignal } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-ingredient-list',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './ingredient-list.html',
  styleUrl: './ingredient-list.css',
})
export class IngredientList {
  private ingredientService = inject(IngredientsService);
  private router = inject(Router);
  ui = inject(UIStore);

  ingredients = signal<IngredientResponse[]>([]);
  readonly units = toSignal(this.ingredientService.getUnits(), { initialValue: [] });

  editingId = signal<number | null>(null);

  readonly search = signal('');
  readonly selectedUnit = signal('');
  readonly filteredIngredients = computed(() => {
    const searchValue = normalizeText(this.search().trim());
    const unitValue = this.selectedUnit();
    return this.sortedIngredients().filter((ingredient) => {
      const matchName = normalizeText(ingredient.name).includes(searchValue);

      const matchUnit = unitValue === '' || ingredient.unit === unitValue;

      return matchName && matchUnit;
    });
  });

  resetFilter() {
    this.search.set('');
    this.selectedUnit.set('');
  }

  // Formulaire edition inline
  editForm = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.minLength(3)]),
    unit: new FormControl('', [Validators.required]),
  });

  sortedIngredients = computed(() =>
    [...this.ingredients()].sort((a, b) => a.name.localeCompare(b.name)),
  );

  constructor() {
    this.loadIngredients();
  }

  // ngOnInit() {
  //   this.ingredientService.getUnits().subscribe(units =>{
  //     this.units.set(units);
  //   });
  // }

  loadIngredients() {
    this.ingredientService.getAllIngredients().subscribe((res) => {
      this.ingredients.set(res);
    });
  }

  goCreate() {
    this.router.navigateByUrl('/app/ingredients/create');
  }

  goUpdate(id: number) {
    const ingredient = this.ingredients().find((i) => i.id === id);
    if (!ingredient) return;

    this.editingId.set(id);
    this.editForm.setValue({
      name: ingredient.name,
      unit: ingredient.unit,
    });
  }

  cancelEdit() {
    this.editingId.set(null);
  }

  saveEdit(id: number) {
    if (this.editForm.invalid) {
      this.editForm.markAllAsTouched();
      return;
    }

    const request: UpdateIngredientRequest = {
      name: this.editForm.controls.name.value!,
      unit: this.editForm.controls.unit.value!,
    };

    this.ui.clearMessage();
    this.ui.startLoading();

    this.ingredientService.updateIngredient(id, request).subscribe({
      next: () => {
        setTimeout(() => {
          this.ui.stopLoading();
          this.ui.showSuccess('Ingrédient mis à jour !');
        }, 800);

        setTimeout(() => {
          this.editingId.set(null);
          this.loadIngredients();
        }, 1400);
      },
      error: (err) => {
        setTimeout(() => {
          this.ui.stopLoading();
          this.ui.showError('Erreur lors de la mise à jour.');
          console.error(err);
        }, 800);

        setTimeout(() => {
          this.editingId.set(null);
        }, 1400);
      },
    });
  }

  delete(id: number) {
    this.ui.clearMessage();
    this.ui.startLoading();
    console.log('DELETE', id);

    this.ingredientService.deleteIngredient(id).subscribe({
      next: () => {
        setTimeout(() => {
          this.ui.stopLoading();
          this.ui.showSuccess('Ingrédient supprimé avec succes');
        }, 800);

        setTimeout(() => {
          this.loadIngredients();
        }, 1400);
      },
      error: (err) => {
        console.error('Erreur lors de la suppression', err);
        setTimeout(() => {
          this.ui.stopLoading();
          this.ui.showError('Erreur lors de la suppression');
        }, 800);
      },
    });
  }
}
