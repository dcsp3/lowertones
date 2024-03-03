import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISongArtistJoin } from '../song-artist-join.model';

@Component({
  selector: 'jhi-song-artist-join-detail',
  templateUrl: './song-artist-join-detail.component.html',
})
export class SongArtistJoinDetailComponent implements OnInit {
  songArtistJoin: ISongArtistJoin | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ songArtistJoin }) => {
      this.songArtistJoin = songArtistJoin;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
