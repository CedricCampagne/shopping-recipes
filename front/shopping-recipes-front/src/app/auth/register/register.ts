import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { RegisterRequest } from '../models/register-request';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { UIStore } from '../../shared/ui.store';
import { UiMessages } from "../../shared/ui-messages/ui-messages";
import { trigger, transition, style, animate } from '@angular/animations';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule, UiMessages],
  templateUrl: './register.html',
  styleUrl: './register.css',
  animations: [
    trigger('pageTransition', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(50px)' }),
        animate('500ms ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
      ]),
      transition(':leave', [
        animate('500ms ease-in', style({ opacity: 0, transform: 'translateY(-10px)' }))
      ])
    ])
  ]
})
export class Register {
  
  authService = inject(AuthService);
  router = inject(Router);
  ui = inject(UIStore);

  form = new FormGroup({
    username: new FormControl('', { nonNullable: true, validators: Validators.required }),
    email: new FormControl('', {nonNullable: true, validators: [Validators.required, Validators.email]}),
    password: new FormControl('', {nonNullable: true, validators: [Validators.required, Validators.minLength(12), Validators.pattern(/[^A-Za-z0-9]/)]}),
    confirm: new FormControl('', {nonNullable: true, validators: [Validators.required]})
    },
    { validators: this.passwordsMatchValidator }
);

  onSubmit() {
    if (this.form.invalid) return;
    
    this.ui.startLoading();

    const raw = this.form.getRawValue();

    const data: RegisterRequest = {
      username: raw.username,
      email: raw.email,
      password: raw.password
    };

    this.authService.register(data).subscribe({
      next: res => {
        console.log('REGISTER OK', res)
        this.ui.showSuccess("Inscription réussie, redirection vers la connexion...")

        setTimeout(() => {
          this.ui.stopLoading();
          this.router.navigate(['/login']);
        }, 1500);
      },
      error: err => {
        console.log('LOGIN ERROR', err);

        setTimeout(()=>{
          this.ui.stopLoading();
        },1500);
        
        setTimeout(()=>{
          this.ui.showError("Erreur lors de l'inscription");
        },1500);
      }
    });

  }

  passwordsMatchValidator(control: AbstractControl): ValidationErrors | null {
    const form = control as FormGroup;

    const password = form.get('password')?.value;
    const confirm = form.get('confirm')?.value;

    return password === confirm ? null : { passwordMismatch: true };
  }

}
