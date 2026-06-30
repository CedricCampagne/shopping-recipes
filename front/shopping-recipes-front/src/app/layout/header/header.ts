import { Component, signal, inject } from '@angular/core';
import { AuthStateService } from '../../auth/services/auth-state.service';
import { Router, RouterLink } from '@angular/router';
import { shoppingListService } from '../../shopping-list/services/shopping-list.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {

  private shoppingListService = inject(shoppingListService);

  shoppingList = this.shoppingListService.mergedItems;
  savedLists = this.shoppingListService.savedLists;

  isFetching = signal(false);

  constructor(
    private authState: AuthStateService,
    private router: Router 
  ){}

  logout() {
    this.isFetching.set(true);
    setTimeout(()=>{
      this.isFetching.set(false);
      this.authState.logout();
      this.router.navigate(['/login']);
    }, 2500)
  }
}
