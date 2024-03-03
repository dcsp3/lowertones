import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMainArtist } from '../main-artist.model';

@Component({
  selector: 'jhi-main-artist-detail',
  templateUrl: './main-artist-detail.component.html',
})
export class MainArtistDetailComponent implements OnInit {
  mainArtist: IMainArtist | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mainArtist }) => {
      this.mainArtist = mainArtist;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
