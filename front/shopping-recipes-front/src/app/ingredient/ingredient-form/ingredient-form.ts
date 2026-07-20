import { Component, inject, signal} from '@angular/core';
import { IngredientsService } from '../services/ingredients.service';
import { Router } from '@angular/router';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CreateIngredientRequest } from '../models/create-ingredient-request';
import { UIStore } from '../../shared/ui.store';
import { UiMessages } from "../../shared/ui-messages/ui-messages";

@Component({
  selector: 'app-ingredient-form',
  standalone: true,
  imports: [ReactiveFormsModule, UiMessages],
  templateUrl: './ingredient-form.html',
  styleUrl: './ingredient-form.css',
})
export class IngredientForm {

  private ingredientService = inject(IngredientsService);
  private router = inject(Router);
  private ui = inject(UIStore);

  units = signal<string[]>([]);

  form = new FormGroup({
    name: new FormControl('', [Validators.required,Validators.minLength(3)]),
    unit: new FormControl('', [Validators.required])
  });

  ngOnInit() {
    this.ingredientService.getUnits().subscribe(units =>{
      this.units.set(units);
    });
  }

  onSubmit() {
    if(this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const request: CreateIngredientRequest = {
      name: this.form.controls.name.value!,
      unit: this.normalizeUnit(this.form.controls.unit.value!)
    }

    
    this.ingredientService.createIngredient(request).subscribe({
      next: () => {
        this.ui.showSuccess("Ingrédient créé avec succès !");
        setTimeout(()=>{
          this.router.navigateByUrl('/app/ingredients');
        },1200)
      },
      error: (err) => {
        if (err.status === 409) {
          this.ui.showError("Ce nom d'ingrédient existe déjà.");
          return;
        }
        this.ui.showError("Erreur lors de la création de l'ingrédient.");
        console.error("Erreur lors de la création de l'ingredient", err);
      }
    });
  }

  private normalizeUnit(u: string): string {
    const lower = u.toLowerCase();

    if (lower === 'piece') return 'pièce';
    if (lower === 'tranche') return 'tranche';

    return lower;
  }

}