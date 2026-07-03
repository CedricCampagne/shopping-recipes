import { Component,  inject, computed, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { shoppingListService } from '../../shopping-list/services/shopping-list.service';

@Component({
  selector: 'app-shopping-list-saved-detail',
  standalone: true,
  imports: [],
  templateUrl: './shopping-list-saved-detail.html',
  styleUrl: './shopping-list-saved-detail.css',
})
export class ShoppingListSavedDetail {
  private shoppingListService = inject(shoppingListService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  statusChange = signal(false);
  statusError = signal(false);

  id = Number(this.route.snapshot.paramMap.get("id"));
  savedLists = this.shoppingListService.savedLists;

  sortedItems = computed(() => {
    return [...this.list()!.items].sort((a, b) =>
      a.ingredientName.localeCompare(b.ingredientName)
    );
  });

  list = computed(() =>
    this.savedLists().find(l => l.id === this.id)
  );

  updateStatus(newStatus: string) {
    this.shoppingListService.updateStatus(this.id, newStatus).subscribe({
      next: () => {
        this.statusError.set(false);
        this.statusChange.set(true);

        setTimeout(() => {
          this.statusChange.set(false);
        }, 1000);

        this.shoppingListService.refreshSavedLists();
      },
      error: () => {
        this.statusChange.set(false);
        this.statusError.set(true);

        setTimeout(() => {
          this.statusError.set(false);
        }, 2000);
      }
    });
  }


  deleteList() {
    this.shoppingListService.deleteList(this.id).subscribe({
      next : () => {
        this.shoppingListService.refreshSavedLists();
        this.router.navigate(['/app/shopping-list-saved']);
      }
    });
  }
}
