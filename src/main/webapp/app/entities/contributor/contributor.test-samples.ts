import { IContributor, NewContributor } from './contributor.model';

export const sampleWithRequiredData: IContributor = {
  id: 9846,
};

export const sampleWithPartialData: IContributor = {
  id: 79719,
  name: 'Benin Sleek Isle',
  musicbrainzID: 'Quality',
};

export const sampleWithFullData: IContributor = {
  id: 51660,
  name: 'Computers Alabama value-added',
  role: 'Massachusetts',
  musicbrainzID: 'attitude',
};

export const sampleWithNewData: NewContributor = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
