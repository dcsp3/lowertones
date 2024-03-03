import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMusicbrainzGenreEntity } from '../musicbrainz-genre-entity.model';

@Component({
  selector: 'jhi-musicbrainz-genre-entity-detail',
  templateUrl: './musicbrainz-genre-entity-detail.component.html',
})
export class MusicbrainzGenreEntityDetailComponent implements OnInit {
  musicbrainzGenreEntity: IMusicbrainzGenreEntity | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ musicbrainzGenreEntity }) => {
      this.musicbrainzGenreEntity = musicbrainzGenreEntity;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
