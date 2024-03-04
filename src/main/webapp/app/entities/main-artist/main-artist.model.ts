import dayjs from 'dayjs/esm';

export interface IMainArtist {
  id: number;
  artistSpotifyID?: string | null;
  artistName?: string | null;
  artistPopularity?: number | null;
  artistImage?: string | null;
  artistFollowers?: number | null;
  dateAddedToDB?: dayjs.Dayjs | null;
  dateLastModified?: dayjs.Dayjs | null;
}

export type NewMainArtist = Omit<IMainArtist, 'id'> & { id: null };
