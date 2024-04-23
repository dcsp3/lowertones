import dayjs from 'dayjs/esm';

import { IVault, NewVault } from './vault.model';

export const sampleWithRequiredData: IVault = {
  id: 54693,
};

export const sampleWithPartialData: IVault = {
  id: 65701,
  userId: 84464,
  resultPlaylistID: 'transmitting Assistant',
  frequency: 'Wyoming Optimization Computers',
  playlistCoverURL: 'calculate Home',
  playlistSnapshotID: 'Future Plastic monitoring',
  dateLastUpdated: dayjs('2024-04-21'),
};

export const sampleWithFullData: IVault = {
  id: 89407,
  userId: 98390,
  sourcePlaylistID: 'backing Mongolia',
  playlistName: 'Fresh Handcrafted sensor',
  resultPlaylistID: 'Automotive 1080p',
  frequency: 'Sudanese Account',
  type: 'Soft',
  playlistCoverURL: 'Developer Toys Group',
  playlistSnapshotID: 'bottom-line Inverse',
  dateLastUpdated: dayjs('2024-04-21'),
};

export const sampleWithNewData: NewVault = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
