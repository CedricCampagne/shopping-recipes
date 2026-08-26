import { Component, inject, input, output, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UiButton } from '../../shared/ui/ui-button/ui-button';
import { IngredientResponse } from '../models/ingredient-response';
import { IngredientsService } from '../services/ingredients.service';
import { UIStore } from '../../shared/ui.store';

@Component({
  selector: 'app-ingredient-card',
  imports: [ReactiveFormsModule, UiButton],
  templateUrl: './ingredient-card.html',
  styleUrl: './ingredient-card.css',
})
export class IngredientCard {
  private ingredientService = inject(IngredientsService);
  ui = inject(UIStore);

  ingredient = input.required<IngredientResponse>();
  units = input.required<string[]>();

  isEditing = signal(false);

  delete = output<number>();
  updated = output<void>();

  editForm = new FormGroup({
    name: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
    unit: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
  });

  startEdit() {
    this.editForm.patchValue({
      name: this.ingredient().name,
      unit: this.ingredient().unit,
    });

    this.isEditing.set(true);
  }

  cancelEdit() {
    this.isEditing.set(false);
    this.editForm.reset();
  }

  saveEdit() {
  if (this.editForm.invalid) return;

  const request = this.editForm.getRawValue();

  this.ui.clearMessage();
  this.ui.startLoading();

  this.ingredientService.updateIngredient(this.ingredient().id, request).subscribe({
    next: () => {
      setTimeout(() => {
        this.ui.stopLoading();
        this.ui.showSuccess('Ingrédient mis à jour !');
      }, 800);

      setTimeout(() => {
        this.isEditing.set(false);
        this.editForm.reset();
        this.updated.emit();
      }, 1400);
    },

    error: (err) => {
      setTimeout(() => {
        this.ui.stopLoading();
        this.ui.showError('Erreur lors de la mise à jour.');
        console.error(err);
      }, 800);

      setTimeout(() => {
        this.isEditing.set(false);
      }, 1400);
    },
  });
}
}