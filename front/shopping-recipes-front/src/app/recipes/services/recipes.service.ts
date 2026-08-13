import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Recipe } from '../models/recipe';
import { CreateRecipeRequest } from '../models/create-recipe-request';
import { UpdateRecipeRequest } from '../models/update-recipe-request';

@Injectable({ providedIn: 'root' })
export class RecipesServices {
  private apiUrl = 'http://localhost:8080/recipes';
  private http = inject(HttpClient);

  getAll(): Observable<Recipe[]> {
    return this.http.get<Recipe[]>(this.apiUrl);
  }

  getById(id: number): Observable<Recipe> {
    return this.http.get<Recipe>(`${this.apiUrl}/${id}`);
  }

  createRecipe(payload: CreateRecipeRequest) {
    return this.http.post(`${this.apiUrl}`, payload);
  }

  deleteRecipe(id: number) {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  updateRecipe(id: number, request: UpdateRecipeRequest) {
    return this.http.put(`${this.apiUrl}/${id}`, request);
  }
}
