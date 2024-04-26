import { Component, Injectable, OnInit } from '@angular/core';
import { TreeNode } from 'primeng/api';
import { TableviewTreeService } from '../../../java/team/bham/service/TableviewTreeService';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TableviewService } from './tableview.service';

interface SongEntry {
  placeholder: boolean;
  title: string;
  artist: string;
  contributor: boolean;
  length: string;
  explicit: boolean;
  popularity: number;
  release: string;
  acousticness: number;
  danceability: number;
  instrumentalness: number;
  energy: number;
  liveness: number;
  loudness: number;
  speechiness: number;
  valence: number;
  tempo: number;
  spotifyId: string;
  contributorNames: string[];
  contributorRoles: string[];
  contributorInstruments: string[];
}

interface QueryParams {
  searchQuery: string;
  minDuration: number | null;
  maxDuration: number | null;
  selectedExplicitness: string;
  minPopularity: number | null;
  maxPopularity: number | null;
  artistChips: string[];
  contributorChips: string[];
  minAcousticness: number | null | undefined;
  maxAcousticness: number | null | undefined;
  minDanceability: number | null | undefined;
  maxDanceability: number | null | undefined;
  minInstrumentalness: number | null | undefined;
  maxInstrumentalness: number | null | undefined;
  minEnergy: number | null | undefined;
  maxEnergy: number | null | undefined;
  minLiveness: number | null | undefined;
  maxLiveness: number | null | undefined;
  minLoudness: number | null | undefined;
  maxLoudness: number | null | undefined;
  minSpeechiness: number | null | undefined;
  maxSpeechiness: number | null | undefined;
  minValence: number | null | undefined;
  maxValence: number | null | undefined;
}

type Feature = {
  name: string;
  range: [number | null, number | null];
};

interface Column {
  value: string;
  label: string;
  short: string;
}

interface Choice {
  label: string;
  value: string;
}

interface Playlist {
  name: string;
  spotifyId: string;
  image: string;
}

interface PagePair {
  user: number;
  staging: number;
}

@Injectable({
  providedIn: 'root',
})
class ScrapeService {
  private apiUrl = '/api/scrape';
  constructor(private http: HttpClient) {}
  scrape(): Observable<any> {
    console.log('in class');
    return this.http.post<boolean>(this.apiUrl, {});
  }
}

@Component({
  selector: 'jhi-tableview',
  templateUrl: './tableview.component.html',
  styleUrls: ['./tableview.component.scss'],
})
export class TableviewComponent implements OnInit {
  loadingSongs: boolean = true;
  songData: SongEntry[] = [];
  placeholderSong: SongEntry = {
    placeholder: true,
    title: '',
    artist: '',
    contributor: false,
    length: '',
    explicit: false,
    popularity: 0,
    release: '',
    acousticness: 0,
    danceability: 0,
    instrumentalness: 0,
    energy: 0,
    liveness: 0,
    loudness: 0,
    speechiness: 0,
    valence: 0,
    tempo: 0,
    spotifyId: '',
    contributorNames: [],
    contributorRoles: [],
    contributorInstruments: [],
  };
  placeholderList: SongEntry[] = [];
  songDataInUse: SongEntry[] = [];
  filteredSongData: SongEntry[] = [];
  selectedSongs: SongEntry[] = [];
  selectedSongCount: number;
  searchQuery: string = '';
  jsonBlob: any;
  filters!: TreeNode[];
  searchTypes: Choice[];
  selectedSearchType: Choice;
  columns!: Column[];
  selectedColumns!: Column[];
  queryParams: QueryParams;
  private tableviewTreeService: TableviewTreeService = new TableviewTreeService();

  features: Feature[] = [
    { name: 'Acousticness', range: [null, null] },
    { name: 'Danceability', range: [null, null] },
    { name: 'Instrumentalness', range: [null, null] },
    { name: 'Energy', range: [null, null] },
    { name: 'Liveness', range: [null, null] },
    { name: 'Loudness', range: [null, null] },
    { name: 'Speechiness', range: [null, null] },
    { name: 'Valence', range: [null, null] },
  ];

