import { Component, inject } from '@angular/core';
import { RouterModule } from "@angular/router";
import { Header } from "../header/header";
import { shoppingListService } from '../../shopping-list/services/shopping-list.service';

import { RouterOutlet } from '@angular/router';
import { UiMessages } from '../../shared/ui-messages/ui-messages';

@Component({
  selector: 'app-main',
  imports: [RouterModule, Header, RouterOutlet, UiMessages],
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
