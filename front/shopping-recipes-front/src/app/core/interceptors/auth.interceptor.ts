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

        const status =
            err.status ??
            err.error?.status ??
            err.error?.code ??
            err.error?.error;

        //Laisser passer les erreurs métier
        if (status === 409 || status === 400 || status === 404) {
            return throwError(() => err);
        }

        // Auth seulement
        if(status === 401) {
            console.error('401 : session expirée ou token invalide');
            localStorage.removeItem('token');
            router.navigate(['/login']);
            return throwError(() => err); // surtout PAS EMPTY
        }

        // NE PAS rediriger sur 403
        // 403 = erreur métier, pas auth
        if (status === 403) {
            return throwError(() => err);
        }

        // autres erreurs passent
        return throwError(()=> err);
      })
    );
}