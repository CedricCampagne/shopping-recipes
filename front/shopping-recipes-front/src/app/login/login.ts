import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  form = new FormGroup({
    username: new FormControl('', { validators: Validators.required }),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', { validators: Validators.required })
  });

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      console.log('form invalid');
      return;
    }

    console.log('form valid');
    console.log(this.form.value);
  }
}
