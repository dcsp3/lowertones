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
  emailUpdatesEnabled: true,
};

export const sampleWithPartialData: IAppUser = {
  id: 9400,
  spotifyUserID: 'application Checking solid',
  name: 'violet white',
  email: 'Elenora.Cummings25@hotmail.com',
  spotifyAuthToken: 'Harbor Card Summit',
  lastLoginDate: dayjs('2024-03-03'),
  discoverWeeklyBufferSettings: 20474,
  discoverWeeklyBufferPlaylistID: 'transition',
  highContrastMode: false,
  textSize: 88061,
  emailUpdatesEnabled: false,
};

export const sampleWithFullData: IAppUser = {
  id: 64159,
  spotifyUserID: 'Total Peso Visionary',
  name: 'green hack Louisiana',
  email: 'Bertram_Renner@gmail.com',
  userImageLarge: 'Refined Islands, Jordan',
  userImageMedium: 'Account',
  userImageSmall: 'maximize Toys haptic',
  spotifyRefreshToken: 'Licensed copy Cotton',
  spotifyAuthToken: 'algorithm Multi-channelled',
  lastLoginDate: dayjs('2024-03-03'),
  discoverWeeklyBufferSettings: 68961,
  discoverWeeklyBufferPlaylistID: 'Assistant',
  highContrastMode: true,
  textSize: 47573,
  emailUpdatesEnabled: true,
};

export const sampleWithNewData: NewAppUser = {
  spotifyUserID: 'e-services national Handcrafted',
  name: 'plug-and-play',
  email: 'Chester.Jacobs@yahoo.com',
  lastLoginDate: dayjs('2024-03-03'),
  discoverWeeklyBufferSettings: 78567,
  highContrastMode: false,
  textSize: 60396,
  emailUpdatesEnabled: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
