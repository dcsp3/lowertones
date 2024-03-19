import dayjs from 'dayjs/esm';

import { IMainArtist, NewMainArtist } from './main-artist.model';

export const sampleWithRequiredData: IMainArtist = {
  id: 47418,
  artistSpotifyID: 'Phased',
  artistName: 'Underpass XML',
  artistPopularity: 58918,
  artistImageSmall: 'exploit',
  artistImageMedium: 'cyan Buckinghamshire Uganda',
  artistImageLarge: 'Handmade Wooden Forward',
};

export const sampleWithPartialData: IMainArtist = {
  id: 33596,
  artistSpotifyID: 'policy analyzer',
  artistName: 'calculating Frozen',
  artistPopularity: 39356,
  artistImageSmall: 'Markets Interactions',
  artistImageMedium: 'Tuna transform Shilling',
  artistImageLarge: 'Enterprise-wide',
  artistFollowers: 86443,
  dateAddedToDB: dayjs('2024-03-03'),
  dateLastModified: dayjs('2024-03-03'),
};

export const sampleWithFullData: IMainArtist = {
  id: 16225,
  artistSpotifyID: 'management',
  artistName: 'Developer',
  artistPopularity: 88640,
  artistImageSmall: 'withdrawal multi-byte',
  artistImageMedium: 'virtual Coordinator hack',
  artistImageLarge: 'radical',
  artistFollowers: 8967,
  dateAddedToDB: dayjs('2024-03-02'),
  dateLastModified: dayjs('2024-03-02'),
};

export const sampleWithNewData: NewMainArtist = {
  artistSpotifyID: 'Rubber feed',
  artistName: 'Metal quantify Assimilated',
  artistPopularity: 1430,
  artistImageSmall: 'Orchestrator Steel',
  artistImageMedium: 'quantify Account optical',
  artistImageLarge: 'state',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
