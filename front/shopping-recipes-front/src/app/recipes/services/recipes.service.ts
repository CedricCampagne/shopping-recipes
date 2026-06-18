import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Recipe } from "../models/recipe";



@Injectable({providedIn: 'root'})
export class RecipesServices {
    private apiUrl = 'http://localhost:8080/recipes';

    constructor(private http: HttpClient) {}

    getAll(): Observable<Recipe[]> {
        const token  = localStorage.getItem('token');
        const headers = new HttpHeaders({
            Authorization: `Bearer ${token}`
        });

        return this.http.get<Recipe[]>(this.apiUrl, { headers });
    }

    getById(id: number): Observable<Recipe> {
        const token  = localStorage.getItem('token');
        const headers = new HttpHeaders({
            Authorization: `Bearer ${token}`
        });

        return this.http.get<Recipe>(`${this.apiUrl}/${id}`, { headers });
  }
}