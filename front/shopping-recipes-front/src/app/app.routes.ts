import { Routes } from '@angular/router';
import { Home } from './home/home';
import { Register } from './auth/register/register';
import { Login } from './auth/login/login';
import { AuthGuard } from './auth/guard/auth.guard';

export const routes: Routes = [
    {
        path: '',
        component: Home
    },
    {
        path:'register',
        component: Register
    },
    { 
        path: 'login',
        component: Login
    },
    {
        path: 'app',
        loadComponent: () => import('./layout/main/main').then(m => m.Main),
        canActivate: [AuthGuard],
        children: [
            {
                path: 'recipes',
                loadComponent: () => import('./recipes/list/list').then(m => m.List)
            },
            {
                path: 'recipes/:id',
                loadComponent: () => import('./recipes/detail/detail').then(m => m.Detail)
            },
            // redirect auto pour pas avoir un /app vide
            { path: '', redirectTo: 'recipes', pathMatch: 'full' }
        ]
    }
];
