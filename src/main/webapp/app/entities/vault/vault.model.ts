export interface IVault {
  id: number;
  sourcePlaylistID?: string | null;
  playlistName?: string | null;
  resultPlaylistID?: string | null;
  frequency?: string | null;
  type?: string | null;
  playlistCoverURL?: string | null;
  playlistSnapshotID?: string | null;
}

export type NewVault = Omit<IVault, 'id'> & { id: null };
