import { Component, computed, inject, effect } from '@angular/core';
import { shoppingListService } from '../../shopping-list/services/shopping-list.service';
import { Router } from '@angular/router';
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

  lists = this.shoppingListService.savedLists;

  sortedListByDate = computed(() =>
    [...this.lists()].sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()),
  );

  // loadLists = effect(() => {
  //   this.shoppingListService.getAllSavedList().subscribe();
  // });

  openDetail(id: number) {
    this.router.navigate(['/app/shopping-list-saved', id]);
  }
}
