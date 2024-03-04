import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { MusicBrainzSongAttributionFormService, MusicBrainzSongAttributionFormGroup } from './music-brainz-song-attribution-form.service';
import { IMusicBrainzSongAttribution } from '../music-brainz-song-attribution.model';
import { MusicBrainzSongAttributionService } from '../service/music-brainz-song-attribution.service';

@Component({
  selector: 'jhi-music-brainz-song-attribution-update',
  templateUrl: './music-brainz-song-attribution-update.component.html',
})
export class MusicBrainzSongAttributionUpdateComponent implements OnInit {
  isSaving = false;
  musicBrainzSongAttribution: IMusicBrainzSongAttribution | null = null;

  editForm: MusicBrainzSongAttributionFormGroup = this.musicBrainzSongAttributionFormService.createMusicBrainzSongAttributionFormGroup();

  constructor(
    protected musicBrainzSongAttributionService: MusicBrainzSongAttributionService,
    protected musicBrainzSongAttributionFormService: MusicBrainzSongAttributionFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ musicBrainzSongAttribution }) => {
      this.musicBrainzSongAttribution = musicBrainzSongAttribution;
      if (musicBrainzSongAttribution) {
        this.updateForm(musicBrainzSongAttribution);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const musicBrainzSongAttribution = this.musicBrainzSongAttributionFormService.getMusicBrainzSongAttribution(this.editForm);
    if (musicBrainzSongAttribution.id !== null) {
      this.subscribeToSaveResponse(this.musicBrainzSongAttributionService.update(musicBrainzSongAttribution));
    } else {
      this.subscribeToSaveResponse(this.musicBrainzSongAttributionService.create(musicBrainzSongAttribution));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMusicBrainzSongAttribution>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(musicBrainzSongAttribution: IMusicBrainzSongAttribution): void {
    this.musicBrainzSongAttribution = musicBrainzSongAttribution;
    this.musicBrainzSongAttributionFormService.resetForm(this.editForm, musicBrainzSongAttribution);
  }
}
