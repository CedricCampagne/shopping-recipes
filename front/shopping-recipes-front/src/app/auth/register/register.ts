import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
  AbstractControl,
  ValidationErrors,
} from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { RegisterRequest } from '../models/register-request';
import { AuthService } from '../services/auth.service';
import { UIStore } from '../../shared/ui.store';
import { UiMessages } from '../../shared/ui/ui-messages/ui-messages';
import { UiLink } from '../../shared/ui/ui-link/ui-link';
import { UiButton } from '../../shared/ui/ui-button/ui-button';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule, UiMessages, UiLink, UiButton],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  private authService = inject(AuthService);
  private router = inject(Router);
  ui = inject(UIStore);

  form = new FormGroup(
    {
      username: new FormControl('', { nonNullable: true, validators: Validators.required }),
      email: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required, Validators.email],
      }),
      password: new FormControl('', {
        nonNullable: true,
        validators: [
          Validators.required,
          Validators.minLength(12),
          Validators.pattern(/[^A-Za-z0-9]/),
        ],
      }),
      confirm: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    },
    { validators: this.passwordsMatchValidator },
  );

  onSubmit() {
    if (this.form.invalid) return;

    this.ui.clearMessage();
    this.ui.startLoading();

    const raw = this.form.getRawValue();

    const data: RegisterRequest = {
      username: raw.username,
      email: raw.email,
      password: raw.password,
    };

    this.authService.register(data).subscribe({
      next: () => {
        setTimeout(() => {
          this.ui.stopLoading();
          this.ui.showSuccess('Inscription réussie, redirection vers la connexion...');
        }, 800);

        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 1400);
      },
      error: (err) => {
        setTimeout(() => {
          this.ui.stopLoading();
        }, 800);

        setTimeout(() => {
          this.ui.showError("Erreur lors de l'inscription");
        }, 1400);
      },
    });
  }

  passwordsMatchValidator(control: AbstractControl): ValidationErrors | null {
    const form = control as FormGroup;

    const password = form.get('password')?.value;
    const confirm = form.get('confirm')?.value;

    return password === confirm ? null : { passwordMismatch: true };
  }
}