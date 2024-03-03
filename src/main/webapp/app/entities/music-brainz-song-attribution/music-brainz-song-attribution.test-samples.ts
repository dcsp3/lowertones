import { IMusicBrainzSongAttribution, NewMusicBrainzSongAttribution } from './music-brainz-song-attribution.model';

export const sampleWithRequiredData: IMusicBrainzSongAttribution = {
  id: 36361,
};

export const sampleWithPartialData: IMusicBrainzSongAttribution = {
  id: 21397,
  recordingTitle: 'Platinum',
  songMainArtistName: 'Maryland cultivate',
  songContributorMBID: 'Security capacitor index',
  songContributorInstrument: 'bypassing',
};

export const sampleWithFullData: IMusicBrainzSongAttribution = {
  id: 63386,
  recordingMBID: 'Berkshire ivory',
  recordingTitle: 'asynchronous program strategic',
  songMainArtistName: 'copying violet',
  songMainArtistID: 22978,
  songContributorMBID: 'Plastic supply-chains',
  songContributorName: 'neural Small Belarussian',
  songContributorRole: 'Fish Aruba',
  songContributorInstrument: 'Health',
};

export const sampleWithNewData: NewMusicBrainzSongAttribution = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
