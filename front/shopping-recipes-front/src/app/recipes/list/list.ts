import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { RecipesServices } from '../services/recipes.service';
import { toSignal } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-list',
  standalone: true,
  imports: [],
  templateUrl: './list.html',
  styleUrl: './list.css',
})
export class List {

  private router = inject(Router);
  private recipesService = inject(RecipesServices);
  
  recipes = toSignal(this.recipesService.getAll(), {initialValue: []});
  
  goToRecipe(id: number) {
    this.router.navigate([`/app/recipes/${id}`])
  }
}
