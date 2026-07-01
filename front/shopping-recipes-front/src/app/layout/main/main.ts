import { Component, inject, signal } from '@angular/core';
import { RouterModule } from "@angular/router";
import { Header } from "../header/header";
import { shoppingListService } from '../../shopping-list/services/shopping-list.service';
import { ShoppingListResponse } from '../../shopping-list/models/shoppin-list-response';

@Component({
  selector: 'app-main',
  imports: [RouterModule, Header],
  templateUrl: './main.html',
  styleUrl: './main.css',
})
export class Main {

  private shoppingListService = inject(shoppingListService);

  constructor() {
    this.shoppingListService.getAllSavedList().subscribe({
      next: (res) => this.shoppingListService.savedLists.set(res),
      error: (err) => console.error("Erreur lors de récupération des listes", err)
    });
  }
}
