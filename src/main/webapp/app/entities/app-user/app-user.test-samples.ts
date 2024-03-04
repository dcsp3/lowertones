import dayjs from 'dayjs/esm';

import { IAppUser, NewAppUser } from './app-user.model';

export const sampleWithRequiredData: IAppUser = {
  id: 1883,
  spotifyUserID: 'compressing Latvian Account',
  name: 'Outdoors Health Gloves',
  email: 'Justina_Mueller45@gmail.com',
  lastLoginDate: dayjs('2024-03-03'),
  discoverWeeklyBufferSettings: 2176,
  darkMode: false,
};

export const sampleWithPartialData: IAppUser = {
  id: 15664,
  spotifyUserID: 'Mountains',
  name: 'monitor',
  email: 'Libbie80@yahoo.com',
  spotifyAuthToken: 'Overpass applications Mandatory',
  lastLoginDate: dayjs('2024-03-03'),
  discoverWeeklyBufferSettings: 16875,
  darkMode: true,
};

export const sampleWithFullData: IAppUser = {
  id: 14090,
  spotifyUserID: 'indexing',
  name: 'Card Summit Concrete',
  email: 'Shania88@yahoo.com',
  spotifyRefreshToken: 'invoice',
  spotifyAuthToken: 'Ohio one-to-one Oklahoma',
  lastLoginDate: dayjs('2024-03-03'),
  discoverWeeklyBufferSettings: 92014,
  discoverWeeklyBufferPlaylistID: 'Steel bricks-and-clicks Soft',
  darkMode: true,
};

export const sampleWithNewData: NewAppUser = {
  spotifyUserID: 'Refined Islands, Jordan',
  name: 'Account',
  email: 'Graciela.Moore56@hotmail.com',
  lastLoginDate: dayjs('2024-03-03'),
  discoverWeeklyBufferSettings: 80684,
  darkMode: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
