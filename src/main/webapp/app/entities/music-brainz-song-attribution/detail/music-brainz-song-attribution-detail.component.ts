import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMusicBrainzSongAttribution } from '../music-brainz-song-attribution.model';

@Component({
  selector: 'jhi-music-brainz-song-attribution-detail',
  templateUrl: './music-brainz-song-attribution-detail.component.html',
})
export class MusicBrainzSongAttributionDetailComponent implements OnInit {
  musicBrainzSongAttribution: IMusicBrainzSongAttribution | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ musicBrainzSongAttribution }) => {
      this.musicBrainzSongAttribution = musicBrainzSongAttribution;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
