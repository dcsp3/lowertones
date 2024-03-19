import dayjs from 'dayjs/esm';

import { IPlaylist, NewPlaylist } from './playlist.model';

export const sampleWithRequiredData: IPlaylist = {
  id: 45404,
  dateAddedToDB: dayjs('2024-03-03'),
  dateLastModified: dayjs('2024-03-03'),
  playlistSpotifyID: 'Rubber withdrawal',
  playlistName: 'world-class synthesize invoice',
  playlistSnapshotID: 'extranet Automotive',
};

export const sampleWithPartialData: IPlaylist = {
  id: 51949,
  dateAddedToDB: dayjs('2024-03-03'),
  dateLastModified: dayjs('2024-03-03'),
  playlistSpotifyID: 'Forint',
  playlistName: 'granular Object-based',
  playlistSnapshotID: 'Streets Investment National',
  playlistImageMedium: 'Fresh',
};

export const sampleWithFullData: IPlaylist = {
  id: 71695,
  dateAddedToDB: dayjs('2024-03-03'),
  dateLastModified: dayjs('2024-03-02'),
  playlistSpotifyID: 'revolutionary Directives',
  playlistName: 'Practical',
  playlistSnapshotID: 'virtual Circle auxiliary',
  playlistImageLarge: 'TCP Republic',
  playlistImageMedium: 'user-centric up',
  playlistImageSmall: 'Phased Keyboard',
};

export const sampleWithNewData: NewPlaylist = {
  dateAddedToDB: dayjs('2024-03-03'),
  dateLastModified: dayjs('2024-03-03'),
  playlistSpotifyID: 'Account',
  playlistName: 'synthesizing SCSI',
  playlistSnapshotID: 'Small Secured',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
