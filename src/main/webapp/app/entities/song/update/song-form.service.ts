import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISong, NewSong } from '../song.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISong for edit and NewSongFormGroupInput for create.
 */
type SongFormGroupInput = ISong | PartialWithRequiredKeyOf<NewSong>;

type SongFormDefaults = Pick<NewSong, 'id' | 'songExplicit' | 'songTrackFeaturesAdded' | 'contributors'>;

type SongFormGroupContent = {
  id: FormControl<ISong['id'] | NewSong['id']>;
  songSpotifyID: FormControl<ISong['songSpotifyID']>;
  songTitle: FormControl<ISong['songTitle']>;
  songDuration: FormControl<ISong['songDuration']>;
  songAlbumType: FormControl<ISong['songAlbumType']>;
  songAlbumID: FormControl<ISong['songAlbumID']>;
  songExplicit: FormControl<ISong['songExplicit']>;
  songPopularity: FormControl<ISong['songPopularity']>;
  songPreviewURL: FormControl<ISong['songPreviewURL']>;
  songTrackFeaturesAdded: FormControl<ISong['songTrackFeaturesAdded']>;
  songAcousticness: FormControl<ISong['songAcousticness']>;
  songDanceability: FormControl<ISong['songDanceability']>;
  songEnergy: FormControl<ISong['songEnergy']>;
  songInstrumentalness: FormControl<ISong['songInstrumentalness']>;
  songLiveness: FormControl<ISong['songLiveness']>;
  songLoudness: FormControl<ISong['songLoudness']>;
  songSpeechiness: FormControl<ISong['songSpeechiness']>;
  songTempo: FormControl<ISong['songTempo']>;
  songValence: FormControl<ISong['songValence']>;
  songKey: FormControl<ISong['songKey']>;
  songTimeSignature: FormControl<ISong['songTimeSignature']>;
  songDateAddedToDB: FormControl<ISong['songDateAddedToDB']>;
  songDateLastModified: FormControl<ISong['songDateLastModified']>;
  recordingMBID: FormControl<ISong['recordingMBID']>;
  contributors: FormControl<ISong['contributors']>;
  album: FormControl<ISong['album']>;
};

export type SongFormGroup = FormGroup<SongFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SongFormService {
  createSongFormGroup(song: SongFormGroupInput = { id: null }): SongFormGroup {
    const songRawValue = {
      ...this.getFormDefaults(),
      ...song,
    };
    return new FormGroup<SongFormGroupContent>({
      id: new FormControl(
        { value: songRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      songSpotifyID: new FormControl(songRawValue.songSpotifyID, {
        validators: [Validators.required],
      }),
      songTitle: new FormControl(songRawValue.songTitle, {
        validators: [Validators.required, Validators.maxLength(1000)],
      }),
      songDuration: new FormControl(songRawValue.songDuration, {
        validators: [Validators.required],
      }),
      songAlbumType: new FormControl(songRawValue.songAlbumType, {
        validators: [Validators.required],
      }),
      songAlbumID: new FormControl(songRawValue.songAlbumID, {
        validators: [Validators.required],
      }),
      songExplicit: new FormControl(songRawValue.songExplicit, {
        validators: [Validators.required],
      }),
      songPopularity: new FormControl(songRawValue.songPopularity, {
        validators: [Validators.required],
      }),
      songPreviewURL: new FormControl(songRawValue.songPreviewURL),
      songTrackFeaturesAdded: new FormControl(songRawValue.songTrackFeaturesAdded, {
        validators: [Validators.required],
      }),
      songAcousticness: new FormControl(songRawValue.songAcousticness),
      songDanceability: new FormControl(songRawValue.songDanceability),
      songEnergy: new FormControl(songRawValue.songEnergy),
      songInstrumentalness: new FormControl(songRawValue.songInstrumentalness),
      songLiveness: new FormControl(songRawValue.songLiveness),
      songLoudness: new FormControl(songRawValue.songLoudness),
      songSpeechiness: new FormControl(songRawValue.songSpeechiness),
      songTempo: new FormControl(songRawValue.songTempo),
      songValence: new FormControl(songRawValue.songValence),
      songKey: new FormControl(songRawValue.songKey),
      songTimeSignature: new FormControl(songRawValue.songTimeSignature),
      songDateAddedToDB: new FormControl(songRawValue.songDateAddedToDB, {
        validators: [Validators.required],
      }),
      songDateLastModified: new FormControl(songRawValue.songDateLastModified, {
        validators: [Validators.required],
      }),
      recordingMBID: new FormControl(songRawValue.recordingMBID),
      contributors: new FormControl(songRawValue.contributors ?? []),
      album: new FormControl(songRawValue.album),
    });
  }

  getSong(form: SongFormGroup): ISong | NewSong {
    return form.getRawValue() as ISong | NewSong;
  }

  resetForm(form: SongFormGroup, song: SongFormGroupInput): void {
    const songRawValue = { ...this.getFormDefaults(), ...song };
    form.reset(
      {
        ...songRawValue,
        id: { value: songRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SongFormDefaults {
    return {
      id: null,
      songExplicit: false,
      songTrackFeaturesAdded: false,
      contributors: [],
    };
  }
}
