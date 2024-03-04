import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMusicbrainzGenreEntity, NewMusicbrainzGenreEntity } from '../musicbrainz-genre-entity.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMusicbrainzGenreEntity for edit and NewMusicbrainzGenreEntityFormGroupInput for create.
 */
type MusicbrainzGenreEntityFormGroupInput = IMusicbrainzGenreEntity | PartialWithRequiredKeyOf<NewMusicbrainzGenreEntity>;

type MusicbrainzGenreEntityFormDefaults = Pick<NewMusicbrainzGenreEntity, 'id'>;

type MusicbrainzGenreEntityFormGroupContent = {
  id: FormControl<IMusicbrainzGenreEntity['id'] | NewMusicbrainzGenreEntity['id']>;
  musicbrainzGenre: FormControl<IMusicbrainzGenreEntity['musicbrainzGenre']>;
  song: FormControl<IMusicbrainzGenreEntity['song']>;
  album: FormControl<IMusicbrainzGenreEntity['album']>;
  mainArtist: FormControl<IMusicbrainzGenreEntity['mainArtist']>;
};

export type MusicbrainzGenreEntityFormGroup = FormGroup<MusicbrainzGenreEntityFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MusicbrainzGenreEntityFormService {
  createMusicbrainzGenreEntityFormGroup(
    musicbrainzGenreEntity: MusicbrainzGenreEntityFormGroupInput = { id: null }
  ): MusicbrainzGenreEntityFormGroup {
    const musicbrainzGenreEntityRawValue = {
      ...this.getFormDefaults(),
      ...musicbrainzGenreEntity,
    };
    return new FormGroup<MusicbrainzGenreEntityFormGroupContent>({
      id: new FormControl(
        { value: musicbrainzGenreEntityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      musicbrainzGenre: new FormControl(musicbrainzGenreEntityRawValue.musicbrainzGenre, {
        validators: [Validators.required],
      }),
      song: new FormControl(musicbrainzGenreEntityRawValue.song),
      album: new FormControl(musicbrainzGenreEntityRawValue.album),
      mainArtist: new FormControl(musicbrainzGenreEntityRawValue.mainArtist),
    });
  }

  getMusicbrainzGenreEntity(form: MusicbrainzGenreEntityFormGroup): IMusicbrainzGenreEntity | NewMusicbrainzGenreEntity {
    return form.getRawValue() as IMusicbrainzGenreEntity | NewMusicbrainzGenreEntity;
  }

  resetForm(form: MusicbrainzGenreEntityFormGroup, musicbrainzGenreEntity: MusicbrainzGenreEntityFormGroupInput): void {
    const musicbrainzGenreEntityRawValue = { ...this.getFormDefaults(), ...musicbrainzGenreEntity };
    form.reset(
      {
        ...musicbrainzGenreEntityRawValue,
        id: { value: musicbrainzGenreEntityRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MusicbrainzGenreEntityFormDefaults {
    return {
      id: null,
    };
  }
}
