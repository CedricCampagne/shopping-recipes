import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { RegisterRequest } from '../models/register-request';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  
  authService = inject(AuthService);
  router = inject(Router);

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
    
    const raw = this.form.getRawValue();

    const data: RegisterRequest = {
      username: raw.username,
      email: raw.email,
      password: raw.password
    };

    this.authService.register(data).subscribe({
      next: res => {
        console.log('REGISTER OK', res)
        this.router.navigate(['/login']);
      },
      error: err => console.log('REGISTER ERROR', err)
    });

  }

  passwordsMatchValidator(control: AbstractControl): ValidationErrors | null {
    const form = control as FormGroup;

    const password = form.get('password')?.value;
    const confirm = form.get('confirm')?.value;

    return password === confirm ? null : { passwordMismatch: true };
  }


}
