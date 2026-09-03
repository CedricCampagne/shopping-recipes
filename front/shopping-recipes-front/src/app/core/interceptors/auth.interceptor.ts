import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

import { AuthStateService } from '../../auth/services/auth-state.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const authState = inject(AuthStateService);
  
  // if (req.url.includes('/auth/login') || req.url.includes('auth/register')) {
  //   return next(req);
  // }

  req = req.clone({
    withCredentials: true
  });

  return next(req).pipe(
    catchError((err) => {
      const status = err.status ?? err.error?.status ?? err.error?.code ?? err.error?.error;

      if (status === 401) {
        console.error('401 : session expirée ou token invalide');
        authState.logout();
        router.navigate(['/login']);
        return throwError(() => err);
      }

      return throwError(() => err);
    }),
  );
};
