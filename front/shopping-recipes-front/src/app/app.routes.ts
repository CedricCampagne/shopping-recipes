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
                loadComponent: () => import('./recipes/detail/recipe-detail').then(m => m.RecipeDetail)
            },
            {
                path: 'shopping-list',
                loadComponent: () => import ('./shopping-list/shopping-list').then(m => m.ShoppingList)
            },
            {
                path: 'shopping-list-saved',
                loadComponent: () => import ('./shopping-list-saved/list/shopping-list-saved-list').then(m =>m.ShoppingListSavedList)
            },
            {
                path: 'shopping-list-saved/:id',
                loadComponent: () =>
                    import('./shopping-list-saved/detail/shopping-list-saved-detail')
                        .then(m => m.ShoppingListSavedDetail)
            },
            // redirect auto pour pas avoir un /app vide
            { path: '', redirectTo: 'recipes', pathMatch: 'full' }
        ]
    }
];
