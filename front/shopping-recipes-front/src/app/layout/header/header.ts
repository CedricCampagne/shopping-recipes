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
  private authState = inject(AuthStateService);
  private router = inject(Router);

  shoppingList = this.shoppingListService.mergedItems;
  savedLists = this.shoppingListService.savedLists;

  isFetching = signal(false);

  logout() {
    this.isFetching.set(true);
    setTimeout(() => {
      this.isFetching.set(false);
      this.authState.logout();
      this.router.navigate(['/login']);
    }, 2500);
  }

  goRecipes() {
    this.router.navigateByUrl('/app/recipes');
  }

  goIngredients() {
    this.router.navigateByUrl('/app/ingredients');
  }

  goSavedLists() {
    this.router.navigateByUrl('/app/shopping-list-saved');
  }
}
