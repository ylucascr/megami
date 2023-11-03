import { Component } from '@angular/core';
import { catchError, of, switchMap } from 'rxjs';
import { AuthService } from '../auth/auth.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-auth-form-page',
  templateUrl: './auth-form-page.component.html',
  styleUrls: ['./auth-form-page.component.scss']
})
export class AuthFormPageComponent {

  constructor(
    private router: Router,
    private authService: AuthService
  ) { }

  form: FormGroup = new FormGroup({
    username: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required, Validators.minLength(8)])
  });

  handleSubmit() {
    if (this.form.invalid) return;

    const { username, password } = this.form.value;

    this.authService.registerUser(username, password).pipe(
      catchError(err => of(null)),
      switchMap(res => this.authService.loginUser(username, password))
    ).subscribe(res => {
      if (res.status === 'success') {
        this.router.navigate(['/']);
      }
    });
  }
}
