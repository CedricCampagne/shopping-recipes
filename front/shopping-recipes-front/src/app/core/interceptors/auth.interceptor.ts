import { HttpInterceptorFn } from "@angular/common/http";
import { inject } from '@angular/core';
import { Router } from '@angular/router';

import { catchError, EMPTY, throwError } from "rxjs";

export const authInterceptor : HttpInterceptorFn = (req, next) => {
    console.log('authInterceptor → requête sortante:', req.url);

    if (req.url.includes('/auth/login') || req.url.includes('auth/register')) {
        return next(req);
    }

    const token = localStorage.getItem('token');

    if(token) {
        req = req.clone({
            setHeaders : { Authorization : `Bearer ${token}`}
        });
    }

    const router = inject(Router);

    return next(req).pipe(
      catchError(err => {
        if(err.status === 401 || err.status === 403) {
            console.error('401 : session expirée ou token invalide');
            localStorage.removeItem('token');
            router.navigate(['/login']);
            return EMPTY;
        }
        return throwError(()=> err);
      })
    );
}