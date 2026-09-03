import { inject } from "@angular/core";
import { AuthService } from "./services/auth.service";
import { AuthStateService } from "./services/auth-state.service";
import { catchError, of, tap, throwError } from "rxjs";

export function initializeAuth(){
    
    const authService = inject(AuthService);
    const authState = inject(AuthStateService);

    return authService.getCurrentUser().pipe(
        tap((user) => {
            authState.currentUser.set(user);
        }),
        catchError((err)=>{
            if(err.status === 401){
                return of(null)
            }

            return throwError(()=> err);
        })
    );

}