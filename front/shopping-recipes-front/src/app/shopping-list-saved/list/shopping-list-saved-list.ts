import { Component, inject, signal } from '@angular/core';
import { shoppingListService } from '../../shopping-list/services/shopping-list.service';
import { Router } from '@angular/router';
import { ShoppingListResponse } from '../../shopping-list/models/shoppin-list-response';

@Component({
  selector: 'app-shopping-list-saved-list',
  standalone: true,
  imports: [],
  templateUrl: './shopping-list-saved-list.html',
  styleUrl: './shopping-list-saved-list.css',
})

export class ShoppingListSavedList {
  private shoppingListService = inject(shoppingListService);
  private router = inject(Router);

  lists =this.shoppingListService.savedLists;
  
  openDetail(id:number) {
    this.router.navigate(['/app/shopping-list-saved', id])
  }
}
