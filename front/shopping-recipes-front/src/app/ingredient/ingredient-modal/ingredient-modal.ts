import { Component, EventEmitter, inject, Output, signal } from '@angular/core';
import { FormGroup, ReactiveFormsModule, FormControl } from '@angular/forms';
import { IngredientsService } from '../services/ingredients.service';
import { UIStore } from '../../shared/ui.store';
import { Validators } from '@angular/forms';
import { CreateIngredientRequest } from '../models/create-ingredient-request';

@Component({
  selector: 'app-ingredient-modal',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './ingredient-modal.html',
  styleUrl: './ingredient-modal.css',
})
export class IngredientModal {
  private ingredientService = inject(IngredientsService);
  private ui = inject(UIStore);

  units = signal<string[]>([]);

  ingredientForm = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.minLength(3),
      Validators.pattern(/^\S.*$/)
    ]),
    unit: new FormControl('', [Validators.required])
  });

  @Output() closeModal = new EventEmitter<void>();
  @Output() ingredientCreated = new EventEmitter();

  ngOnInit() {
    this.ingredientService.getUnits().subscribe(units =>{
      this.units.set(units);
    });
  }

  createIngredient(){
    if(this.ingredientForm.invalid) {
          this.ingredientForm.markAllAsTouched();
          return;
        }
    
        const request: CreateIngredientRequest = {
          name: this.ingredientForm.controls.name.value!,
          unit: this.ingredientForm.controls.unit.value!
        }
    
        this.ui.startLoading();

        this.ingredientService.createIngredient(request).subscribe({
          next: (newIngredient) => {
            setTimeout(()=>{
              this.ui.stopLoading();
              this.ui.showSuccess("Ingrédient créé avec succès !");              
            }, 800);

            setTimeout(()=> {
              this.ingredientCreated.emit(newIngredient);
              this.closeModal.emit();
            },1400);
          },
          error: (err) => {
            this.ui.stopLoading();
            if (err.status === 409) {
              this.ui.showError("Ce nom d'ingrédient existe déjà.");
              return;
            }
            this.ui.showError("Erreur lors de la création de l'ingrédient.");
            console.error("Erreur lors de la création de l'ingredient", err);
          }
        });
  }

  close(){
    this.closeModal.emit();
  }

}
