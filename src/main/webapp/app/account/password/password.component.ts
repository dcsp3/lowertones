import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Observable } from 'rxjs';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { PasswordService } from './password.service';
import { PreferencesService } from '../preferences/preferences.service';

@Component({
  selector: 'jhi-password',
  templateUrl: './password.component.html',
  styleUrls: ['./password.component.scss'],
})
export class PasswordComponent implements OnInit {
  doNotMatch = false;
  error = false;
  success = false;
  highContrastMode = false;
  account$?: Observable<Account | null>;

  passwordForm = new FormGroup({
    currentPassword: new FormControl('', { nonNullable: true, validators: Validators.required }),
    newPassword: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(4), Validators.maxLength(50)],
    }),
    confirmPassword: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(4), Validators.maxLength(50)],
    }),
  });

  constructor(
    private passwordService: PasswordService,
    private accountService: AccountService,
    private preferencesService: PreferencesService
  ) {}

  ngOnInit(): void {
    this.account$ = this.accountService.identity();
    this.checkHighContrast();
  }

  public checkHighContrast(): void {
    this.preferencesService.getHighContrast().subscribe(
      highContrast => {
        this.highContrastMode = highContrast;
        console.log('High contrast: ' + highContrast);
      },
      error => {
        console.error('Error retrieving high contrast mode:', error);
      }
    );
  }

  changePassword(): void {
    this.error = false;
    this.success = false;
    this.doNotMatch = false;

    const { newPassword, confirmPassword, currentPassword } = this.passwordForm.getRawValue();
    if (newPassword !== confirmPassword) {
      this.doNotMatch = true;
    } else {
      this.passwordService.save(newPassword, currentPassword).subscribe({
        next: () => (this.success = true),
        error: () => (this.error = true),
      });
    }
  }
}
