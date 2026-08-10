import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';

import { catchError, throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  
  if (req.url.includes('/auth/login') || req.url.includes('auth/register')) {
    return next(req);
  }

  const token = localStorage.getItem('token');

  if (token) {
    req = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` },
    });
  }

  return next(req).pipe(
    catchError((err) => {
      const status = err.status ?? err.error?.status ?? err.error?.code ?? err.error?.error;

      if (status === 401) {
        console.error('401 : session expirée ou token invalide');
        localStorage.removeItem('token');
        router.navigate(['/login']);
        return throwError(() => err);
      }

      return throwError(() => err);
    }),
  );
};
