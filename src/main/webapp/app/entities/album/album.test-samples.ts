import dayjs from 'dayjs/esm';

import { ReleaseDatePrecision } from 'app/entities/enumerations/release-date-precision.model';
import { AlbumType } from 'app/entities/enumerations/album-type.model';

import { IAlbum, NewAlbum } from './album.model';

export const sampleWithRequiredData: IAlbum = {
  id: 51589,
  albumSpotifyID: 'back-end',
  albumName: 'Chips',
  albumCoverArt: 'Computers web Guinea',
  albumReleaseDate: dayjs('2024-03-03'),
  releaseDatePrecision: ReleaseDatePrecision['DAY'],
  albumPopularity: 30069,
  albumType: AlbumType['ALBUM'],
  dateAddedToDB: dayjs('2024-03-03'),
  dateLastModified: dayjs('2024-03-03'),
  musicbrainzMetadataAdded: true,
};

export const sampleWithPartialData: IAlbum = {
  id: 12477,
  albumSpotifyID: 'niches Avon Designer',
  albumName: 'Account Account',
  albumCoverArt: 'Manager Rustic Licensed',
  albumReleaseDate: dayjs('2024-03-03'),
  releaseDatePrecision: ReleaseDatePrecision['YEAR'],
  albumPopularity: 9622,
  albumType: AlbumType['ALBUM'],
  spotifyAlbumEAN: 'Configurable Chief',
  dateAddedToDB: dayjs('2024-03-03'),
  dateLastModified: dayjs('2024-03-03'),
  musicbrainzMetadataAdded: false,
};

export const sampleWithFullData: IAlbum = {
  id: 40858,
  albumSpotifyID: 'Licensed withdrawal',
  albumName: 'Sterling Unbranded payment',
  albumCoverArt: 'Tasty Handmade reintermediate',
  albumReleaseDate: dayjs('2024-03-03'),
  releaseDatePrecision: ReleaseDatePrecision['DAY'],
  albumPopularity: 60099,
  albumType: AlbumType['SINGLE'],
  spotifyAlbumUPC: 'copying wireless Myanmar',
  spotifyAlbumEAN: 'reboot Tala optical',
  spotifyAlbumISRC: 'zero hack',
  dateAddedToDB: dayjs('2024-03-03'),
  dateLastModified: dayjs('2024-03-02'),
  musicbrainzMetadataAdded: false,
  musicbrainzID: 'primary Home turquoise',
};

export const sampleWithNewData: NewAlbum = {
  albumSpotifyID: 'Gloves',
  albumName: 'parse',
  albumCoverArt: 'parsing',
  albumReleaseDate: dayjs('2024-03-03'),
  releaseDatePrecision: ReleaseDatePrecision['MONTH'],
  albumPopularity: 56402,
  albumType: AlbumType['COMPILATION'],
  dateAddedToDB: dayjs('2024-03-03'),
  dateLastModified: dayjs('2024-03-02'),
  musicbrainzMetadataAdded: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
