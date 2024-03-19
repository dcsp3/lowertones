import dayjs from 'dayjs/esm';
import { IContributor } from 'app/entities/contributor/contributor.model';
import { IAlbum } from 'app/entities/album/album.model';
import { AlbumType } from 'app/entities/enumerations/album-type.model';

export interface ISong {
  id: number;
  songSpotifyID?: string | null;
  songTitle?: string | null;
  songDuration?: number | null;
  songAlbumType?: AlbumType | null;
  songAlbumID?: string | null;
  songExplicit?: boolean | null;
  songPopularity?: number | null;
  songPreviewURL?: string | null;
  songTrackFeaturesAdded?: boolean | null;
  songAcousticness?: number | null;
  songDanceability?: number | null;
  songEnergy?: number | null;
  songInstrumentalness?: number | null;
  songLiveness?: number | null;
  songLoudness?: number | null;
  songSpeechiness?: number | null;
  songTempo?: number | null;
  songValence?: number | null;
  songKey?: number | null;
  songTimeSignature?: number | null;
  songDateAddedToDB?: dayjs.Dayjs | null;
  songDateLastModified?: dayjs.Dayjs | null;
  contributors?: Pick<IContributor, 'id'>[] | null;
  album?: Pick<IAlbum, 'id'> | null;
}

export type NewSong = Omit<ISong, 'id'> & { id: null };
