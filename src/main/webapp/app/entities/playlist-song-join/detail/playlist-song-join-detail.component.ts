import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlaylistSongJoin } from '../playlist-song-join.model';

@Component({
  selector: 'jhi-playlist-song-join-detail',
  templateUrl: './playlist-song-join-detail.component.html',
})
export class PlaylistSongJoinDetailComponent implements OnInit {
  playlistSongJoin: IPlaylistSongJoin | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ playlistSongJoin }) => {
      this.playlistSongJoin = playlistSongJoin;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
