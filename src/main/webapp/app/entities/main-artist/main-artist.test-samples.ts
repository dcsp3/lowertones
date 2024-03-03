import dayjs from 'dayjs/esm';

import { IMainArtist, NewMainArtist } from './main-artist.model';

export const sampleWithRequiredData: IMainArtist = {
  id: 47418,
  artistSpotifyID: 'Phased',
  artistName: 'Underpass XML',
  artistPopularity: 58918,
  artistImage: 'exploit',
};

export const sampleWithPartialData: IMainArtist = {
  id: 91375,
  artistSpotifyID: 'copy copying',
  artistName: 'transmit',
  artistPopularity: 14040,
  artistImage: 'Forward invoice dynamic',
  artistFollowers: 73951,
  dateLastModified: dayjs('2024-03-02'),
};

export const sampleWithFullData: IMainArtist = {
  id: 32527,
  artistSpotifyID: 'Granite',
  artistName: 'Senior Assistant grow',
  artistPopularity: 38398,
  artistImage: 'Interactions Marketing withdrawal',
  artistFollowers: 97064,
  dateAddedToDB: dayjs('2024-03-03'),
  dateLastModified: dayjs('2024-03-02'),
};

export const sampleWithNewData: NewMainArtist = {
  artistSpotifyID: 'database transmitting Games',
  artistName: 'Rubber',
  artistPopularity: 99488,
  artistImage: 'Fresh withdrawal multi-byte',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
