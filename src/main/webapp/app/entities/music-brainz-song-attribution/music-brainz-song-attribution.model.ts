export interface IMusicBrainzSongAttribution {
  id: number;
  recordingMBID?: string | null;
  recordingTitle?: string | null;
  songMainArtistName?: string | null;
  songMainArtistID?: number | null;
  songContributorMBID?: string | null;
  songContributorName?: string | null;
  songContributorRole?: string | null;
  songContributorInstrument?: string | null;
}

export type NewMusicBrainzSongAttribution = Omit<IMusicBrainzSongAttribution, 'id'> & { id: null };
