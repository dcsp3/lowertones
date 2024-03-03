import { IContributor, NewContributor } from './contributor.model';

export const sampleWithRequiredData: IContributor = {
  id: 9846,
};

export const sampleWithPartialData: IContributor = {
  id: 67514,
  name: 'Incredible Berkshire',
  instrument: 'Steel Dirham',
  musicbrainzID: 'Computers Alabama value-added',
};

export const sampleWithFullData: IContributor = {
  id: 6698,
  name: 'communities attitude',
  role: 'IB Phased Philippines',
  instrument: 'Streets Intelligent',
  musicbrainzID: 'payment Namibia',
};

export const sampleWithNewData: NewContributor = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
