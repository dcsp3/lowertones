import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMainArtist, NewMainArtist } from '../main-artist.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMainArtist for edit and NewMainArtistFormGroupInput for create.
 */
type MainArtistFormGroupInput = IMainArtist | PartialWithRequiredKeyOf<NewMainArtist>;

type MainArtistFormDefaults = Pick<NewMainArtist, 'id'>;

type MainArtistFormGroupContent = {
  id: FormControl<IMainArtist['id'] | NewMainArtist['id']>;
  artistSpotifyID: FormControl<IMainArtist['artistSpotifyID']>;
  artistName: FormControl<IMainArtist['artistName']>;
  artistPopularity: FormControl<IMainArtist['artistPopularity']>;
  artistImageSmall: FormControl<IMainArtist['artistImageSmall']>;
  artistImageMedium: FormControl<IMainArtist['artistImageMedium']>;
  artistImageLarge: FormControl<IMainArtist['artistImageLarge']>;
  artistFollowers: FormControl<IMainArtist['artistFollowers']>;
  dateAddedToDB: FormControl<IMainArtist['dateAddedToDB']>;
  dateLastModified: FormControl<IMainArtist['dateLastModified']>;
  relatedArtists: FormControl<IMainArtist['relatedArtists']>;
};

export type MainArtistFormGroup = FormGroup<MainArtistFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MainArtistFormService {
  createMainArtistFormGroup(mainArtist: MainArtistFormGroupInput = { id: null }): MainArtistFormGroup {
    const mainArtistRawValue = {
      ...this.getFormDefaults(),
      ...mainArtist,
    };
    return new FormGroup<MainArtistFormGroupContent>({
      id: new FormControl(
        { value: mainArtistRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      artistSpotifyID: new FormControl(mainArtistRawValue.artistSpotifyID, {
        validators: [Validators.required],
      }),
      artistName: new FormControl(mainArtistRawValue.artistName, {
        validators: [Validators.required],
      }),
      artistPopularity: new FormControl(mainArtistRawValue.artistPopularity, {
        validators: [Validators.required],
      }),
      artistImageSmall: new FormControl(mainArtistRawValue.artistImageSmall, {
        validators: [Validators.required],
      }),
      artistImageMedium: new FormControl(mainArtistRawValue.artistImageMedium, {
        validators: [Validators.required],
      }),
      artistImageLarge: new FormControl(mainArtistRawValue.artistImageLarge, {
        validators: [Validators.required],
      }),
      artistFollowers: new FormControl(mainArtistRawValue.artistFollowers),
      dateAddedToDB: new FormControl(mainArtistRawValue.dateAddedToDB),
      dateLastModified: new FormControl(mainArtistRawValue.dateLastModified),
      relatedArtists: new FormControl(mainArtistRawValue.relatedArtists),
    });
  }

  getMainArtist(form: MainArtistFormGroup): IMainArtist | NewMainArtist {
    return form.getRawValue() as IMainArtist | NewMainArtist;
  }

  resetForm(form: MainArtistFormGroup, mainArtist: MainArtistFormGroupInput): void {
    const mainArtistRawValue = { ...this.getFormDefaults(), ...mainArtist };
    form.reset(
      {
        ...mainArtistRawValue,
        id: { value: mainArtistRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MainArtistFormDefaults {
    return {
      id: null,
    };
  }
}