  yearRange: number[] = new Array(2).fill(undefined);
  popularityRange: number[] = new Array(2).fill(undefined);
  tempoRange: number[] = new Array(2).fill(undefined);
  durationRange: string[] = ['', ''];
  artistChips: string[] = [];
  contributorChips: string[] = [];
  Explicitness: Choice[] = [];
  selectedExplicitness: Choice;
  playlists: any[];
  importedPlaylists: any[];
  selectedPlaylist!: any;
  tableStates: Choice[];
  selectedTableState: Choice;
  pagePair: PagePair = { user: 0, staging: 0 };
  tablePage: number = 0;
  tableRows: number = 15;
  entryCount: number = 0;

  exportPopupVisible: boolean = false;
  exportName: string = '';
  exportNameError: boolean = false;
  exportItemsError: boolean = false;

  constructor(private tableviewService: TableviewService, private scrapeService: ScrapeService) {
    for (let i = 0; i < 15; i++) {
      let songEntry: SongEntry = this.placeholderSong;
      this.songData[i] = songEntry;
    }

    for (let i = 0; i < 15; i++) {
      this.placeholderList.push({ ...this.placeholderSong });
    }

    this.songDataInUse = this.songData;

    this.queryParams = this.initializeQueryParams();

    this.tableStates = [
      { label: 'Working Playlist', value: 'user' },
      { label: 'Staging Playlist', value: 'staging' },
    ];

    this.selectedTableState = { label: 'Working Playlist', value: 'user' };

    this.searchTypes = [
      { label: 'Titles & Artists', value: 'both' },
      { label: 'Titles', value: 'title' },
      { label: 'Artists', value: 'artist' },
    ];

    this.Explicitness = [
      { label: 'Both', value: 'both' },
      { label: 'Explicit', value: 'yes' },
      { label: 'Non-Explicit', value: 'no' },
    ];

    this.selectedExplicitness = { label: 'Both', value: 'both' };

    this.selectedSearchType = { label: 'Titles & Artists', value: 'both' };

    this.playlists = [
      { name: 'Lowertones Library', spotifyId: 'lowertonesLibrary', image: '../../content/images/lowertones_small.png' },
      { name: 'My Entire Library', spotifyId: 'entireLibrary', image: '../../content/images/library.png' },
    ];

    this.importedPlaylists = [{}];

    this.selectedPlaylist = { name: 'My Top Songs', spotifyId: 'topSongs', image: 'null' };
    this.selectedSongCount = 0;
  }

  ngOnInit(): void {
    this.scrape();
    this.fetchPlaylists();

    this.filteredSongData = this.songDataInUse;
    this.columns = [
      { value: 'title', label: 'Title', short: 'Title' },
      { value: 'artist', label: 'Artist', short: 'Artist' },
      { value: 'contributor', label: 'Contributor', short: 'Con' },
      { value: 'length', label: 'Length', short: 'Len' },
      { value: 'release', label: 'Release Date', short: 'Rel' },
      { value: 'popularity', label: 'Popularity', short: 'Pop' },
      { value: 'explicit', label: 'Explicit', short: 'Exp' },
      { value: 'acousticness', label: 'Acousticness', short: 'Aco' },
      { value: 'danceability', label: 'Danceability', short: 'Dan' },
      { value: 'instrumentalness', label: 'Instrumentalness', short: 'Ins' },
      { value: 'energy', label: 'Energy', short: 'Ene' },
      { value: 'liveness', label: 'Liveness', short: 'Liv' },
      { value: 'loudness', label: 'Loudness', short: 'Lou' },
      { value: 'speechiness', label: 'Speechiness', short: 'Spe' },
      { value: 'valence', label: 'Valence', short: 'Val' },
      { value: 'tempo', label: 'Tempo', short: 'Tem' },
    ];

    this.selectedColumns = [
      { value: 'title', label: 'Title', short: 'Title' },
      { value: 'artist', label: 'Artist', short: 'Artist' },
      { value: 'contributor', label: 'Contributor', short: 'Con' },
      { value: 'length', label: 'Length', short: 'Len' },
      { value: 'release', label: 'Release Date', short: 'Rel' },
      { value: 'popularity', label: 'Popularity', short: 'Pop' },
      { value: 'explicit', label: 'Explicit', short: 'Exp' },
      { value: 'acousticness', label: 'Acousticness', short: 'Aco' },
      { value: 'danceability', label: 'Danceability', short: 'Dan' },
      { value: 'instrumentalness', label: 'Instrumentalness', short: 'Ins' },
      { value: 'energy', label: 'Energy', short: 'Ene' },
      { value: 'liveness', label: 'Liveness', short: 'Liv' },
      { value: 'loudness', label: 'Loudness', short: 'Lou' },
      { value: 'speechiness', label: 'Speechiness', short: 'Spe' },
      { value: 'valence', label: 'Valence', short: 'Val' },
      { value: 'tempo', label: 'Tempo', short: 'Tem' },
    ];
    this.tableviewTreeService.getTreeNodes().then(data => (this.filters = data));

    const defaultSearchType = this.searchTypes.find(searchType => searchType.label === 'Titles & Artists');
    if (defaultSearchType) {
      this.selectedSearchType = defaultSearchType;
    }
  }

