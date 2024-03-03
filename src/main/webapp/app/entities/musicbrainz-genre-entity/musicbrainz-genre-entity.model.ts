import { ISong } from 'app/entities/song/song.model';
import { IAlbum } from 'app/entities/album/album.model';
import { IMainArtist } from 'app/entities/main-artist/main-artist.model';

export interface IMusicbrainzGenreEntity {
  id: number;
  musicbrainzGenre?: string | null;
  song?: Pick<ISong, 'id'> | null;
  album?: Pick<IAlbum, 'id'> | null;
  mainArtist?: Pick<IMainArtist, 'id'> | null;
}

export type NewMusicbrainzGenreEntity = Omit<IMusicbrainzGenreEntity, 'id'> & { id: null };
