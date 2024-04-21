import dayjs from 'dayjs/esm';

import { IAppUser, NewAppUser } from './app-user.model';

export const sampleWithRequiredData: IAppUser = {
  id: 1883,
  spotifyUserID: 'compressing Latvian Account',
  name: 'Outdoors Health Gloves',
  email: 'Justina_Mueller45@gmail.com',
  lastLoginDate: dayjs('2024-03-03'),
  discoverWeeklyBufferSettings: 2176,
  highContrastMode: false,
  textSize: 47641,
};

export const sampleWithPartialData: IAppUser = {
  id: 70686,
  spotifyUserID: 'monitor',
  name: 'Checking solid',
  email: 'Cletus.Kihn22@yahoo.com',
  userImageLarge: 'Dalasi panel',
  lastLoginDate: dayjs('2024-03-03'),
  discoverWeeklyBufferSettings: 46955,
  discoverWeeklyBufferPlaylistID: 'pixel',
  highContrastMode: false,
  textSize: 100,
};

export const sampleWithFullData: IAppUser = {
  id: 84629,
  spotifyUserID: 'Concrete transition Salad',
  name: 'compressing Ohio',
  email: 'Damon_Wunsch@yahoo.com',
  userImageLarge: 'azure',
  userImageMedium: 'Awesome',
  userImageSmall: 'array',
  spotifyRefreshToken: 'Refined Islands, Jordan',
  spotifyAuthToken: 'Account',
  lastLoginDate: dayjs('2024-03-02'),
  discoverWeeklyBufferSettings: 39733,
  discoverWeeklyBufferPlaylistID: 'up projection',
  highContrastMode: false,
  textSize: 115,
};

export const sampleWithNewData: NewAppUser = {
  spotifyUserID: 'quantify',
  name: 'Cross-group',
  email: 'Justine88@gmail.com',
  lastLoginDate: dayjs('2024-03-03'),
  discoverWeeklyBufferSettings: 45764,
  highContrastMode: true,
  textSize: 125,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