  searchOrQuery(): void {
    if (this.selectedPlaylist.spotifyId === 'lowertonesLibrary') {
      this.genSongList();
    } else {
      this.applySearch();
    }
  }

  applySearch(): void {
    this.filteredSongData = this.songDataInUse.filter(song => {
      return (
        this.matchesSearchQuery(song) &&
        this.filterByExplicitness(song) &&
        this.filterByArtistChips(song) &&
        this.filterByContributorChips(song) &&
        this.filterByDuration(song) &&
        this.filterByReleaseYear(song) &&
        this.filterByPopularity(song) &&
        this.filterByTempo(song) &&
        this.filterByAcousticness(song) &&
        this.filterByDanceability(song) &&
        this.filterByInstrumentalness(song) &&
        this.filterByEnergy(song) &&
        this.filterByLiveness(song) &&
        this.filterByLoudness(song) &&
        this.filterBySpeechiness(song) &&
        this.filterByValence(song)
      );
    });

    this.fixPages();
    this.filteredSongData = this.removePlaceholders(this.filteredSongData);
    this.songListFactor15(this.filteredSongData);
  }

  private matchesSearchQuery(song: SongEntry): boolean {
    const query = this.searchQuery.toLowerCase();
    const matchesTitle = song.title.toLowerCase().includes(query);
    const matchesArtist = song.artist.toLowerCase().includes(query);

    if (this.selectedSearchType.value === 'both') return matchesTitle || matchesArtist;
    else if (this.selectedSearchType.value === 'title') return matchesTitle;
    else return matchesArtist;
  }

  private filterByDuration(song: SongEntry): boolean {
    const songDurationSeconds = this.convertToSeconds(song.length);
    const [minTime, maxTime] = this.durationRange;
    const minTimeSeconds = minTime.includes('_') ? null : this.convertToSeconds(minTime);
    const maxTimeSeconds = maxTime.includes('_') ? null : this.convertToSeconds(maxTime);
    if (minTimeSeconds !== null && songDurationSeconds < minTimeSeconds) return false;
    if (maxTimeSeconds !== null && songDurationSeconds > maxTimeSeconds) return false;
    return true;
  }

  private filterByReleaseYear(song: SongEntry): boolean {
    const songReleaseYear = parseInt(song.release.split('-')[0]);
    const [minYear, maxYear] = this.yearRange;

    if (minYear !== null && minYear !== undefined && songReleaseYear < minYear) return false;
    if (maxYear !== null && maxYear !== undefined && songReleaseYear > maxYear) return false;
    return true;
  }

  private filterByPopularity(song: SongEntry): boolean {
    const [minPopularity, maxPopularity] = this.popularityRange;
    if (minPopularity !== null && song.popularity < minPopularity) return false;
    if (maxPopularity !== null && song.popularity > maxPopularity) return false;
    return true;
  }

  private filterByTempo(song: SongEntry): boolean {
    const [minTempo, maxTempo] = this.tempoRange;
    if (minTempo !== null && song.tempo < minTempo) return false;
    if (maxTempo !== null && song.tempo > maxTempo) return false;
    return true;
  }

  private convertToSeconds(timeStr: string): number {
    const [mins, secs] = timeStr.split(':').map(Number);
    return mins * 60 + secs;
  }

  private filterByFeature(song: SongEntry, featureName: string): boolean {
    const feature = this.features.find(f => f.name === featureName);
    if (!feature) {
      console.error(`Feature not found: ${featureName}`);
      return true;
    }

    const [minValue, maxValue] = feature.range;
    const featureValue = song[featureName.toLowerCase() as keyof SongEntry] as number;
    if (minValue !== null && featureValue < minValue) return false;
    if (maxValue !== null && featureValue > maxValue) return false;
    return true;
  }

  private filterByAcousticness(song: SongEntry): boolean {
    return this.filterByFeature(song, 'Acousticness');
  }

