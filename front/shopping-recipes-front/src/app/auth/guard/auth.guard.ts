import { Injectable, inject } from '@angular/core';
import { AuthStateService } from '../services/auth-state.service';
import { Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class AuthGuard {
  private authState = inject(AuthStateService);
  private router = inject(Router);

  canActivate(): boolean {
    if (!this.authState.isLoggedIn()) {
      this.router.navigate(['/login']);
      return false;
    }
    return true;
  }
}