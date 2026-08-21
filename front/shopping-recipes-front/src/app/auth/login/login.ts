import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { LoginRequest } from '../models/login-request';
import { AuthService } from '../services/auth.service';
import { UIStore } from '../../shared/ui.store';
import { UiMessages } from '../../shared/ui/ui-messages/ui-messages';

@Component({
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule, RouterModule, UiMessages],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  private authService = inject(AuthService);
  private router = inject(Router);
  ui = inject(UIStore);

  form = new FormGroup({
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
  });

  onSubmit() {
    if (this.form.invalid) return;

    this.ui.clearMessage();
    this.ui.startLoading();

    const raw = this.form.getRawValue();

    const data: LoginRequest = {
      email: raw.email,
      password: raw.password,
    };

    this.authService.login(data).subscribe({
      next: (res) => {
        setTimeout(() => {
          this.ui.stopLoading();
          this.ui.showSuccess('Connexion autorisée!');
        }, 800);

        setTimeout(() => {
          localStorage.setItem('token', res.token);
          this.ui.clearMessage();
          this.router.navigate(['/app']);
        }, 1400);
      },
      error: (err) => {
        setTimeout(() => {
          this.ui.stopLoading();
        }, 800);

        setTimeout(() => {
          this.ui.showError('Email ou mot de passe incorrect');
        }, 1400);
      },
    });
  }
}