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

  lists = signal<ShoppingListResponse[]>([]);

  constructor() {
    this.shoppingListService.getAllSavedList().subscribe({
      next: (res) => this.lists.set(res),
      error: (err) => console.error("Erreur lors de récupération des listes", err)
    });
  }

  openDetail(id:number) {
    this.router.navigate(['/app/shopping-list-saved', id])
  }
}
