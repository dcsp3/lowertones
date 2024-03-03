import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISpotifyGenreEntity } from '../spotify-genre-entity.model';

@Component({
  selector: 'jhi-spotify-genre-entity-detail',
  templateUrl: './spotify-genre-entity-detail.component.html',
})
export class SpotifyGenreEntityDetailComponent implements OnInit {
  spotifyGenreEntity: ISpotifyGenreEntity | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ spotifyGenreEntity }) => {
      this.spotifyGenreEntity = spotifyGenreEntity;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
