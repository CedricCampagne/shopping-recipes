import { Component, inject, signal, computed, viewChild } from '@angular/core';
import { RouterModule } from "@angular/router";
import { Header } from "../header/header";
import { shoppingListService } from '../../shopping-list/services/shopping-list.service';
import { trigger, transition, style, animate } from '@angular/animations';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-main',
  imports: [RouterModule, Header, RouterOutlet],
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
