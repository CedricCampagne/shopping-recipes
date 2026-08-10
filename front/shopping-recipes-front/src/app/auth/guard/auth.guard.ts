import { Injectable } from '@angular/core';
import { AuthStateService } from '../services/auth-state.service';
import { Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class AuthGuard {
  constructor(
    private authState: AuthStateService,
    private router: Router,
  ) {}

  canActivate(): boolean {
    if (!this.authState.isLoggedIn()) {
      this.router.navigate(['/login']);
      return false;
    }
    return true;
  }
}
