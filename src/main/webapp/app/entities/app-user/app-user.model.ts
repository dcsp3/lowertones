import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IAppUser {
  id: number;
  spotifyUserID?: string | null;
  name?: string | null;
  email?: string | null;
  userImageLarge?: string | null;
  userImageMedium?: string | null;
  userImageSmall?: string | null;
  spotifyRefreshToken?: string | null;
  spotifyAuthToken?: string | null;
  lastLoginDate?: dayjs.Dayjs | null;
  discoverWeeklyBufferSettings?: number | null;
  discoverWeeklyBufferPlaylistID?: string | null;
  highContrastMode?: boolean | null;
  textSize?: number | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewAppUser = Omit<IAppUser, 'id'> & { id: null };
