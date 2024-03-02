import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISpotifyExchangeCode, NewSpotifyExchangeCode } from '../spotify-exchange-code.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISpotifyExchangeCode for edit and NewSpotifyExchangeCodeFormGroupInput for create.
 */
type SpotifyExchangeCodeFormGroupInput = ISpotifyExchangeCode | PartialWithRequiredKeyOf<NewSpotifyExchangeCode>;

type SpotifyExchangeCodeFormDefaults = Pick<NewSpotifyExchangeCode, 'id'>;

type SpotifyExchangeCodeFormGroupContent = {
  id: FormControl<ISpotifyExchangeCode['id'] | NewSpotifyExchangeCode['id']>;
};

export type SpotifyExchangeCodeFormGroup = FormGroup<SpotifyExchangeCodeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SpotifyExchangeCodeFormService {
  createSpotifyExchangeCodeFormGroup(spotifyExchangeCode: SpotifyExchangeCodeFormGroupInput = { id: null }): SpotifyExchangeCodeFormGroup {
    const spotifyExchangeCodeRawValue = {
      ...this.getFormDefaults(),
      ...spotifyExchangeCode,
    };
    return new FormGroup<SpotifyExchangeCodeFormGroupContent>({
      id: new FormControl(
        { value: spotifyExchangeCodeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
    });
  }

  getSpotifyExchangeCode(form: SpotifyExchangeCodeFormGroup): ISpotifyExchangeCode | NewSpotifyExchangeCode {
    if (form.controls.id.disabled) {
      // form.value returns id with null value for FormGroup with only one FormControl
      return { id: null };
    }
    return form.getRawValue() as ISpotifyExchangeCode | NewSpotifyExchangeCode;
  }

  resetForm(form: SpotifyExchangeCodeFormGroup, spotifyExchangeCode: SpotifyExchangeCodeFormGroupInput): void {
    const spotifyExchangeCodeRawValue = { ...this.getFormDefaults(), ...spotifyExchangeCode };
    form.reset(
      {
        ...spotifyExchangeCodeRawValue,
        id: { value: spotifyExchangeCodeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SpotifyExchangeCodeFormDefaults {
    return {
      id: null,
    };
  }
}
