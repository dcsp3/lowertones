import dayjs from 'dayjs/esm';
import { IPlaylist } from 'app/entities/playlist/playlist.model';
import { ISong } from 'app/entities/song/song.model';

export interface IPlaylistSongJoin {
  id: number;
  songOrderIndex?: number | null;
  songDateAdded?: dayjs.Dayjs | null;
  playlist?: Pick<IPlaylist, 'id'> | null;
  song?: Pick<ISong, 'id'> | null;
}

export type NewPlaylistSongJoin = Omit<IPlaylistSongJoin, 'id'> & { id: null };
