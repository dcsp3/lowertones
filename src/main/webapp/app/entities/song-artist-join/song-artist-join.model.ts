import { ISong } from 'app/entities/song/song.model';
import { IMainArtist } from 'app/entities/main-artist/main-artist.model';

export interface ISongArtistJoin {
  id: number;
  topTrackIndex?: number | null;
  song?: Pick<ISong, 'id'> | null;
  mainArtist?: Pick<IMainArtist, 'id'> | null;
}

export type NewSongArtistJoin = Omit<ISongArtistJoin, 'id'> & { id: null };
