import { IPlaylistSongJoin, NewPlaylistSongJoin } from './playlist-song-join.model';

export const sampleWithRequiredData: IPlaylistSongJoin = {
  id: 40284,
  songOrderIndex: 67461,
};

export const sampleWithPartialData: IPlaylistSongJoin = {
  id: 58906,
  songOrderIndex: 8043,
};

export const sampleWithFullData: IPlaylistSongJoin = {
  id: 21929,
  songOrderIndex: 64323,
};

export const sampleWithNewData: NewPlaylistSongJoin = {
  songOrderIndex: 42623,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
