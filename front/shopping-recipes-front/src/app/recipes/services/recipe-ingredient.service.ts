import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RecipeIngredient } from '../models/recipe-ingredient';

@Injectable({ providedIn: 'root' })
export class RecipeIgredientService {

  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/recipe-ingredients/recipe';

  getByRecipeId(recipeId: number): Observable<RecipeIngredient[]> {
    return this.http.get<RecipeIngredient[]>(`${this.apiUrl}/${recipeId}`);
  }
}
