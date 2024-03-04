import { IMusicbrainzGenreEntity, NewMusicbrainzGenreEntity } from './musicbrainz-genre-entity.model';

export const sampleWithRequiredData: IMusicbrainzGenreEntity = {
  id: 71117,
  musicbrainzGenre: 'empowering Executive revolutionize',
};

export const sampleWithPartialData: IMusicbrainzGenreEntity = {
  id: 24089,
  musicbrainzGenre: 'Generic Georgia empower',
};

export const sampleWithFullData: IMusicbrainzGenreEntity = {
  id: 44276,
  musicbrainzGenre: 'Music Toys Music',
};

export const sampleWithNewData: NewMusicbrainzGenreEntity = {
  musicbrainzGenre: 'engineer withdrawal multimedia',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
