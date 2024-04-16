export enum MusicianType {
  PRODUCER = 'PRODUCER',
  MIXING_ENGINEER = 'MIXING_ENGINEER',
  GUITARIST = 'GUITARIST',
  DRUMMER = 'DRUMMER',
  BASSIST = 'BASSIST',
  VOCALIST = 'VOCALIST',
}

export interface RecappedDTO {
  numOneArtistName: string;
  numOneAristNumSongs: number;
  numTwoArtistName: string;
  numTwoArtistNumSongs: number;
  numThreeArtistName: string;
  numThreeArtistNumSongs: number;
  numFourArtistName: string;
  numFourArtistNumSongs: number;
  numFiveArtistName: string;
  numFiveArtistNumSongs: number;
  numOneHeroImg: string;
  numOneFirstCoverImg: string;
  numOneFirstSongTitle: string;
  numOneFirstSongMainArtist: string;
  numOneSecondCoverImg: string;
  numOneSecondSongTitle: string;
  numOneSecondSongMainArtist: string;
  totalSongs: number;
  totalDuration: number;
  totalArtists: number;
  totalContributors: number;
  topUnder1kName: string;
  topUnder10kName: string;
  topUnder100kName: string;
  topUnder1kImage: string;
  topUnder10kImage: string;
  topUnder100kImage: string;
}

export interface RecappedRequest {
  dateRange: string;
  musicianType: MusicianType;
  scanEntireLibrary: boolean;
  scanTopSongs: boolean;
  scanSpecificPlaylist: boolean;
  playlistId?: string;
}

export enum DateRange {
  LAST_MONTH = 'LAST_MONTH',
  LAST_6_MONTHS = 'LAST_6_MONTHS',
  LAST_FEW_YEARS = 'LAST_FEW_YEARS',
}

export interface choice {
  label: string;
  value: string;
}
