import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { LoginRequest } from '../models/login-request';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { UIStore } from '../../shared/ui.store';

@Component({
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  authService = inject(AuthService);
  router = inject(Router);
  ui = inject(UIStore);

  // isFetching = signal(false);
  // loginErr = signal(false);

  form = new FormGroup({
    email: new FormControl('', {nonNullable: true, validators:[Validators.required, Validators.email]}),
    password: new FormControl('', {nonNullable: true, validators:[Validators.required, Validators.minLength(12), Validators.pattern(/[^A-Za-z0-9]/)]}),
  });

  onSubmit() {
    if (this.form.invalid) return;
    
    //this.isFetching.set(true);
    this.ui.startLoading();
    
    const raw = this.form.getRawValue();
    
        const data: LoginRequest = {
          email: raw.email,
          password: raw.password
        };
    
        this.authService.login(data).subscribe({
          next: res => {
            console.log('LOGIN OK', res);
            localStorage.setItem('token', res.token);
            setTimeout(() => {
              this.router.navigate(['/app']);
            }, 2000);
          },
          error: err => {
            console.log('LOGIN ERROR', err);
            this.ui.showError("Email ou mot de passe incorrect");
            setTimeout(()=>{
              this.ui.stopLoading();
            }, 2000);
          }
        });
  }
}
