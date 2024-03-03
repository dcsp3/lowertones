import dayjs from 'dayjs/esm';

import { IPlaylist, NewPlaylist } from './playlist.model';

export const sampleWithRequiredData: IPlaylist = {
  id: 45404,
  dateAddedToDB: dayjs('2024-03-03'),
  dateLastModified: dayjs('2024-03-03'),
  playlistSpotifyID: 'Rubber withdrawal',
  playlistName: 'world-class synthesize invoice',
};

export const sampleWithPartialData: IPlaylist = {
  id: 32931,
  dateAddedToDB: dayjs('2024-03-03'),
  dateLastModified: dayjs('2024-03-03'),
  playlistSpotifyID: 'Research',
  playlistName: 'Open-architected FTP distributed',
};

export const sampleWithFullData: IPlaylist = {
  id: 34557,
  dateAddedToDB: dayjs('2024-03-03'),
  dateLastModified: dayjs('2024-03-03'),
  playlistSpotifyID: 'Avon',
  playlistName: 'up Course',
  playlistPhoto: 'hacking Human',
};

export const sampleWithNewData: NewPlaylist = {
  dateAddedToDB: dayjs('2024-03-03'),
  dateLastModified: dayjs('2024-03-03'),
  playlistSpotifyID: 'Dynamic Gorgeous Pizza',
  playlistName: 'niches',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
