import { IVault, NewVault } from './vault.model';

export const sampleWithRequiredData: IVault = {
  id: 54693,
};

export const sampleWithPartialData: IVault = {
  id: 87937,
  sourcePlaylistID: 'invoice transmitting Assistant',
  frequency: 'Wyoming Optimization Computers',
  type: 'calculate Home',
  playlistSnapshotID: 'Future Plastic monitoring',
};

export const sampleWithFullData: IVault = {
  id: 65642,
  sourcePlaylistID: 'Architect Garden Connecticut',
  playlistName: 'Unbranded',
  resultPlaylistID: 'Handcrafted sensor',
  frequency: 'Automotive 1080p',
  type: 'Sudanese Account',
  playlistCoverURL: 'Soft',
  playlistSnapshotID: 'Developer Toys Group',
};

export const sampleWithNewData: NewVault = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
