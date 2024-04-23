import dayjs from 'dayjs/esm';

export interface IVault {
  id: number;
  userId?: number | null;
  sourcePlaylistID?: string | null;
  playlistName?: string | null;
  resultPlaylistID?: string | null;
  frequency?: string | null;
  type?: string | null;
  playlistCoverURL?: string | null;
  playlistSnapshotID?: string | null;
  dateLastUpdated?: dayjs.Dayjs | null;
}

export type NewVault = Omit<IVault, 'id'> & { id: null };
