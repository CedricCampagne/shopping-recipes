import { Component, signal, inject } from '@angular/core';
import { AuthStateService } from '../../auth/services/auth-state.service';
import { Router, RouterLink } from '@angular/router';
import { shoppingListService } from '../../shopping-list/services/shopping-list.service';
import { RecipesServices } from '../../recipes/services/recipes.service';
import { IngredientsService } from '../../ingredient/services/ingredients.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {
  private shoppingListService = inject(shoppingListService);
  private recipesService = inject(RecipesServices);
  private ingredientsService = inject(IngredientsService);

  shoppingList = this.shoppingListService.mergedItems;
  savedLists = this.shoppingListService.savedLists;

  isFetching = signal(false);

  constructor(
    private authState: AuthStateService,
    private router: Router,
  ) {}

  logout() {
    this.isFetching.set(true);
    setTimeout(() => {
      this.isFetching.set(false);
      this.authState.logout();
      this.router.navigate(['/login']);
    }, 2500);
  }

  goRecipes() {
    this.recipesService.getAll().subscribe({
      next: () => {
        this.router.navigateByUrl('/app/recipes');
      },
      error: () => {
        // l’interceptor gère déjà le redirect
      },
    });
  }

  goIngredients() {
    this.ingredientsService.getAllIngredients().subscribe({
      next: () => {
        this.router.navigateByUrl('/app/ingredients');
      },
      error: () => {
        // l’interceptor gère déjà le redirect
      },
    });
  }

  goSavedLists() {
    this.shoppingListService.getAllSavedList().subscribe({
      next: () => this.router.navigate(['/app/shopping-list-saved']),
      error: () => this.router.navigate(['/login']),
    });
  }
}
