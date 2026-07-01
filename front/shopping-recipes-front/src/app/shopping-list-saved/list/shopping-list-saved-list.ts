import { Component, inject, Signal } from '@angular/core';
import { shoppingListService } from '../../shopping-list/services/shopping-list.service';
import { Router } from '@angular/router';
import { ShoppingListResponse } from '../../shopping-list/models/shoppin-list-response';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-shopping-list-saved-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './shopping-list-saved-list.html',
  styleUrl: './shopping-list-saved-list.css',
})

export class ShoppingListSavedList {
  private shoppingListService = inject(shoppingListService);
  private router = inject(Router);

  lists: Signal<ShoppingListResponse[]> =this.shoppingListService.savedLists;
  
  openDetail(id:number) {
    this.router.navigate(['/app/shopping-list-saved', id])
  }

}


