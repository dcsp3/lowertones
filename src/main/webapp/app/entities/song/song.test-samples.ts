import dayjs from 'dayjs/esm';

import { AlbumType } from 'app/entities/enumerations/album-type.model';

import { ISong, NewSong } from './song.model';

export const sampleWithRequiredData: ISong = {
  id: 99772,
  songSpotifyID: 'Health',
  songTitle: 'magenta Dynamic',
  songDuration: 64493,
  songAlbumType: AlbumType['COMPILATION'],
  songAlbumID: 'interface',
  songExplicit: false,
  songPopularity: 30167,
  songTrackFeaturesAdded: false,
  songDateAddedToDB: dayjs('2024-03-03'),
  songDateLastModified: dayjs('2024-03-02'),
};

export const sampleWithPartialData: ISong = {
  id: 79730,
  songSpotifyID: 'definition',
  songTitle: 'system Facilitator generate',
  songDuration: 38944,
  songAlbumType: AlbumType['COMPILATION'],
  songAlbumID: 'withdrawal',
  songExplicit: true,
  songPopularity: 38416,
  songTrackFeaturesAdded: true,
  songDanceability: 24009,
  songEnergy: 89589,
  songLoudness: 68054,
  songTempo: 68193,
  songValence: 83773,
  songKey: 33378,
  songDateAddedToDB: dayjs('2024-03-03'),
  songDateLastModified: dayjs('2024-03-03'),
};

export const sampleWithFullData: ISong = {
  id: 70715,
  songSpotifyID: 'Shoes didactic',
  songTitle: 'Hat state Kids',
  songDuration: 7246,
  songAlbumType: AlbumType['SINGLE'],
  songAlbumID: 'Clothing',
  songExplicit: false,
  songPopularity: 49406,
  songPreviewURL: 'payment Berkshire',
  songTrackFeaturesAdded: true,
  songAcousticness: 34751,
  songDanceability: 3720,
  songEnergy: 11276,
  songInstrumentalness: 99029,
  songLiveness: 93631,
  songLoudness: 46230,
  songSpeechiness: 15135,
  songTempo: 55702,
  songValence: 97426,
  songKey: 24227,
  songTimeSignature: 65446,
  songDateAddedToDB: dayjs('2024-03-03'),
  songDateLastModified: dayjs('2024-03-02'),
};

export const sampleWithNewData: NewSong = {
  songSpotifyID: 'niches Investment value-added',
  songTitle: 'Dinar Drive',
  songDuration: 16285,
  songAlbumType: AlbumType['SINGLE'],
  songAlbumID: 'International',
  songExplicit: true,
  songPopularity: 77488,
  songTrackFeaturesAdded: false,
  songDateAddedToDB: dayjs('2024-03-03'),
  songDateLastModified: dayjs('2024-03-03'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