  private filterByDanceability(song: SongEntry): boolean {
    return this.filterByFeature(song, 'Danceability');
  }

  private filterByInstrumentalness(song: SongEntry): boolean {
    return this.filterByFeature(song, 'Instrumentalness');
  }

  private filterByEnergy(song: SongEntry): boolean {
    return this.filterByFeature(song, 'Energy');
  }

  private filterByLiveness(song: SongEntry): boolean {
    return this.filterByFeature(song, 'Liveness');
  }

  private filterByLoudness(song: SongEntry): boolean {
    return this.filterByFeature(song, 'Loudness');
  }

  private filterBySpeechiness(song: SongEntry): boolean {
    return this.filterByFeature(song, 'Speechiness');
  }

  private filterByValence(song: SongEntry): boolean {
    return this.filterByFeature(song, 'Valence');
  }

  private filterByArtistChips(song: SongEntry): boolean {
    if (this.artistChips.length === 0) {
      return true;
    }
    const lowerCaseArtist = song.artist.toLowerCase();
    const lowerCaseChips = this.artistChips.map(chip => chip.toLowerCase());
    return lowerCaseChips.includes(lowerCaseArtist);
  }

  private filterByContributorChips(song: SongEntry): boolean {
    if (this.contributorChips.length === 0) {
      return true;
    }
    const lowerCaseContributorChips = this.contributorChips.map(chip => chip.toLowerCase());

    return song.contributorNames.some(contributor => lowerCaseContributorChips.includes(contributor.toLowerCase()));
  }

  private filterByExplicitness(song: SongEntry): boolean {
    switch (this.selectedExplicitness.value) {
      case 'both':
        return true;
      case 'yes':
        return song.explicit === true;
      case 'no':
        return song.explicit === false;
      default:
        return true;
    }
  }

  onColumnHeaderClick(): void {
    this.filteredSongData = this.removePlaceholders(this.filteredSongData);
    this.songListFactor15(this.filteredSongData);
  }

  switchSongDataInUse(): void {
    const key = this.selectedTableState.value as keyof PagePair;
    const otherKey = key === 'user' ? 'staging' : ('user' as keyof PagePair);

    this.songDataInUse = key === 'user' ? this.songData : this.selectedSongs;
    this.applySearch();
    let maxPages = this.countSongsNoPlaceholder(this.filteredSongData);
    this.pagePair[otherKey] = this.tablePage;

    if (maxPages < this.pagePair[key]) {
      console.log(
        "setting value from what could've been " + maxPages.toString() + ' to ' + (Math.floor(maxPages / 15) * 15).toString() + ':1'
      );
      this.setPage(Math.floor(maxPages / 15) * 15);
    } else {
      console.log('setting value to ' + this.pagePair[key].toString() + ':3');
      this.setPage(this.pagePair[key]);
    }
  }

  fixPages(): void {
    const key = this.selectedTableState.value as keyof PagePair;
    let maxPages = this.countSongsNoPlaceholder(this.filteredSongData);

    if (maxPages < this.tablePage) {
      console.log(
        "setting value from what could've been " + maxPages.toString() + ' to ' + (Math.floor(maxPages / 15) * 15).toString() + ':1'
      );
      this.setPage(Math.floor(maxPages / 15) * 15);
    }
  }

  setPage(pageNum: number): void {
    this.tablePage = pageNum;
  }

  pageChange(event: { first: number; rows: number }) {
    this.tablePage = event.first;
    this.tableRows = event.rows;
    this.entryCount = this.countSongsNoPlaceholder(this.filteredSongData);
  }

  scrape() {
    this.scrapeService.scrape().subscribe((response: boolean) => {
      console.log('Scrape in progress');
    });
  }

  truncate(num: number): number {
    return Math.floor(num * 100) / 100;
  }

  sortColumns(): void {
    const orderMap = new Map(this.columns.map((col, index) => [col.value, index]));
    this.selectedColumns.sort((a, b) => {
      return (orderMap.get(a.value) || 0) - (orderMap.get(b.value) || 0);
    });
  }

  songListFactor15(songList: SongEntry[]): void {
    const mod = songList.length % 15;
    if (mod !== 0 || songList.length === 0) {
      const itemsToAdd = 15 - mod;
      for (let i = 0; i < itemsToAdd; i++) {
        let songEntry: SongEntry = this.placeholderSong;
        songList.push(songEntry);
      }
    }
  }

