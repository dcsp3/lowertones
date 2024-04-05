import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms'; // Import FormsModule
import { DropdownModule } from 'primeng/dropdown'; // Import DropdownModule
import { ButtonModule } from 'primeng/button'; // Import ButtonModule
import { TreeNode } from 'primeng/api';

interface searchType {
  label: string;
  value: string;
}

@Component({
  selector: 'jhi-tableview',
  templateUrl: './tableview.component.html',
  styleUrls: ['./tableview.component.scss'],
})
export class TableviewComponent implements OnInit {
  songData: SongEntry[];
  filteredSongData: SongEntry[] = [];
  searchQuery: string = '';
  jsonBlob: any;
  files!: TreeNode[];
  selectedFiles!: TreeNode[];
  searchTypes: searchType[];
  selectedSearchType: searchType;

  constructor() {
    this.songData = new Array(100);
    this.searchTypes = [
      { label: 'Titles & Artists', value: 'both' },
      { label: 'Titles', value: 'title' },
      { label: 'Artists', value: 'artist' },
    ];
    this.selectedSearchType = { label: 'Titles & Artists', value: 'both' };

    // for primeNG dropdown menu
  }

  ngOnInit(): void {
    this.genSongList();
    this.filteredSongData = this.songData;

    const defaultSearchType = this.searchTypes.find(searchType => searchType.label === 'Titles & Artists');
    if (defaultSearchType) {
      this.selectedSearchType = defaultSearchType;
    }
  }

  applySearch(): void {
    this.filteredSongData = this.songData.filter(song => {
      const query = this.searchQuery.toLowerCase();
      const matchesTitle = song.title.toLowerCase().includes(query);
      const matchesArtist = song.artist.toLowerCase().includes(query);

      if (this.selectedSearchType.value === 'both') return matchesTitle || matchesArtist;
      else if (this.selectedSearchType.value === 'title') return matchesTitle;
      else return matchesArtist;

      return false; // Fallback case
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
    const numTracks = this.jsonBlob.total;
    for (let i = 0; i < numTracks; i++) {
      let songEntry: SongEntry = {
        selected: false,
        title: 'lorem',
        artist: 'ipsum',
        length: 'sdfss',
        explicit: false,
        popularity: 0,
        release: 'N/A',
      };
      songEntry.title = this.jsonBlob.items[i].track.name;
      songEntry.artist = this.jsonBlob.items[i].track.artists[0].name;
      songEntry.explicit = this.jsonBlob.items[i].track.explicit;
      songEntry.popularity = this.jsonBlob.items[i].track.popularity;
      songEntry.release = this.jsonBlob.items[i].track.album.release_date;

      //handle length
      let length = this.jsonBlob.items[i].track.duration_ms;
      const lenFormatted = new Date(length).toISOString().substr(11, 8);
      songEntry.length = lenFormatted;

      this.songData[i] = songEntry;
    }

    this.songData = this.songData.slice(0, numTracks);
  }
}

interface SongEntry {
  selected: boolean;
  title: string;
  artist: string;
  length: string;
  explicit: boolean;
  popularity: number;
  release: string;
}
