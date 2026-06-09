import { CommonModule } from '@angular/common';
import { Component, } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  
  form = new FormGroup({
    username: new FormControl('', { validators: Validators.required }),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required, Validators.minLength(12), Validators.pattern(/[^A-Za-z0-9]/)]),
    confirm: new FormControl('', [Validators.required])
    },
    { validators: this.passwordsMatchValidator }
);

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      console.log('form invalid');
      return;
    }

    console.log('form valid');
    console.log(this.form.value);
  }

  passwordsMatchValidator(control: AbstractControl): ValidationErrors | null {
    const form = control as FormGroup;

    const password = form.get('password')?.value;
    const confirm = form.get('confirm')?.value;

    return password === confirm ? null : { passwordMismatch: true };
  }


}
