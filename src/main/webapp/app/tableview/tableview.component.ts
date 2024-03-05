import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-tableview',
  templateUrl: './tableview.component.html',
  styleUrls: ['./tableview.component.scss'],
})
export class TableviewComponent implements OnInit {
  songData: SongEntry[];
  jsonBlob: any;

  constructor() {
    this.songData = new Array(100);
  }

  ngOnInit(): void {
    this.genSongList();
  }

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
