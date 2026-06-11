import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { RegisterRequest } from "./models/registerRequest";
import { LoginRequest } from "./models/loginRequest";





@Injectable({providedIn:'root'})
export class AuthService {
    private apiUrl = 'http://localhost:8080/auth';

    constructor(private http: HttpClient){}

    register(data:RegisterRequest) {
        console.log('register data',data);
        return this.http.post(`${this.apiUrl}/register`, data)
    }

    login(data: LoginRequest) {
        return this.http.post(`${this.apiUrl}/login`, data);
    }
}