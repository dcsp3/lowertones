import { Component, OnInit } from '@angular/core';

//Depricated definitions for old advanced search

/*
interface SongEntry {
  [key: string]: boolean | number | string;
  selected: boolean;
  title: string;
  artist: string;
  length: string;
  explicit: boolean;
  popularity: number;
  release: string;
}

type Filter = { operator: string; value: string | boolean };
*/

@Component({
  selector: 'jhi-tableview',
  templateUrl: './tableview.component.html',
  styleUrls: ['./tableview.component.scss'],
})
export class TableviewComponent implements OnInit {
  songData: SongEntry[];
  filteredSongData: SongEntry[] = [];
  searchQuery: string = '';
  searchType: string = 'both'; // Default to searching both titles and artists
  jsonBlob: any;

  constructor() {
    this.songData = new Array(100);
  }

  ngOnInit(): void {
    this.genSongList();
    this.filteredSongData = this.songData;
  }

  applySearch(): void {
    this.filteredSongData = this.songData.filter(song => {
      const query = this.searchQuery.toLowerCase();
      const matchesTitle = song.title.toLowerCase().includes(query);
      const matchesArtist = song.artist.toLowerCase().includes(query);

      if (this.searchType === 'both') return matchesTitle || matchesArtist;
      if (this.searchType === 'title') return matchesTitle;
      if (this.searchType === 'artist') return matchesArtist;

      return false; // Fallback case
    });
  }

  //old code for side filters

  /*

  applyFilter(filterValue: string, property: keyof SongEntry) {
    if (!filterValue) {
      this.filteredSongData = this.songData; // No filter, show all data
    } else {
      this.filteredSongData = this.songData.filter(song => {
        // Convert the property value to a string for comparison
        const propertyValue = song[property];
        // If the property value is null or undefined, treat it as an empty string
        const valueString = propertyValue == null ? '' : propertyValue.toString();
        return valueString.toLowerCase().includes(filterValue.toLowerCase());
      });
    }
  }*/

  //code for advanced search

  /*
  applyAdvancedFilter(query: string): void {
    const filters = this.parseQuery(query);
    this.filteredSongData = this.songData.filter(song => 
      Object.entries(filters).every(([key, {operator, value}]) => 
        this.applyCondition(song[key], operator, value)
      )
    );
  }

  parseQuery(query: string): Record<string, Filter> {
    const regex = /(\w+):([^:\s]+)\s?/g;
    let match;
    const filters: Record<string, Filter> = {};
    while ((match = regex.exec(query)) !== null) {
      const [, field, value] = match;
      let operator: string = '=';
      let val: string | boolean = value;
  
      // Check if the value starts with an operator
      if (value.match(/^([<>=])/)) {
        operator = value[0];
        val = value.substring(1);
      }
  
      // Convert "true" or "false" to boolean values
      if (val.toLowerCase() === 'true') {
        val = true;
      } else if (val.toLowerCase() === 'false') {
        val = false;
      }
  
      filters[field] = { operator, value: val };
    }
    return filters;
  }
  

  applyCondition(songValue: string | number | boolean | undefined, operator: string, value: string | boolean): boolean {
    // Check if songValue is undefined
    if (typeof songValue === 'undefined') {
      return false;
    }
  
    // Handle comparison logic based on the type of songValue and value
    if (typeof songValue === 'boolean' && typeof value === 'boolean') {
      return operator === '=' ? songValue === value : false;
    }
  
    // Assuming songValue is not boolean or undefined at this point,
    // convert songValue to string for comparison with non-boolean values
    let songValueStr = songValue.toString();
  
    // Continue with the existing comparison logic for strings and numbers,
    // making sure to convert value to a string if it's not already
    switch (operator) {
      case '<': return parseFloat(songValueStr) < parseFloat(value.toString());
      case '>': return parseFloat(songValueStr) > parseFloat(value.toString());
      case '=': 
        // For string comparison, ensure both are treated as strings
        return songValueStr.toLowerCase().includes(value.toString().toLowerCase());
      default: return false;
    }
  }
  
  */

  genSongList(): void {
    const token = sessionStorage.getItem('jhi-authenticationToken')?.slice(1, -1);
    const headers: Headers = new Headers();
    headers.set('Authorization', 'Bearer ' + token);
    const request: RequestInfo = new Request('/api/playlist-tracks', {
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
