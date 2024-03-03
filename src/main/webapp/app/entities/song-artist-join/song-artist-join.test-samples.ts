import { ISongArtistJoin, NewSongArtistJoin } from './song-artist-join.model';

export const sampleWithRequiredData: ISongArtistJoin = {
  id: 41793,
};

export const sampleWithPartialData: ISongArtistJoin = {
  id: 4332,
};

export const sampleWithFullData: ISongArtistJoin = {
  id: 92424,
  topTrackIndex: 47015,
};

export const sampleWithNewData: NewSongArtistJoin = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
