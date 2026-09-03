import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { RegisterRequest } from '../models/register-request';
import { LoginRequest } from '../models/login-request';
import { UserResponse } from '../models/user-response';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = 'http://localhost:8080/auth';
  private http = inject(HttpClient);

  register(data: RegisterRequest) {
    return this.http.post<void>(`${this.apiUrl}/register`, data);
  }

  login(data: LoginRequest) {
    return this.http.post<UserResponse>(`${this.apiUrl}/login`, data);
  }

  getCurrentUser(){
    return this.http.get<UserResponse>(`${this.apiUrl}/me`);
  }
}