  removePlaceholders(songList: SongEntry[]): SongEntry[] {
    return songList.filter(song => !song.placeholder);
  }

  selectionLogic(): void {
    this.deselectPlaceholders();
    this.songListFactor15(this.selectedSongs);
    this.countSelectedSongsNoPlaceholder();
  }

  deselectPlaceholders(): void {
    this.selectedSongs = this.selectedSongs.filter(song => !song.placeholder);
  }

  countSelectedSongs(): number {
    return (this.selectedSongCount = this.selectedSongs.length);
  }

  countSelectedSongsNoPlaceholder(): number {
    return (this.selectedSongCount = this.selectedSongs.filter(song => !song.placeholder).length);
  }

  countSongsNoPlaceholder(songList: SongEntry[]): number {
    return songList.filter(song => !song.placeholder).length;
  }

  getNumberOfLastEntry(): number {
    let lastEntry = this.tablePage + 15;
    if (lastEntry > this.countSongsNoPlaceholder(this.filteredSongData)) {
      lastEntry = this.countSongsNoPlaceholder(this.filteredSongData);
    }
    return lastEntry;
  }

  getPlaylistData(): void {
    console.log(this.selectedPlaylist);
    this.genSongList();
  }

  fetchPlaylists(): void {
    this.tableviewService.getPlaylists().subscribe({
      next: (data: any[]) => {
        this.importedPlaylists = data.map(playlist => {
          const image = playlist.imgLarge || playlist.imgMedium || playlist.imgSmall || '';
          return {
            name: playlist.name,
            spotifyId: playlist.id,
            image: image,
          };
        });

        // Check if there are any playlists and set the first one as selected
        if (this.importedPlaylists.length > 0) {
          this.selectedPlaylist = this.importedPlaylists[0];
          this.getPlaylistData();
        }

        this.playlists.push(...this.importedPlaylists);
      },
      error: error => {
        console.error('There was an error fetching the playlists', error);
      },
    });
  }

  formatTime(ms: number): string {
    const totalSeconds = Math.floor(ms / 1000);
    const minutes = Math.floor(totalSeconds / 60);
    const seconds = totalSeconds % 60;
    return `${minutes}:${String(seconds).padStart(2, '0')}`;
  }

  timeToMilliseconds(time: string): number | null {
    const parts = time.split(':');
    if (parts.length !== 2) {
      return null;
    }

    const minutes = parseInt(parts[0]);
    const seconds = parseInt(parts[1]);

    if (isNaN(minutes) || isNaN(seconds) || minutes < 0 || seconds < 0 || seconds >= 60) {
      return null;
    }

    return (minutes * 60 + seconds) * 1000;
  }

  consolidateArtists(songEntries: SongEntry[]): SongEntry[] {
    const spotifyIdMap = new Map<string, SongEntry>();

    for (const song of songEntries) {
      if (spotifyIdMap.has(song.spotifyId)) {
        const firstInstance = spotifyIdMap.get(song.spotifyId)!;
        firstInstance.artist += `, ${song.artist}`;
      } else {
        spotifyIdMap.set(song.spotifyId, song);
      }
    }
    return Array.from(spotifyIdMap.values());
  }

  consolidateContributors(songEntries: SongEntry[]): SongEntry[] {
    return songEntries.map(song => {
      const { contributorNames, contributorRoles, contributorInstruments } = song;

      let uniqueContributorNames: string[] = [];
      let uniqueContributorRoles: string[] = [];
      let uniqueContributorInstruments: string[] = [];

      const seenTrios = new Set<string>();

      contributorNames.forEach((name, index) => {
        const role = contributorRoles[index];
        const instrument = contributorInstruments[index];
        const trioKey = `${name}-${role}-${instrument}`;

        if (!seenTrios.has(trioKey)) {
          seenTrios.add(trioKey);
          uniqueContributorNames.push(name);
          uniqueContributorRoles.push(role);
          uniqueContributorInstruments.push(instrument);
        }
      });
      return {
        ...song,
        contributorNames: uniqueContributorNames,
        contributorRoles: uniqueContributorRoles,
        contributorInstruments: uniqueContributorInstruments,
      };
    });
  }

