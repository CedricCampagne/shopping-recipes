import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { RecipeIngredient } from "../models/recipe-ingredient";

@Injectable({ providedIn: 'root' })
export class RecipeIgredientService {

    constructor(private http: HttpClient) {}

    private apiUrl = "http://localhost:8080/recipe-ingredients/recipe";

    getByRecipeId(recipeId: number): Observable<RecipeIngredient[]> {

        const token = localStorage.getItem('token');
        const headers = new HttpHeaders({
            Authorization: `Bearer ${token}`
        });

        console.log('api url', `${this.apiUrl}${recipeId}`)
        return this.http.get<RecipeIngredient[]>(
            `${this.apiUrl}/${recipeId}`,
            { headers }
        );
    }
}