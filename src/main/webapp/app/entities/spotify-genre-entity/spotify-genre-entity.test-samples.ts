import { ISpotifyGenreEntity, NewSpotifyGenreEntity } from './spotify-genre-entity.model';

export const sampleWithRequiredData: ISpotifyGenreEntity = {
  id: 56192,
  spotifyGenre: 'calculate Health leverage',
};

export const sampleWithPartialData: ISpotifyGenreEntity = {
  id: 10027,
  spotifyGenre: 'silver',
};

export const sampleWithFullData: ISpotifyGenreEntity = {
  id: 93573,
  spotifyGenre: 'Lake bluetooth',
};

export const sampleWithNewData: NewSpotifyGenreEntity = {
  spotifyGenre: 'hacking',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
