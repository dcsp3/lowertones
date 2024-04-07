import dayjs from 'dayjs/esm';

import { IMainArtist, NewMainArtist } from './main-artist.model';

export const sampleWithRequiredData: IMainArtist = {
  id: 47418,
  artistSpotifyID: 'Phased',
  artistName: 'Underpass XML',
  artistPopularity: 58918,
};

export const sampleWithPartialData: IMainArtist = {
  id: 91375,
  artistSpotifyID: 'copy copying',
  artistName: 'transmit',
  artistPopularity: 14040,
  artistImageLarge: 'Forward invoice dynamic',
  artistFollowers: 73951,
  dateAddedToDB: dayjs('2024-03-02'),
  musicbrainzID: 'Bacon',
};

export const sampleWithFullData: IMainArtist = {
  id: 89637,
  artistSpotifyID: 'silver',
  artistName: 'grow transition',
  artistPopularity: 90723,
  artistImageSmall: 'Marketing',
  artistImageMedium: 'transform Shilling',
  artistImageLarge: 'Enterprise-wide',
  artistFollowers: 86443,
  dateAddedToDB: dayjs('2024-03-03'),
  dateLastModified: dayjs('2024-03-03'),
  musicbrainzID: 'orange',
};

export const sampleWithNewData: NewMainArtist = {
  artistSpotifyID: 'Computer bandwidth',
  artistName: 'multi-byte',
  artistPopularity: 71193,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
