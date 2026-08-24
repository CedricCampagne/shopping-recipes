import { Component, input, output } from '@angular/core';
import { Recipe } from '../models/recipe';
import { UiButton } from '../../shared/ui/ui-button/ui-button';

@Component({
  selector: 'app-recipe-card',
  standalone: true,
  imports: [UiButton],
  templateUrl: './recipe-card.html',
  styleUrl: './recipe-card.css',
})
export class RecipeCard {
  recipe  = input.required<Recipe>();
  isAdded = input(false);
  addedServing = input<number | null>(null);

  select = output<number>();
  update = output<number>();
  delete = output<number>();
}
