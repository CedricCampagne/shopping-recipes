import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IngredientResponse } from '../models/ingredient-response';
import { CreateIngredientRequest } from '../models/create-ingredient-request';
import { UpdateIngredientRequest } from '../models/update-ingredient.request';

@Injectable({ providedIn: 'root' })
export class IngredientsService {
  constructor(private http: HttpClient) {}

  private apiUrl = 'http://localhost:8080/ingredients';

  getAllIngredients() {
    return this.http.get<IngredientResponse[]>(`${this.apiUrl}`);
  }

  getUnits() {
    return this.http.get<string[]>(`${this.apiUrl}/units`);
  }

  createIngredient(request: CreateIngredientRequest) {
    return this.http.post(`${this.apiUrl}`, request);
  }

  updateIngredient(id: number, request: UpdateIngredientRequest) {
    return this.http.put(`${this.apiUrl}/${id}`, request);
  }

  deleteIngredient(id: number) {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}
