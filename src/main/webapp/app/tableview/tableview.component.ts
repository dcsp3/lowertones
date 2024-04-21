import { Component, Injectable, OnInit } from '@angular/core';
import { TreeNode } from 'primeng/api';
import { TableviewTreeService } from '../../../java/team/bham/service/TableviewTreeService';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

interface SongEntry {
  placeholder: boolean;
  title: string;
  artist: string;
  contributor: string;
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
  snapshotId: string;
}

interface PagePair {
  user: number;
  staging: number;
}

@Injectable({
  providedIn: 'root',
})
class PlaylistService {
  private apiUrl = '/api/get-user-playlists';
  constructor(private http: HttpClient) {}
  getPlaylists(): Observable<any> {
    return this.http.get(this.apiUrl);
  }
}

@Injectable({
  providedIn: 'root',
})
class ScrapeService {
  private apiUrl = '/api/scrape';
  constructor(private http: HttpClient) {}
  scrape(): Observable<any> {
    console.log('in class');
    return this.http.get(this.apiUrl);
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
    contributor: '',
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
  };
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
  yearValues: number[] = [1900, 2030];
  rangeValues: number[] = [0, 100];
  durationValues: string[] = ['', ''];
  artistChips: string[] = [];
  producerChips: string[] = [];
  Explicitness: Choice[] = [];
  selectedExplicitness: Choice;
  scanType: Choice[];
  selectedScanType: string = '';
  tableStates: Choice[];
  selectedTableState: Choice;
  pagePair: PagePair = { user: 0, staging: 0 };
  tablePage: number = 0;
  tableRows: number = 15;
  entryCount: number = 0;

  constructor(private playlistService: PlaylistService, private scrapeService: ScrapeService) {
    for (let i = 0; i < 15; i++) {
      let songEntry: SongEntry = this.placeholderSong;
      this.songData[i] = songEntry;
    }

    this.songDataInUse = this.songData;

    this.tableStates = [
      { label: 'Your Playlist', value: 'user' },
      { label: 'Staging Playlist', value: 'staging' },
    ];

    this.selectedTableState = { label: 'Your Playlist', value: 'user' };

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

    this.scanType = [
      { label: 'My Entire Library', value: 'entireLibrary' },
      { label: 'My Top Songs', value: 'topSongs' },
    ];

    this.selectedSongCount = 0;
  }

  ngOnInit(): void {
    this.fetchPlaylists();
    this.genSongList();
    this.filteredSongData = this.songDataInUse;
    this.columns = [
      { value: 'title', label: 'Title', short: 'Title' },
      { value: 'artist', label: 'Artist', short: 'Artist' },
      { value: 'contributor', label: 'Contributor', short: 'Con' },
      { value: 'length', label: 'Length', short: 'Length' },
      { value: 'release', label: 'Release Date', short: 'Release Date' },
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
      { value: 'length', label: 'Length', short: 'Length' },
      { value: 'release', label: 'Release Date', short: 'Release Date' },
      { value: 'popularity', label: 'Popularity', short: 'Pop' },
      { value: 'explicit', label: 'Explicit', short: 'Exp' },
    ];
    /*[
      { value: 'title', label: 'Title', short: 'Title'},
      { value: 'artist', label: 'Artist', short: 'Artist'},
      { value: 'contributor', label: 'Contributor', short: 'Con'},
      { value: 'length', label: 'Length', short: 'Length'},
      { value: 'release', label: 'Release Date', short: 'Release Date'},
      { value: 'popularity', label: 'Popularity', short: 'Pop'},
      { value: 'explicit', label: 'Explicit', short: 'Exp'}
    ]*/
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
    console.log('starting scrape');
    this.scrapeService.scrape();
    console.log('finished scrape');
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

  fetchPlaylists() {
    this.playlistService.getPlaylists().subscribe({
      next: (response: Playlist[]) => {
        const playlistOptions = response.map((playlist: Playlist) => ({
          label: playlist.name,
          value: playlist.spotifyId,
        }));
        this.scanType = [...this.scanType, ...playlistOptions];
      },
      error: error => {
        console.error('Error fetching playlists:', error);
      },
    });
  }

  genSongList(): void {
    const token = sessionStorage.getItem('jhi-authenticationToken')?.slice(1, -1);
    const headers: Headers = new Headers();
    headers.set('Authorization', 'Bearer ' + token);
    const request: RequestInfo = new Request('/api/top-playlist', {
      method: 'GET',
      headers: headers,
    });

    const response = fetch(request);
    const jsonData = response.then(response => response.json());
    jsonData.then(data => {
      this.jsonBlob = data;
      console.log(data);
      this.fillSongTable();
    });
  }

  fillSongTable(): void {
    const numTracks = this.jsonBlob.tracks.length;
    for (let i = 0; i < numTracks; i++) {
      let songEntry: SongEntry = {
        placeholder: true,
        title: 'lorem',
        artist: 'ipsum',
        contributor: 'contributor',
        length: 'sdfss',
        explicit: false,
        popularity: 0,
        release: 'N/A',
        acousticness: 0,
        danceability: 0,
        instrumentalness: 0,
        energy: 0,
        liveness: 0,
        loudness: 0,
        speechiness: 0,
        valence: 0,
        tempo: 0,
      };

      songEntry.placeholder = false;
      songEntry.title = this.jsonBlob.tracks[i].name;
      songEntry.artist = this.jsonBlob.tracks[i].artist.name;
      songEntry.explicit = this.jsonBlob.tracks[i].explicit;
      songEntry.popularity = this.jsonBlob.tracks[i].popularity;
      songEntry.release = this.jsonBlob.tracks[i].album.releaseDate;
      songEntry.acousticness = this.truncate(this.jsonBlob.tracks[i].audioFeatures.acousticness * 100);
      songEntry.danceability = this.truncate(this.jsonBlob.tracks[i].audioFeatures.danceability * 100);
      songEntry.instrumentalness = this.truncate(this.jsonBlob.tracks[i].audioFeatures.instrumentalness * 100);
      songEntry.energy = this.truncate(this.jsonBlob.tracks[i].audioFeatures.energy * 100);
      songEntry.liveness = this.truncate(this.jsonBlob.tracks[i].audioFeatures.liveness * 100);
      songEntry.loudness = this.jsonBlob.tracks[i].audioFeatures.loudness;
      songEntry.speechiness = this.truncate(this.jsonBlob.tracks[i].audioFeatures.speechiness * 100);
      songEntry.valence = this.truncate(this.jsonBlob.tracks[i].audioFeatures.valence * 100);
      songEntry.tempo = this.jsonBlob.tracks[i].audioFeatures.tempo;

      //handle length
      let length = this.jsonBlob.tracks[i].duration;
      const lenFormatted = new Date(length).toISOString().substr(11, 8);
      songEntry.length = lenFormatted;

      this.songData[i] = songEntry;
    }

    this.songData = this.songData.slice(0, numTracks);
    this.applySearch();
    this.loadingSongs = false;
  }
}
