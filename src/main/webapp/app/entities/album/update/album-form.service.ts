import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAlbum, NewAlbum } from '../album.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAlbum for edit and NewAlbumFormGroupInput for create.
 */
type AlbumFormGroupInput = IAlbum | PartialWithRequiredKeyOf<NewAlbum>;

type AlbumFormDefaults = Pick<NewAlbum, 'id' | 'musicbrainzMetadataAdded'>;

type AlbumFormGroupContent = {
  id: FormControl<IAlbum['id'] | NewAlbum['id']>;
  albumSpotifyID: FormControl<IAlbum['albumSpotifyID']>;
  albumName: FormControl<IAlbum['albumName']>;
  albumCoverArt: FormControl<IAlbum['albumCoverArt']>;
  albumReleaseDate: FormControl<IAlbum['albumReleaseDate']>;
  releaseDatePrecision: FormControl<IAlbum['releaseDatePrecision']>;
  albumPopularity: FormControl<IAlbum['albumPopularity']>;
  albumType: FormControl<IAlbum['albumType']>;
  spotifyAlbumUPC: FormControl<IAlbum['spotifyAlbumUPC']>;
  spotifyAlbumEAN: FormControl<IAlbum['spotifyAlbumEAN']>;
  spotifyAlbumISRC: FormControl<IAlbum['spotifyAlbumISRC']>;
  dateAddedToDB: FormControl<IAlbum['dateAddedToDB']>;
  dateLastModified: FormControl<IAlbum['dateLastModified']>;
  musicbrainzMetadataAdded: FormControl<IAlbum['musicbrainzMetadataAdded']>;
  musicbrainzID: FormControl<IAlbum['musicbrainzID']>;
  mainArtist: FormControl<IAlbum['mainArtist']>;
};

export type AlbumFormGroup = FormGroup<AlbumFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AlbumFormService {
  createAlbumFormGroup(album: AlbumFormGroupInput = { id: null }): AlbumFormGroup {
    const albumRawValue = {
      ...this.getFormDefaults(),
      ...album,
    };
    return new FormGroup<AlbumFormGroupContent>({
      id: new FormControl(
        { value: albumRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      albumSpotifyID: new FormControl(albumRawValue.albumSpotifyID, {
        validators: [Validators.required],
      }),
      albumName: new FormControl(albumRawValue.albumName, {
        validators: [Validators.required],
      }),
      albumCoverArt: new FormControl(albumRawValue.albumCoverArt, {
        validators: [Validators.required],
      }),
      albumReleaseDate: new FormControl(albumRawValue.albumReleaseDate, {
        validators: [Validators.required],
      }),
      releaseDatePrecision: new FormControl(albumRawValue.releaseDatePrecision, {
        validators: [Validators.required],
      }),
      albumPopularity: new FormControl(albumRawValue.albumPopularity, {
        validators: [Validators.required],
      }),
      albumType: new FormControl(albumRawValue.albumType, {
        validators: [Validators.required],
      }),
      spotifyAlbumUPC: new FormControl(albumRawValue.spotifyAlbumUPC),
      spotifyAlbumEAN: new FormControl(albumRawValue.spotifyAlbumEAN),
      spotifyAlbumISRC: new FormControl(albumRawValue.spotifyAlbumISRC),
      dateAddedToDB: new FormControl(albumRawValue.dateAddedToDB, {
        validators: [Validators.required],
      }),
      dateLastModified: new FormControl(albumRawValue.dateLastModified, {
        validators: [Validators.required],
      }),
      musicbrainzMetadataAdded: new FormControl(albumRawValue.musicbrainzMetadataAdded, {
        validators: [Validators.required],
      }),
      musicbrainzID: new FormControl(albumRawValue.musicbrainzID),
      mainArtist: new FormControl(albumRawValue.mainArtist),
    });
  }

  getAlbum(form: AlbumFormGroup): IAlbum | NewAlbum {
    return form.getRawValue() as IAlbum | NewAlbum;
  }

  resetForm(form: AlbumFormGroup, album: AlbumFormGroupInput): void {
    const albumRawValue = { ...this.getFormDefaults(), ...album };
    form.reset(
      {
        ...albumRawValue,
        id: { value: albumRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AlbumFormDefaults {
    return {
      id: null,
      musicbrainzMetadataAdded: false,
    };
  }
}
