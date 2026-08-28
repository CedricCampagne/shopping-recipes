import { Component, input, output } from '@angular/core';
import { DatePipe } from '@angular/common';
import { UiButton } from '../../shared/ui/ui-button/ui-button';
import { ShoppingListResponse } from '../../shopping-list/models/shoppin-list-response';

@Component({
  selector: 'app-shopping-list-card',
  imports: [DatePipe, UiButton],
  templateUrl: './shopping-list-saved-card.html',
  styleUrl: './shopping-list-saved-card.css',
})
export class ShoppingListSavedCard {
  list = input.required<ShoppingListResponse>();

  openDetail = output<number>();
}