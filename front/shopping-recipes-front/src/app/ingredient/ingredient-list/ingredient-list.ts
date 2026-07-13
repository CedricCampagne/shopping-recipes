import { Component, computed, inject, signal } from '@angular/core';
import { IngredientsService } from '../services/ingredients.service';
import { IngredientResponse } from '../models/ingredient-response';
import { Router } from '@angular/router';

@Component({
  selector: 'app-ingredient-list',
  standalone: true,
  imports: [],
  templateUrl: './ingredient-list.html',
  styleUrl: './ingredient-list.css',
})
export class IngredientList {

  private ingredientService = inject(IngredientsService);
  private router = inject(Router);

  ingredients = signal<IngredientResponse[]>([]);

  sortedIngredients = computed(()=>
    [...this.ingredients()].sort((a, b)=>
      a.name.localeCompare(b.name)
    )
  );

  constructor(){
    this.loadIngredietns();
  }

  loadIngredietns() {
    this.ingredientService.getAllIngredients().subscribe(res => {
      this.ingredients.set(res);
    });
  }

  goCreate() {
    this.router.navigateByUrl('/app/ingredients/create');
  }

  goUpdate(id: number) {
    this.router.navigate(['/app/ingredients', id]);
  }

  delete(id: number){
    console.log("DELETE", id)
    this.ingredientService.deleteIngredient(id).subscribe({
      next: () => {
        this.loadIngredietns();
      },
      error: (err) => {
        console.error("Erreur lors de la suppression", err);
      }
    });
  }
}
