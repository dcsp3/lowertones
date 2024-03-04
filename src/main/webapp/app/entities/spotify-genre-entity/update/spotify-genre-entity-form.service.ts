import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISpotifyGenreEntity, NewSpotifyGenreEntity } from '../spotify-genre-entity.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISpotifyGenreEntity for edit and NewSpotifyGenreEntityFormGroupInput for create.
 */
type SpotifyGenreEntityFormGroupInput = ISpotifyGenreEntity | PartialWithRequiredKeyOf<NewSpotifyGenreEntity>;

type SpotifyGenreEntityFormDefaults = Pick<NewSpotifyGenreEntity, 'id'>;

type SpotifyGenreEntityFormGroupContent = {
  id: FormControl<ISpotifyGenreEntity['id'] | NewSpotifyGenreEntity['id']>;
  spotifyGenre: FormControl<ISpotifyGenreEntity['spotifyGenre']>;
  song: FormControl<ISpotifyGenreEntity['song']>;
  album: FormControl<ISpotifyGenreEntity['album']>;
  mainArtist: FormControl<ISpotifyGenreEntity['mainArtist']>;
};

export type SpotifyGenreEntityFormGroup = FormGroup<SpotifyGenreEntityFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SpotifyGenreEntityFormService {
  createSpotifyGenreEntityFormGroup(spotifyGenreEntity: SpotifyGenreEntityFormGroupInput = { id: null }): SpotifyGenreEntityFormGroup {
    const spotifyGenreEntityRawValue = {
      ...this.getFormDefaults(),
      ...spotifyGenreEntity,
    };
    return new FormGroup<SpotifyGenreEntityFormGroupContent>({
      id: new FormControl(
        { value: spotifyGenreEntityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      spotifyGenre: new FormControl(spotifyGenreEntityRawValue.spotifyGenre, {
        validators: [Validators.required],
      }),
      song: new FormControl(spotifyGenreEntityRawValue.song),
      album: new FormControl(spotifyGenreEntityRawValue.album),
      mainArtist: new FormControl(spotifyGenreEntityRawValue.mainArtist),
    });
  }

  getSpotifyGenreEntity(form: SpotifyGenreEntityFormGroup): ISpotifyGenreEntity | NewSpotifyGenreEntity {
    return form.getRawValue() as ISpotifyGenreEntity | NewSpotifyGenreEntity;
  }

  resetForm(form: SpotifyGenreEntityFormGroup, spotifyGenreEntity: SpotifyGenreEntityFormGroupInput): void {
    const spotifyGenreEntityRawValue = { ...this.getFormDefaults(), ...spotifyGenreEntity };
    form.reset(
      {
        ...spotifyGenreEntityRawValue,
        id: { value: spotifyGenreEntityRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SpotifyGenreEntityFormDefaults {
    return {
      id: null,
    };
  }
}
