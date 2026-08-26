import { Component, computed, inject, signal } from '@angular/core';
import { IngredientsService } from '../services/ingredients.service';
import { IngredientResponse } from '../models/ingredient-response';
import { Router } from '@angular/router';
import { UIStore } from '../../shared/ui.store';
import { normalizeText } from '../../shared/utils/string.utils';
import { toSignal } from '@angular/core/rxjs-interop';
import { UiButton } from '../../shared/ui/ui-button/ui-button';
import { IngredientCard } from '../ingredient-card/ingredient-card';

@Component({
  selector: 'app-ingredient-list',
  standalone: true,
  imports: [UiButton, IngredientCard],
  templateUrl: './ingredient-list.html',
  styleUrl: './ingredient-list.css',
})
export class IngredientList {
  private ingredientService = inject(IngredientsService);
  private router = inject(Router);
  ui = inject(UIStore);

  ingredients = signal<IngredientResponse[]>([]);

  readonly units = toSignal(this.ingredientService.getUnits(), {
    initialValue: [],
  });

  readonly search = signal('');
  readonly selectedUnit = signal('');

  readonly filteredIngredients = computed(() => {
    const searchValue = normalizeText(this.search().trim());
    const unitValue = this.selectedUnit();

    return this.sortedIngredients().filter((ingredient) => {
      const matchName = normalizeText(ingredient.name).includes(searchValue);
      const matchUnit =
        unitValue === '' || ingredient.unit === unitValue;

      return matchName && matchUnit;
    });
  });

  sortedIngredients = computed(() =>
    [...this.ingredients()].sort((a, b) =>
      a.name.localeCompare(b.name),
    ),
  );

  constructor() {
    this.loadIngredients();
  }

  resetFilter() {
    this.search.set('');
    this.selectedUnit.set('');
  }

  loadIngredients() {
    this.ingredientService.getAllIngredients().subscribe((res) => {
      this.ingredients.set(res);
    });
  }

  goCreate() {
    this.router.navigateByUrl('/app/ingredients/create');
  }

  delete(id: number) {
    this.ui.clearMessage();
    this.ui.startLoading();

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