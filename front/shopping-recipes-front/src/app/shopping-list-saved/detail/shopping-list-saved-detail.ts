import { Component,  inject, computed, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { shoppingListService } from '../../shopping-list/services/shopping-list.service';
import { UIStore } from '../../shared/ui.store';
import { UiMessages } from "../../shared/ui-messages/ui-messages";

@Component({
  selector: 'app-shopping-list-saved-detail',
  standalone: true,
  imports: [UiMessages],
  templateUrl: './shopping-list-saved-detail.html',
  styleUrl: './shopping-list-saved-detail.css',
})
export class ShoppingListSavedDetail {
  private shoppingListService = inject(shoppingListService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  ui = inject(UIStore);

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
    this.ui.startLoading();
    
    this.shoppingListService.updateStatus(this.id, newStatus).subscribe({
      next: () => {
        this.ui.showSuccess("Statut mis à jour !");
        
        this.shoppingListService.refreshSavedLists();

        setTimeout(() => {
          this.ui.stopLoading();
        }, 1200);

      },
      error: () => {
        this.ui.showError("Erreur lors de la mise à jour du Statut !");
        setTimeout(() => {
          this.ui.stopLoading();
        }, 1500);
      }
    });
  }

  deleteList() {
    this.ui.startLoading();

    this.shoppingListService.deleteList(this.id).subscribe({
      next : () => {
        this.ui.showSuccess("Liste supprimée !");
        setTimeout(() => {
          this.router.navigate(['/app/shopping-list-saved']);
          this.shoppingListService.refreshSavedLists();
          this.ui.stopLoading();
        }, 1200);
      },
      error: () => {
        this.ui.showError("Erreur lors de la suppression !");
        this.ui.stopLoading();
      }
    });
  }
}
