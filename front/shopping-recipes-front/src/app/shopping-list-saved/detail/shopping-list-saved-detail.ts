import { Component,  inject, computed } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
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

}
