import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMusicBrainzSongAttribution, NewMusicBrainzSongAttribution } from '../music-brainz-song-attribution.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMusicBrainzSongAttribution for edit and NewMusicBrainzSongAttributionFormGroupInput for create.
 */
type MusicBrainzSongAttributionFormGroupInput = IMusicBrainzSongAttribution | PartialWithRequiredKeyOf<NewMusicBrainzSongAttribution>;

type MusicBrainzSongAttributionFormDefaults = Pick<NewMusicBrainzSongAttribution, 'id'>;

type MusicBrainzSongAttributionFormGroupContent = {
  id: FormControl<IMusicBrainzSongAttribution['id'] | NewMusicBrainzSongAttribution['id']>;
  recordingMBID: FormControl<IMusicBrainzSongAttribution['recordingMBID']>;
  recordingTitle: FormControl<IMusicBrainzSongAttribution['recordingTitle']>;
  songMainArtistName: FormControl<IMusicBrainzSongAttribution['songMainArtistName']>;
  songMainArtistID: FormControl<IMusicBrainzSongAttribution['songMainArtistID']>;
  songContributorMBID: FormControl<IMusicBrainzSongAttribution['songContributorMBID']>;
  songContributorName: FormControl<IMusicBrainzSongAttribution['songContributorName']>;
  songContributorRole: FormControl<IMusicBrainzSongAttribution['songContributorRole']>;
  songContributorInstrument: FormControl<IMusicBrainzSongAttribution['songContributorInstrument']>;
};

export type MusicBrainzSongAttributionFormGroup = FormGroup<MusicBrainzSongAttributionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MusicBrainzSongAttributionFormService {
  createMusicBrainzSongAttributionFormGroup(
    musicBrainzSongAttribution: MusicBrainzSongAttributionFormGroupInput = { id: null }
  ): MusicBrainzSongAttributionFormGroup {
    const musicBrainzSongAttributionRawValue = {
      ...this.getFormDefaults(),
      ...musicBrainzSongAttribution,
    };
    return new FormGroup<MusicBrainzSongAttributionFormGroupContent>({
      id: new FormControl(
        { value: musicBrainzSongAttributionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      recordingMBID: new FormControl(musicBrainzSongAttributionRawValue.recordingMBID),
      recordingTitle: new FormControl(musicBrainzSongAttributionRawValue.recordingTitle),
      songMainArtistName: new FormControl(musicBrainzSongAttributionRawValue.songMainArtistName),
      songMainArtistID: new FormControl(musicBrainzSongAttributionRawValue.songMainArtistID),
      songContributorMBID: new FormControl(musicBrainzSongAttributionRawValue.songContributorMBID),
      songContributorName: new FormControl(musicBrainzSongAttributionRawValue.songContributorName),
      songContributorRole: new FormControl(musicBrainzSongAttributionRawValue.songContributorRole),
      songContributorInstrument: new FormControl(musicBrainzSongAttributionRawValue.songContributorInstrument),
    });
  }

  getMusicBrainzSongAttribution(form: MusicBrainzSongAttributionFormGroup): IMusicBrainzSongAttribution | NewMusicBrainzSongAttribution {
    return form.getRawValue() as IMusicBrainzSongAttribution | NewMusicBrainzSongAttribution;
  }

  resetForm(form: MusicBrainzSongAttributionFormGroup, musicBrainzSongAttribution: MusicBrainzSongAttributionFormGroupInput): void {
    const musicBrainzSongAttributionRawValue = { ...this.getFormDefaults(), ...musicBrainzSongAttribution };
    form.reset(
      {
        ...musicBrainzSongAttributionRawValue,
        id: { value: musicBrainzSongAttributionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MusicBrainzSongAttributionFormDefaults {
    return {
      id: null,
    };
  }
}
