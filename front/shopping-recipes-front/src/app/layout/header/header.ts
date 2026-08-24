import { Component, signal, inject } from '@angular/core';
import { AuthStateService } from '../../auth/services/auth-state.service';
import { Router } from '@angular/router';
import { shoppingListService } from '../../shopping-list/services/shopping-list.service';
import { UiButton } from '../../shared/ui/ui-button/ui-button';
import { UiLink } from "../../shared/ui/ui-link/ui-link";

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [UiButton, UiLink],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {
  private shoppingListService = inject(shoppingListService);
  private authState = inject(AuthStateService);
  private router = inject(Router);

  shoppingList = this.shoppingListService.mergedItems;
  savedLists = this.shoppingListService.savedLists;

  isFetching = signal(false);

  isMenuOpen = signal(false);

  toggleMenu() {
    this.isMenuOpen.update(isMenuOpen => !isMenuOpen);
  }

  logout() {
    this.isFetching.set(true);
    setTimeout(() => {
      this.isMenuOpen.set(false);
      this.isFetching.set(false);
      this.authState.logout();
      this.router.navigate(['/login']);
    }, 2500);
  }

  goRecipes() {
     this.isMenuOpen.set(false);
    this.router.navigateByUrl('/app/recipes');
  }

  goIngredients() {
     this.isMenuOpen.set(false);
    this.router.navigateByUrl('/app/ingredients');
  }

  goShoppingList() {
     this.isMenuOpen.set(false);
    this.router.navigateByUrl('/app/shopping-list');
  }

  goSavedLists() {
     this.isMenuOpen.set(false);
    this.router.navigateByUrl('/app/shopping-list-saved');
  }
}
