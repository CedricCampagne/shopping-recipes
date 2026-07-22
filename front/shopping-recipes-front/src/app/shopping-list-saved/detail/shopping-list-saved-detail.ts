import { Component,  inject, computed, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { shoppingListService } from '../../shopping-list/services/shopping-list.service';
import { UIStore } from '../../shared/ui.store';

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
        setTimeout(()=>{
          this.ui.stopLoading();
          this.ui.showSuccess("Statut mis à jour !");
        },800);

        setTimeout(() => {
          this.shoppingListService.refreshSavedLists();
        }, 1400);
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
        setTimeout(()=>{
          this.ui.stopLoading();
          this.ui.showSuccess("Liste supprimée !");  
        },800);

        setTimeout(() => {
          this.router.navigate(['/app/shopping-list-saved']);
          this.shoppingListService.refreshSavedLists();
        }, 1400);
      },
      error: () => {
        setTimeout(()=>{
          this.ui.showError("Erreur lors de la suppression !");
          this.ui.stopLoading();
        },800)
      }
    });
  }

  exportPdf() {
    this.ui.startLoading();

    this.shoppingListService.exportPdf(this.id).subscribe({
      next: (pdfBlob: Blob) => {
        setTimeout(() =>{
          this.ui.stopLoading();
          this.ui.showSuccess("PDF exporté !");
        }, 800);

        setTimeout(()=>{
          const url = window.URL.createObjectURL(pdfBlob);
          const a = document.createElement('a');
          a.href = url;
          a.download = `shopping-list-${this.id}.pdf`;
          a.click();
        },1400);

      },
      error: () => {
        setTimeout(()=>{
          this.ui.showError("Erreur lors de l'export PDF !");
          this.ui.stopLoading();
        },800);
      }
    });
  }
}
