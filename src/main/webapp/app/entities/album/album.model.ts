import dayjs from 'dayjs/esm';
import { IMainArtist } from 'app/entities/main-artist/main-artist.model';
import { ReleaseDatePrecision } from 'app/entities/enumerations/release-date-precision.model';
import { AlbumType } from 'app/entities/enumerations/album-type.model';

export interface IAlbum {
  id: number;
  albumSpotifyID?: string | null;
  albumName?: string | null;
  albumCoverArt?: string | null;
  albumReleaseDate?: dayjs.Dayjs | null;
  releaseDatePrecision?: ReleaseDatePrecision | null;
  albumPopularity?: number | null;
  albumType?: AlbumType | null;
  spotifyAlbumUPC?: string | null;
  spotifyAlbumEAN?: string | null;
  spotifyAlbumISRC?: string | null;
  dateAddedToDB?: dayjs.Dayjs | null;
  dateLastModified?: dayjs.Dayjs | null;
  musicbrainzMetadataAdded?: boolean | null;
  musicbrainzID?: string | null;
  mainArtists?: Pick<IMainArtist, 'id'>[] | null;
}

export type NewAlbum = Omit<IAlbum, 'id'> & { id: null };
