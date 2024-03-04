import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISongArtistJoin, NewSongArtistJoin } from '../song-artist-join.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISongArtistJoin for edit and NewSongArtistJoinFormGroupInput for create.
 */
type SongArtistJoinFormGroupInput = ISongArtistJoin | PartialWithRequiredKeyOf<NewSongArtistJoin>;

type SongArtistJoinFormDefaults = Pick<NewSongArtistJoin, 'id'>;

type SongArtistJoinFormGroupContent = {
  id: FormControl<ISongArtistJoin['id'] | NewSongArtistJoin['id']>;
  topTrackIndex: FormControl<ISongArtistJoin['topTrackIndex']>;
  song: FormControl<ISongArtistJoin['song']>;
  mainArtist: FormControl<ISongArtistJoin['mainArtist']>;
};

export type SongArtistJoinFormGroup = FormGroup<SongArtistJoinFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SongArtistJoinFormService {
  createSongArtistJoinFormGroup(songArtistJoin: SongArtistJoinFormGroupInput = { id: null }): SongArtistJoinFormGroup {
    const songArtistJoinRawValue = {
      ...this.getFormDefaults(),
      ...songArtistJoin,
    };
    return new FormGroup<SongArtistJoinFormGroupContent>({
      id: new FormControl(
        { value: songArtistJoinRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      topTrackIndex: new FormControl(songArtistJoinRawValue.topTrackIndex),
      song: new FormControl(songArtistJoinRawValue.song),
      mainArtist: new FormControl(songArtistJoinRawValue.mainArtist),
    });
  }

  getSongArtistJoin(form: SongArtistJoinFormGroup): ISongArtistJoin | NewSongArtistJoin {
    return form.getRawValue() as ISongArtistJoin | NewSongArtistJoin;
  }

  resetForm(form: SongArtistJoinFormGroup, songArtistJoin: SongArtistJoinFormGroupInput): void {
    const songArtistJoinRawValue = { ...this.getFormDefaults(), ...songArtistJoin };
    form.reset(
      {
        ...songArtistJoinRawValue,
        id: { value: songArtistJoinRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SongArtistJoinFormDefaults {
    return {
      id: null,
    };
  }
}
