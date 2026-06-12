import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { LoginRequest } from '../auth/models/loginRequest';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  authService = inject(AuthService);
  router = inject(Router);

  isFetching = signal(false);

  form = new FormGroup({
    email: new FormControl('', {nonNullable: true, validators:[Validators.required, Validators.email]}),
    password: new FormControl('', {nonNullable: true, validators:[Validators.required, Validators.minLength(12), Validators.pattern(/[^A-Za-z0-9]/)]}),
  });

  onSubmit() {
    if (this.form.invalid) return;
    
    this.isFetching.set(true);

    const raw = this.form.getRawValue();
    
        const data: LoginRequest = {
          email: raw.email,
          password: raw.password
        };
    
        this.authService.login(data).subscribe({
          next: res => {
            console.log('LOGIN OK', res)
            setTimeout(() => {
              this.isFetching.set(false);
              this.router.navigate(['/']);
            }, 2000);
          },
          error: err => console.log('LOGIN ERROR', err)
        });
   
  }
}
