import { Component, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { RecipesServices } from '../services/recipes.service';
import { shoppingListService } from '../../shopping-list/services/shopping-list.service';
import { Recipe } from '../models/recipe';
import { UIStore } from '../../shared/ui.store';
import { normalizeText } from '../../shared/utils/string.utils';
@Component({
  selector: 'app-list',
  standalone: true,
  imports: [],
  templateUrl: './list.html',
  styleUrl: './list.css',
})
export class List {

  private router = inject(Router);
  private recipesService = inject(RecipesServices);
  private shoppingList = inject(shoppingListService);
  ui = inject(UIStore);
  
  recipes = signal<Recipe[]>([]);
  readonly search = signal('');
  readonly filteredRecipes = computed(() => {
  const searchValue = normalizeText(this.search().trim());

  return this.recipes().filter(recipe => {

      const matchName = normalizeText(recipe.name)
        .includes(searchValue);

      const matchDescription = normalizeText(recipe.description ?? '')
        .includes(searchValue);

      return matchName || matchDescription;
    });
  });

  ngOnInit(): void {
    this.recipesService.getAll().subscribe(res =>{
      this.recipes.set(res);
    });
  }

  goToRecipe(id: number) {
    this.router.navigate([`/app/recipes/${id}`])
  }

  // verifier si la recette est déjà dans le panier
  isAdded(recipeId: number) {
    return this.shoppingList.recipes().some(r => r.recipeId === recipeId);
  }

  // recuperer les servings de la derniere fois ou une redette est ajoutée
  addedServing(recipeId: number) {
    const found = this.shoppingList.recipes().filter(r => r.recipeId === recipeId);
    return found.length > 0 ? found[found.length - 1].servings : null;
  }

  goCreate(){
    this.router.navigateByUrl('/app/recipes/create');
  }

  delete(id: number){
    this.ui.clearMessage();
    this.ui.startLoading();
    console.log('DELETE', id)

    this.recipesService.deleteRecipe(id).subscribe({
      next: () => {
        setTimeout(()=>{
          this.ui.stopLoading();
          this.ui.showSuccess("Recette suprrimée avec succes !");
        },800);
        setTimeout(()=>{
          this.recipes.update(list => list.filter(r => r.id != id));
        },1400);
      },
      error : (err) => {
        
        const status =
        err.status ??
        err.error?.status ??
        err.error?.code ??
        err.error?.error;
        
        setTimeout(()=>{
          this.ui.stopLoading();
          if (status === 403) {
            this.ui.showError("Impossible de supprimer cette recette : elle est utilisée dans une liste sauvegardée.");
          }
          
          this.ui.showError("Erreur lors de la suppression.");
        },800);

        setTimeout(()=>{
          return;
        },1400);
      }
    });
  }

  update(id: number){
    console.log('UPDATE', id)
    this.router.navigate(['/app/recipes/update', id]);
  }

  resetFilter() {
    this.search.set('');
  }
}
