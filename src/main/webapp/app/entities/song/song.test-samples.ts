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
  id: 13938,
  songSpotifyID: 'holistic',
  songTitle: 'partnerships RSS Bulgaria',
  songDuration: 70415,
  songAlbumType: AlbumType['ALBUM'],
  songAlbumID: 'archive productize',
  songExplicit: true,
  songPopularity: 68054,
  songTrackFeaturesAdded: true,
  songDanceability: 83773,
  songEnergy: 33378,
  songLoudness: 85964,
  songTempo: 86575,
  songValence: 70715,
  songKey: 52493,
  songDateAddedToDB: dayjs('2024-03-03'),
  songDateLastModified: dayjs('2024-03-03'),
  recordingMBID: 'didactic',
};

export const sampleWithFullData: ISong = {
  id: 73751,
  songSpotifyID: 'Andorra',
  songTitle: 'Solutions Ergonomic protocol',
  songDuration: 49406,
  songAlbumType: AlbumType['SINGLE'],
  songAlbumID: 'array vortals',
  songExplicit: false,
  songPopularity: 3720,
  songPreviewURL: 'Representative',
  songTrackFeaturesAdded: false,
  songAcousticness: 55702,
  songDanceability: 97426,
  songEnergy: 24227,
  songInstrumentalness: 65446,
  songLiveness: 29562,
  songLoudness: 88122,
  songSpeechiness: 80455,
  songTempo: 41607,
  songValence: 38499,
  songKey: 41392,
  songTimeSignature: 60883,
  songDateAddedToDB: dayjs('2024-03-03'),
  songDateLastModified: dayjs('2024-03-03'),
  recordingMBID: 'Grocery Dinar',
};

export const sampleWithNewData: NewSong = {
  songSpotifyID: 'Towels synergies',
  songTitle: 'Texas reciprocal emulation',
  songDuration: 94097,
  songAlbumType: AlbumType['COMPILATION'],
  songAlbumID: 'SMTP',
  songExplicit: false,
  songPopularity: 2015,
  songTrackFeaturesAdded: true,
  songDateAddedToDB: dayjs('2024-03-03'),
  songDateLastModified: dayjs('2024-03-03'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
