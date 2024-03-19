import dayjs from 'dayjs/esm';

import { IPlaylistSongJoin, NewPlaylistSongJoin } from './playlist-song-join.model';

export const sampleWithRequiredData: IPlaylistSongJoin = {
  id: 40284,
  songOrderIndex: 67461,
  songDateAdded: dayjs('2024-03-03'),
};

export const sampleWithPartialData: IPlaylistSongJoin = {
  id: 8043,
  songOrderIndex: 21929,
  songDateAdded: dayjs('2024-03-03'),
};

export const sampleWithFullData: IPlaylistSongJoin = {
  id: 42623,
  songOrderIndex: 6283,
  songDateAdded: dayjs('2024-03-03'),
};

export const sampleWithNewData: NewPlaylistSongJoin = {
  songOrderIndex: 79087,
  songDateAdded: dayjs('2024-03-03'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
