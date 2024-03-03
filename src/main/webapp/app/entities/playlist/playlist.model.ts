import dayjs from 'dayjs/esm';
import { IAppUser } from 'app/entities/app-user/app-user.model';

export interface IPlaylist {
  id: number;
  dateAddedToDB?: dayjs.Dayjs | null;
  dateLastModified?: dayjs.Dayjs | null;
  playlistSpotifyID?: string | null;
  playlistName?: string | null;
  playlistPhoto?: string | null;
  appUser?: Pick<IAppUser, 'id'> | null;
}

export type NewPlaylist = Omit<IPlaylist, 'id'> & { id: null };
