import { Routes } from '@angular/router';
import { Home } from './home/home';
import { Register } from './auth/register/register';
import { Login } from './auth/login/login';
import { AuthGuard } from './auth/guard/auth.guard';
import { animationFrameProvider } from 'rxjs/internal/scheduler/animationFrameProvider';

export const routes: Routes = [
    {
        path: '',
        component: Home,
        data: { animation: 'HomePage' }
    },
    {
        path:'register',
        component: Register,
        data: { animation: 'RegisterPage' }
    },
    { 
        path: 'login',
        component: Login,
        data: { animation: 'LoginPage' }
    },
    {
        path: 'app',
        loadComponent: () => import('./layout/main/main').then(m => m.Main),
        canActivate: [AuthGuard],
        data: { animation: 'MainPage' },
        children: [
            {
                path: 'recipes',
                runGuardsAndResolvers: 'always',
                data: { animation: 'RecipesPage' },
                loadComponent: () => import('./recipes/list/list').then(m => m.List)
            },
            {
                path: 'recipes/:id',
                data: { animation: 'RecipeDetailPage' },
                loadComponent: () => import('./recipes/detail/recipe-detail').then(m => m.RecipeDetail)
            },
            {
                path: 'shopping-list',
                data: { animation: 'ShoppinListsPage' },
                loadComponent: () => import ('./shopping-list/shopping-list').then(m => m.ShoppingList)
            },
            {
                path: 'shopping-list-saved',
                data: { animation: 'ShoppinListsSavedPage' },
                loadComponent: () => import ('./shopping-list-saved/list/shopping-list-saved-list').then(m =>m.ShoppingListSavedList)
            },
            {
                path: 'shopping-list-saved/:id',
                data: { animation: 'ShoppinListDetailPage' },
                loadComponent: () =>
                    import('./shopping-list-saved/detail/shopping-list-saved-detail')
                        .then(m => m.ShoppingListSavedDetail)
            },
            {
                path: 'ingredients',
                data: { animation: 'IngredientsPage' },
                loadComponent: () =>
                    import('./ingredient/ingredient-list/ingredient-list').then(m => m.IngredientList)
            },
            {
                path: 'ingredients/create',
                data: { animation: 'IngredientCreatePage'},
                loadComponent: () => import('./ingredient/ingredient-form/ingredient-form').then(m => m.IngredientForm)
            },
            // redirect auto pour pas avoir un /app vide
            { path: '', redirectTo: 'recipes', pathMatch: 'full' }
        ]
    }
];
