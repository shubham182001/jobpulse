import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { ToastrService } from 'ngx-toastr';
import { BehaviorSubject } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './auth.html',
  styleUrl: './auth.css',
})
export class Auth {

  isLoginMode = true;
  loginEmail = '';
  loginPassword = '';
  firstName = '';
  lastName = '';
  email = '';
  password = '';
  phoneNumber = '';
  skills = '';
  preferredRoles = '';
  preferredLocations = '';
  minSalary: number | null = null;
  remoteOk = true;
  experienceYears: number | null = null;
  education = '';

  loading$ = new BehaviorSubject<boolean>(false);

  constructor(
    private auth: AuthService,
    private toastr: ToastrService,
    private cdr: ChangeDetectorRef,
    private router: Router
  ) { }

  toggleMode() {
    this.isLoginMode = !this.isLoginMode;
  }

  register() {
    this.loading$.next(true);

    const payload = {
      firstName: this.firstName,
      lastName: this.lastName,
      email: this.email,
      password: this.password,
      phoneNumber: this.phoneNumber,
      skills: this.skills ? this.skills.split(',').map(s => s.trim()).filter(s => s) : [],
      preferredRoles: this.preferredRoles ? this.preferredRoles.split(',').map(s => s.trim()).filter(s => s) : [],
      preferredLocations: this.preferredLocations ? this.preferredLocations.split(',').map(s => s.trim()).filter(s => s) : [],
      minSalary: this.minSalary,
      remoteOk: this.remoteOk,
      experienceYears: this.experienceYears,
      education: this.education
    };

    this.auth.register(payload).subscribe({
      next: () => {
        this.loading$.next(false);
        this.cdr.detectChanges();
        this.toastr.success('Registration successful!');
        this.isLoginMode = true;
      },
      error: (err) => {
        this.loading$.next(false);
        this.cdr.detectChanges();
        this.toastr.error(err.error?.error || 'Registration failed');
      }
    });
  }

  login() {
    this.loading$.next(true);

    this.auth.login(this.loginEmail, this.loginPassword).subscribe({
      next: (res) => {
        this.loading$.next(false);
        this.cdr.detectChanges();
        this.auth.saveUser(res);
        this.toastr.success('Login successful!');
        setTimeout(() => {
          this.router.navigate(['/dashboard']);
        }, 800);
      },
      error: (err) => {
        this.loading$.next(false);
        this.cdr.detectChanges();
        this.toastr.error(err.error?.error || 'Invalid credentials');
      }
    });
  }
}