import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPlaylistSongJoin, NewPlaylistSongJoin } from '../playlist-song-join.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPlaylistSongJoin for edit and NewPlaylistSongJoinFormGroupInput for create.
 */
type PlaylistSongJoinFormGroupInput = IPlaylistSongJoin | PartialWithRequiredKeyOf<NewPlaylistSongJoin>;

type PlaylistSongJoinFormDefaults = Pick<NewPlaylistSongJoin, 'id'>;

type PlaylistSongJoinFormGroupContent = {
  id: FormControl<IPlaylistSongJoin['id'] | NewPlaylistSongJoin['id']>;
  songOrderIndex: FormControl<IPlaylistSongJoin['songOrderIndex']>;
  songDateAdded: FormControl<IPlaylistSongJoin['songDateAdded']>;
  playlist: FormControl<IPlaylistSongJoin['playlist']>;
  song: FormControl<IPlaylistSongJoin['song']>;
};

export type PlaylistSongJoinFormGroup = FormGroup<PlaylistSongJoinFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PlaylistSongJoinFormService {
  createPlaylistSongJoinFormGroup(playlistSongJoin: PlaylistSongJoinFormGroupInput = { id: null }): PlaylistSongJoinFormGroup {
    const playlistSongJoinRawValue = {
      ...this.getFormDefaults(),
      ...playlistSongJoin,
    };
    return new FormGroup<PlaylistSongJoinFormGroupContent>({
      id: new FormControl(
        { value: playlistSongJoinRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      songOrderIndex: new FormControl(playlistSongJoinRawValue.songOrderIndex, {
        validators: [Validators.required],
      }),
      songDateAdded: new FormControl(playlistSongJoinRawValue.songDateAdded, {
        validators: [Validators.required],
      }),
      playlist: new FormControl(playlistSongJoinRawValue.playlist),
      song: new FormControl(playlistSongJoinRawValue.song),
    });
  }

  getPlaylistSongJoin(form: PlaylistSongJoinFormGroup): IPlaylistSongJoin | NewPlaylistSongJoin {
    return form.getRawValue() as IPlaylistSongJoin | NewPlaylistSongJoin;
  }

  resetForm(form: PlaylistSongJoinFormGroup, playlistSongJoin: PlaylistSongJoinFormGroupInput): void {
    const playlistSongJoinRawValue = { ...this.getFormDefaults(), ...playlistSongJoin };
    form.reset(
      {
        ...playlistSongJoinRawValue,
        id: { value: playlistSongJoinRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PlaylistSongJoinFormDefaults {
    return {
      id: null,
    };
  }
}
