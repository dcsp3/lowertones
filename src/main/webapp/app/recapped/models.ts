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
  numOneArtistImage: string;

  numTwoArtistName: string;
  numTwoArtistNumSongs: number;
  numTwoArtistImage: string;

  numThreeArtistName: string;
  numThreeArtistNumSongs: number;
  numThreeArtistImage: string;

  numFourArtistName: string;
  numFourArtistNumSongs: number;
  numFourArtistImage: string;

  numFiveArtistName: string;
  numFiveArtistNumSongs: number;
  numFiveArtistImage: string;

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
