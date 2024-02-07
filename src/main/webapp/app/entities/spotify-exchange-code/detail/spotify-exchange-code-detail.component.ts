import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISpotifyExchangeCode } from '../spotify-exchange-code.model';

@Component({
  selector: 'jhi-spotify-exchange-code-detail',
  templateUrl: './spotify-exchange-code-detail.component.html',
})
export class SpotifyExchangeCodeDetailComponent implements OnInit {
  spotifyExchangeCode: ISpotifyExchangeCode | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ spotifyExchangeCode }) => {
      this.spotifyExchangeCode = spotifyExchangeCode;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
