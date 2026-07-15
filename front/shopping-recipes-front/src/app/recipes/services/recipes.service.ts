import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Recipe } from "../models/recipe";
import { CreateRecipeRequest } from "../models/create-recipe-request";



@Injectable({providedIn: 'root'})
export class RecipesServices {
    private apiUrl = 'http://localhost:8080/recipes';

    constructor(private http: HttpClient) {}

    getAll(): Observable<Recipe[]> {
        return this.http.get<Recipe[]>(this.apiUrl);
    }

    getById(id: number): Observable<Recipe> {   
        return this.http.get<Recipe>(`${this.apiUrl}/${id}`);
    }

    createRecipe(payload: CreateRecipeRequest) {
        return this.http.post(`${this.apiUrl}`, payload);
    }
}