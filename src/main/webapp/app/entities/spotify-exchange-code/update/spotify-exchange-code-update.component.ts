import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { SpotifyExchangeCodeFormService, SpotifyExchangeCodeFormGroup } from './spotify-exchange-code-form.service';
import { ISpotifyExchangeCode } from '../spotify-exchange-code.model';
import { SpotifyExchangeCodeService } from '../service/spotify-exchange-code.service';

@Component({
  selector: 'jhi-spotify-exchange-code-update',
  templateUrl: './spotify-exchange-code-update.component.html',
})
export class SpotifyExchangeCodeUpdateComponent implements OnInit {
  isSaving = false;
  spotifyExchangeCode: ISpotifyExchangeCode | null = null;

  editForm: SpotifyExchangeCodeFormGroup = this.spotifyExchangeCodeFormService.createSpotifyExchangeCodeFormGroup();

  constructor(
    protected spotifyExchangeCodeService: SpotifyExchangeCodeService,
    protected spotifyExchangeCodeFormService: SpotifyExchangeCodeFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ spotifyExchangeCode }) => {
      this.spotifyExchangeCode = spotifyExchangeCode;
      if (spotifyExchangeCode) {
        this.updateForm(spotifyExchangeCode);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const spotifyExchangeCode = this.spotifyExchangeCodeFormService.getSpotifyExchangeCode(this.editForm);
    if (spotifyExchangeCode.id !== null) {
      this.subscribeToSaveResponse(this.spotifyExchangeCodeService.update(spotifyExchangeCode));
    } else {
      this.subscribeToSaveResponse(this.spotifyExchangeCodeService.create(spotifyExchangeCode));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISpotifyExchangeCode>>): void {
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

  protected updateForm(spotifyExchangeCode: ISpotifyExchangeCode): void {
    this.spotifyExchangeCode = spotifyExchangeCode;
    this.spotifyExchangeCodeFormService.resetForm(this.editForm, spotifyExchangeCode);
  }
}