  printContributorDetails(): void {
    this.songData.forEach((song, index) => {
      //console.log(`Song ${index + 1}:`);
      if (song.contributorNames && song.contributorNames.length > 0) {
        console.log(song.title);
        console.log('Contributor Names:', song.contributorNames.join(', '));
      } else {
      }

      if (song.contributorRoles && song.contributorRoles.length > 0) {
        console.log('Contributor Roles:', song.contributorRoles.join(', '));
      } else {
      }

      if (song.contributorInstruments && song.contributorInstruments.length > 0) {
        console.log('Contributor Instruments:', song.contributorInstruments.join(', '));
      } else {
      }
      console.log();
    });
  }

  printContributorDetails2(songs: SongEntry[]): void {
    songs.forEach(song => {
      console.log(`Details for Song: ${song.title}`);
      song.contributorNames.forEach((contributor, index) => {
        const role = song.contributorRoles[index] || '[blank]';
        const instrument = song.contributorInstruments[index] || '[blank]';
        console.log(`${contributor} - ${role} - ${instrument}`);
      });
    });
  }

  capitalizeWords(inputArray: string[]): string[] {
    if (inputArray.length === 0) {
      return [];
    }

    return inputArray.map(text => {
      return text
        .split(' ')
        .map(word => {
          if (word === '') return word;
          return word.charAt(0).toUpperCase() + word.slice(1).toLowerCase();
        })
        .join(' ');
    });
  }

  toLowerCaseArray(strings: string[]): string[] {
    return strings.map(s => s.toLowerCase());
  }

  fetchFiltersData(): void {
    this.queryParams = {
      searchQuery: this.searchQuery,
      minDuration: this.timeToMilliseconds(this.durationRange[0]),
      maxDuration: this.timeToMilliseconds(this.durationRange[1]),
      selectedExplicitness: this.selectedExplicitness.value,
      minPopularity: this.popularityRange[0],
      maxPopularity: this.popularityRange[1],
      artistChips: this.artistChips,
      contributorChips: this.contributorChips,
      minAcousticness: this.features.find(f => f.name === 'Acousticness')?.range[0],
      maxAcousticness: this.features.find(f => f.name === 'Acousticness')?.range[1],
      minDanceability: this.features.find(f => f.name === 'Danceability')?.range[0],
      maxDanceability: this.features.find(f => f.name === 'Danceability')?.range[1],
      minInstrumentalness: this.features.find(f => f.name === 'Instrumentalness')?.range[0],
      maxInstrumentalness: this.features.find(f => f.name === 'Instrumentalness')?.range[1],
      minEnergy: this.features.find(f => f.name === 'Energy')?.range[0],
      maxEnergy: this.features.find(f => f.name === 'Energy')?.range[1],
      minLiveness: this.features.find(f => f.name === 'Liveness')?.range[0],
      maxLiveness: this.features.find(f => f.name === 'Liveness')?.range[1],
      minLoudness: this.features.find(f => f.name === 'Loudness')?.range[0],
      maxLoudness: this.features.find(f => f.name === 'Loudness')?.range[1],
      minSpeechiness: this.features.find(f => f.name === 'Speechiness')?.range[0],
      maxSpeechiness: this.features.find(f => f.name === 'Speechiness')?.range[1],
      minValence: this.features.find(f => f.name === 'Valence')?.range[0],
      maxValence: this.features.find(f => f.name === 'Valence')?.range[1],
    };
  }

  private initializeQueryParams(): QueryParams {
    return {
      searchQuery: '',
      minDuration: null,
      maxDuration: null,
      selectedExplicitness: 'both',
      minPopularity: null,
      maxPopularity: null,
      artistChips: [],
      contributorChips: [],
      minAcousticness: null,
      maxAcousticness: null,
      minDanceability: null,
      maxDanceability: null,
      minInstrumentalness: null,
      maxInstrumentalness: null,
      minEnergy: null,
      maxEnergy: null,
      minLiveness: null,
      maxLiveness: null,
      minLoudness: null,
      maxLoudness: null,
      minSpeechiness: null,
      maxSpeechiness: null,
      minValence: null,
      maxValence: null,
    };
  }

