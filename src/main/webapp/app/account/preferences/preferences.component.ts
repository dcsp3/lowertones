import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { PreferencesService } from './preferences.service';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';
import { AppUserFormService, AppUserFormGroup } from 'app/entities/app-user/update/app-user-form.service';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { Router } from '@angular/router';
import { LoginService } from 'app/login/login.service';
import { MainComponent } from 'app/layouts/main/main.component';

const initialAccount: Account = {} as Account;

@Component({
  selector: 'jhi-preferences',
  templateUrl: './preferences.component.html',
  styleUrls: ['./preferences.component.scss'],
})
export class PreferencesComponent implements OnInit {
  success = false; // Represents successful user form submission
  login = ''; // This user's name (unique and shared between user/appuser)
  appUserForm: AppUserFormGroup | undefined;
  appUser: IAppUser | undefined;
  highContrastElements = ['.container', '.text-container', '.navbar', '.footer'];

  constructor(
    private accountService: AccountService,
    private appUserFormService: AppUserFormService,
    private appUserService: AppUserService,
    private preferencesService: PreferencesService,
    private router: Router,
    private loginService: LoginService,
    private mainComponent: MainComponent
  ) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      if (account) {
        this.login = account.login;
        this.userForm.get('email')?.setValue(account.email);
        this.userForm.patchValue(account); // Link this user form to this user
      }
    });

    this.preferencesService.getAppUser().subscribe(appUser => {
      this.appUser = appUser; // Load the entity for this app user
      this.appUserForm = this.appUserFormService.createAppUserFormGroup(this.appUser);
    });
  }

  userForm = new FormGroup({
    email: new FormControl(initialAccount.email, {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email],
    }),
  });

  saveUser(): void {
    // Update this JHipster user based on the user form input
    this.success = false;
    const email = this.userForm.get('email')?.value; // Extracting the email value
    if (email) {
      this.preferencesService.updateEmail(email).subscribe(
        () => {
          this.success = true;
        },
        error => {
          console.error('Error updating email:', error);
        }
      );
    }
  }

  toggleEmailUpdates(): void {
    if (this.appUserForm) {
      const currentValue = this.appUserForm.get('discoverWeeklyBufferSettings')?.value;
      const newValue = currentValue === 0 ? 1 : 0;
      this.appUserForm.get('discoverWeeklyBufferSettings')?.setValue(newValue);
      this.savePreferences(); // Call savePreferences to save the updated value
    }
  }

  savePreferences(): void {
    // Update this app user based on the app user form input
    const updatedAppUser = this.appUserForm?.getRawValue(); // Get the updated appUser form value
    if (updatedAppUser?.id && this.appUserForm) {
      this.appUserService.update(updatedAppUser as IAppUser).subscribe(
        () => {
          this.mainComponent.applyPreferences();
        },
        error => {
          console.error('Error saving preferences:', error);
        }
      );
    }
  }

  signOutAllDevices(): void {
    const confirmation = confirm('Are you sure you want to sign out of all devices? This will log you out immediately');
    if (confirmation) {
      this.preferencesService.signOutAllDevices(this.login).subscribe(
        () => {
          this.loginService.logout();
          this.router.navigateByUrl('/'); // Redirect to the home page
        },
        error => {
          console.error('Error signing out of all devices:', error);
        }
      );
    }
  }

  deleteCurrentUser(): void {
    // Delete this user and corresponding app user
    const confirmation = confirm('Are you sure you want to delete your account? This action cannot be undone.');
    if (confirmation && this.login != 'admin') {
      this.preferencesService.deleteCurrentUser(this.login).subscribe(
        () => {
          this.loginService.logout();
          this.router.navigateByUrl('/'); // Redirect to the home page
        },
        error => {
          console.error('Error deleting user:', error);
        }
      );
    }
  }
}
