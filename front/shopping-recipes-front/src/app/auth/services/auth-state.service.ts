import { Injectable, signal } from '@angular/core';
import { UserResponse } from '../models/user-response';

@Injectable({ providedIn: 'root' })
export class AuthStateService {
  isLoggedIn(): boolean {
    return !!this.currentUser();
  }

  logout(): void {
    this.currentUser.set(null);
  }

  currentUser = signal<UserResponse | null>(null)
}