import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { LoginRequest } from '../models/login-request';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { UIStore } from '../../shared/ui.store';
import { UiMessages } from "../../shared/ui-messages/ui-messages";

@Component({
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule, RouterModule, UiMessages],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  authService = inject(AuthService);
  router = inject(Router);
  ui = inject(UIStore);

  form = new FormGroup({
    email: new FormControl('', {nonNullable: true, validators:[Validators.required, Validators.email]}),
    password: new FormControl('', {nonNullable: true, validators:[Validators.required, Validators.minLength(12), Validators.pattern(/[^A-Za-z0-9]/)]}),
  });

  onSubmit() {
    if (this.form.invalid) return;
    console.log("login click");

    this.ui.clearMessage();
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
              this.ui.stopLoading();
              this.router.navigate(['/app']);
            }, 1500);
          },
          error: err => {
            console.log('LOGIN ERROR', err);
            setTimeout(()=>{
              this.ui.stopLoading();
            },1500);
            
            setTimeout(()=>{
              this.ui.showError("Email ou mot de passe incorrect");
            },1500);
          }
        });
  }
}
