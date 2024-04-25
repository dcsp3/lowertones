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
  private tableviewTreeService: TableviewTreeService = new TableviewTreeService();
  yearValues: number[] = new Array(2).fill(undefined);
  popularityValues: number[] = new Array(2).fill(undefined);
  tempoValues: number[] = new Array(2).fill(undefined);
  durationValues: string[] = ['', ''];
  artistChips: string[] = [];
  contributorChips: string[] = [];
  Explicitness: Choice[] = [];
  selectedExplicitness: Choice;
  playlists: any[];
  selectedPlaylist!: any;
  tableStates: Choice[];
  selectedTableState: Choice;
  pagePair: PagePair = { user: 0, staging: 0 };
  tablePage: number = 0;
  tableRows: number = 15;
  entryCount: number = 0;

  constructor(private tableviewService: TableviewService, private scrapeService: ScrapeService) {
    for (let i = 0; i < 15; i++) {
      let songEntry: SongEntry = this.placeholderSong;
      this.songData[i] = songEntry;
    }

    for (let i = 0; i < 15; i++) {
      this.placeholderList.push({ ...this.placeholderSong });
    }

    this.songDataInUse = this.songData;

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
      { label: 'Both', value: 'Both' },
      { label: 'Explicit', value: 'Yes' },
      { label: 'Non-Explicit', value: 'No' },
    ];

    this.selectedExplicitness = { label: 'Both', value: 'Both' };

    this.selectedSearchType = { label: 'Titles & Artists', value: 'both' };

    this.playlists = [
      { name: 'My Entire Library', spotifyId: 'entireLibrary', image: 'null' },
      { name: 'My Top Songs', spotifyId: 'topSongs', image: 'null' },
    ];

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
    ]; /*
      { value: 'acousticness', label: 'Acousticness', short: 'Aco' },
      { value: 'danceability', label: 'Danceability', short: 'Dan' },
      { value: 'instrumentalness', label: 'Instrumentalness', short: 'Ins' },
      { value: 'energy', label: 'Energy', short: 'Ene' },
      { value: 'liveness', label: 'Liveness', short: 'Liv' },
      { value: 'loudness', label: 'Loudness', short: 'Lou' },
      { value: 'speechiness', label: 'Speechiness', short: 'Spe' },
      { value: 'valence', label: 'Valence', short: 'Val' },
      { value: 'tempo', label: 'Tempo', short: 'Tem' },
    ];*/
    this.tableviewTreeService.getTreeNodes().then(data => (this.filters = data));

    const defaultSearchType = this.searchTypes.find(searchType => searchType.label === 'Titles & Artists');
    if (defaultSearchType) {
      this.selectedSearchType = defaultSearchType;
    }
  }

  applySearch(): void {
    this.filteredSongData = this.songDataInUse.filter(song => {
      const query = this.searchQuery.toLowerCase();
      const matchesTitle = song.title.toLowerCase().includes(query);
      const matchesArtist = song.artist.toLowerCase().includes(query);

      if (this.selectedSearchType.value === 'both') return matchesTitle || matchesArtist;
      else if (this.selectedSearchType.value === 'title') return matchesTitle;
      else return matchesArtist;
    });
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
        this.playlists = data.map(playlist => {
          const image = playlist.imgLarge || playlist.imgMedium || playlist.imgSmall || '';
          return {
            name: playlist.name,
            spotifyId: playlist.id,
            image: image,
          };
        });

        // Check if there are any playlists and set the first one as selected
        if (this.playlists.length > 0) {
          this.selectedPlaylist = this.playlists[0];
          this.getPlaylistData();
        }
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

  appendExtraToArtist(songEntries: SongEntry[]): void {
    const spotifyIdMap = new Map<string, number>();

    // First pass: count occurrences of each spotifyID
    songEntries.forEach(song => {
      const count = spotifyIdMap.get(song.spotifyId) ?? 0;
      spotifyIdMap.set(song.spotifyId, count + 1);
    });

    // Second pass: append "extra" to artist name if spotifyID occurs more than once
    songEntries.forEach(song => {
      if (spotifyIdMap.get(song.spotifyId)! > 1) {
        song.artist += ' extra';
      }
    });
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

  capitalizeWords(inputArray: string[]): string[] {
    // Check if the input array is empty
    if (inputArray.length === 0) {
      return [];
    }

    return inputArray.map(text => {
      return text
        .split(' ')
        .map(word => {
          // Make sure we are not trying to capitalize an empty string
          if (word === '') return word;
          return word.charAt(0).toUpperCase() + word.slice(1).toLowerCase();
        })
        .join(' ');
    });
  }

  genSongList(): void {
    this.loadingSongs = true;
    this.setPage(0);
    if (this.selectedTableState.value === 'user') {
      this.songDataInUse = this.placeholderList;
      this.applySearch();
    }
    console.log('the ID being used: ' + this.selectedPlaylist.spotifyId);

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

        this.printContributorDetails();

        console.log('here is how long the list is' + this.songData.length);

        this.songData = this.consolidateArtists(this.songData);

        console.log('and now: presto! ' + this.songData.length);

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
