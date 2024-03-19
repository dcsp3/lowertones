import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRelatedArtists } from '../related-artists.model';

@Component({
  selector: 'jhi-related-artists-detail',
  templateUrl: './related-artists-detail.component.html',
})
export class RelatedArtistsDetailComponent implements OnInit {
  relatedArtists: IRelatedArtists | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ relatedArtists }) => {
      this.relatedArtists = relatedArtists;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
