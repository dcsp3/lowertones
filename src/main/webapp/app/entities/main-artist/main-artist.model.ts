import dayjs from 'dayjs/esm';
import { IRelatedArtists } from 'app/entities/related-artists/related-artists.model';
import { IAlbum } from 'app/entities/album/album.model';

export interface IMainArtist {
  id: number;
  artistSpotifyID?: string | null;
  artistName?: string | null;
  artistPopularity?: number | null;
  artistImageSmall?: string | null;
  artistImageMedium?: string | null;
  artistImageLarge?: string | null;
  artistFollowers?: number | null;
  dateAddedToDB?: dayjs.Dayjs | null;
  dateLastModified?: dayjs.Dayjs | null;
  musicbrainzID?: string | null;
  relatedArtists?: Pick<IRelatedArtists, 'id'> | null;
  albums?: Pick<IAlbum, 'id'>[] | null;
}

export type NewMainArtist = Omit<IMainArtist, 'id'> & { id: null };