  genSongList(): void {
    this.loadingSongs = true;
    this.setPage(0);
    if (this.selectedTableState.value === 'user') {
      this.songDataInUse = this.placeholderList;
      this.applySearch();
    }
    console.log('the ID being used: ' + this.selectedPlaylist.spotifyId);

    this.fetchFiltersData();

    if (this.selectedPlaylist.spotifyId === 'lowertonesLibrary') {
      this.tableviewService.getLowertonesData(this.queryParams).subscribe({
        next: (data: any[]) => {
          this.songData = data.map(songEntry => ({
            placeholder: false,
            title: songEntry.title,
            artist: songEntry.artist,
            contributor: songEntry.contributor,
            length: this.formatTime(songEntry.length),
            explicit: songEntry.explicit,
            popularity: songEntry.popularity,
            release: songEntry.release,
            acousticness: this.truncate(songEntry.acousticness * 100),
            danceability: this.truncate(songEntry.danceability * 100),
            instrumentalness: this.truncate(songEntry.instrumentalness * 100),
            energy: this.truncate(songEntry.energy * 100),
            liveness: this.truncate(songEntry.liveness * 100),
            loudness: songEntry.loudness,
            speechiness: this.truncate(songEntry.speechiness * 100),
            valence: this.truncate(songEntry.valence * 100),
            tempo: Math.round(songEntry.tempo * 2) / 2,
            spotifyId: songEntry.spotifyId,
            contributorNames: this.capitalizeWords(songEntry.contributorNames),
            contributorRoles: this.capitalizeWords(songEntry.contributorRoles),
            contributorInstruments: this.capitalizeWords(songEntry.contributorInstruments),
          }));

          this.songData = this.consolidateArtists(this.songData);

          this.songData = this.consolidateContributors(this.songData);

          if (this.selectedTableState.value === 'user') {
            this.songDataInUse = this.songData;
          }

          this.applySearch();
          this.loadingSongs = false;
        },
        error: error => {
          console.error('There was an error fetching the playlists', error);
          this.loadingSongs = false;
        },
      });
    } else {
      this.tableviewService.getPlaylistData(this.selectedPlaylist.spotifyId).subscribe({
        next: (data: any[]) => {
          this.songData = data.map(songEntry => ({
            placeholder: false,
            title: songEntry.title,
            artist: songEntry.artist,
            contributor: songEntry.contributor,
            length: this.formatTime(songEntry.length),
            explicit: songEntry.explicit,
            popularity: songEntry.popularity,
            release: songEntry.release,
            acousticness: this.truncate(songEntry.acousticness * 100),
            danceability: this.truncate(songEntry.danceability * 100),
            instrumentalness: this.truncate(songEntry.instrumentalness * 100),
            energy: this.truncate(songEntry.energy * 100),
            liveness: this.truncate(songEntry.liveness * 100),
            loudness: songEntry.loudness,
            speechiness: this.truncate(songEntry.speechiness * 100),
            valence: this.truncate(songEntry.valence * 100),
            tempo: Math.round(songEntry.tempo * 2) / 2,
            spotifyId: songEntry.spotifyId,
            contributorNames: this.capitalizeWords(songEntry.contributorNames),
            contributorRoles: this.capitalizeWords(songEntry.contributorRoles),
            contributorInstruments: this.capitalizeWords(songEntry.contributorInstruments),
          }));

          this.songData = this.consolidateArtists(this.songData);

          this.songData = this.consolidateContributors(this.songData);

          if (this.selectedTableState.value === 'user') {
            this.songDataInUse = this.songData;
          }

          this.applySearch();
          this.loadingSongs = false;
        },
        error: error => {
          console.error('There was an error fetching the playlists', error);
          this.loadingSongs = false;
        },
      });
    }
  }

  showExportPopup() {
    this.exportName = '';
    this.exportPopupVisible = true;
  }

  exportStagingPlaylist() {
    console.log('playlist name: ' + this.exportName);
    if (this.exportName.length === 0) {
      this.exportNameError = true;
    } else {
      if (this.selectedSongs.length === 0) {
        this.exportItemsError = true;
        return;
      }
      //conv from song entries to spotify ids
      const spotifyIds: string[] = this.selectedSongs.map(song => song.spotifyId);
      //then get rid of empty strs (why do these appear?)
      const filteredSpotifyIds: string[] = spotifyIds.filter(spotifyId => spotifyId !== '');
      console.log(filteredSpotifyIds);

      this.tableviewService
        .exportPlaylist({
          name: this.exportName,
          songSpotifyIds: filteredSpotifyIds,
        })
        .subscribe({
          next: (response: any) => {
            console.log('Playlist exported!!');
            this.exportNameError = false;
            this.exportItemsError = false;
            this.exportPopupVisible = false;
          },
        });
    }
  }

  resetExportNameError() {
    this.exportNameError = false;
    this.exportItemsError = false;
  }
}
