import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAppUser, NewAppUser } from '../app-user.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAppUser for edit and NewAppUserFormGroupInput for create.
 */
type AppUserFormGroupInput = IAppUser | PartialWithRequiredKeyOf<NewAppUser>;

type AppUserFormDefaults = Pick<NewAppUser, 'id' | 'highContrastMode'>;

type AppUserFormGroupContent = {
  id: FormControl<IAppUser['id'] | NewAppUser['id']>;
  spotifyUserID: FormControl<IAppUser['spotifyUserID']>;
  name: FormControl<IAppUser['name']>;
  email: FormControl<IAppUser['email']>;
  userImageLarge: FormControl<IAppUser['userImageLarge']>;
  userImageMedium: FormControl<IAppUser['userImageMedium']>;
  userImageSmall: FormControl<IAppUser['userImageSmall']>;
  spotifyRefreshToken: FormControl<IAppUser['spotifyRefreshToken']>;
  spotifyAuthToken: FormControl<IAppUser['spotifyAuthToken']>;
  lastLoginDate: FormControl<IAppUser['lastLoginDate']>;
  discoverWeeklyBufferSettings: FormControl<IAppUser['discoverWeeklyBufferSettings']>;
  discoverWeeklyBufferPlaylistID: FormControl<IAppUser['discoverWeeklyBufferPlaylistID']>;
  highContrastMode: FormControl<IAppUser['highContrastMode']>;
  textSize: FormControl<IAppUser['textSize']>;
  user: FormControl<IAppUser['user']>;
};

export type AppUserFormGroup = FormGroup<AppUserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AppUserFormService {
  createAppUserFormGroup(appUser: AppUserFormGroupInput = { id: null }): AppUserFormGroup {
    const appUserRawValue = {
      ...this.getFormDefaults(),
      ...appUser,
    };
    return new FormGroup<AppUserFormGroupContent>({
      id: new FormControl(
        { value: appUserRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      spotifyUserID: new FormControl(appUserRawValue.spotifyUserID, {
        validators: [Validators.required],
      }),
      name: new FormControl(appUserRawValue.name, {
        validators: [Validators.required],
      }),
      email: new FormControl(appUserRawValue.email, {
        validators: [Validators.required],
      }),
      userImageLarge: new FormControl(appUserRawValue.userImageLarge),
      userImageMedium: new FormControl(appUserRawValue.userImageMedium),
      userImageSmall: new FormControl(appUserRawValue.userImageSmall),
      spotifyRefreshToken: new FormControl(appUserRawValue.spotifyRefreshToken, {
        validators: [Validators.maxLength(1000)],
      }),
      spotifyAuthToken: new FormControl(appUserRawValue.spotifyAuthToken, {
        validators: [Validators.maxLength(1000)],
      }),
      lastLoginDate: new FormControl(appUserRawValue.lastLoginDate, {
        validators: [Validators.required],
      }),
      discoverWeeklyBufferSettings: new FormControl(appUserRawValue.discoverWeeklyBufferSettings, {
        validators: [Validators.required],
      }),
      discoverWeeklyBufferPlaylistID: new FormControl(appUserRawValue.discoverWeeklyBufferPlaylistID),
      highContrastMode: new FormControl(appUserRawValue.highContrastMode, {
        validators: [Validators.required],
      }),
      textSize: new FormControl(appUserRawValue.textSize, {
        validators: [Validators.required],
      }),
      user: new FormControl(appUserRawValue.user),
    });
  }

  getAppUser(form: AppUserFormGroup): IAppUser | NewAppUser {
    return form.getRawValue() as IAppUser | NewAppUser;
  }

  resetForm(form: AppUserFormGroup, appUser: AppUserFormGroupInput): void {
    const appUserRawValue = { ...this.getFormDefaults(), ...appUser };
    form.reset(
      {
        ...appUserRawValue,
        id: { value: appUserRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AppUserFormDefaults {
    return {
      id: null,
      highContrastMode: false,
    };
  }
}
