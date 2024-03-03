import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { MainArtistFormService, MainArtistFormGroup } from './main-artist-form.service';
import { IMainArtist } from '../main-artist.model';
import { MainArtistService } from '../service/main-artist.service';

@Component({
  selector: 'jhi-main-artist-update',
  templateUrl: './main-artist-update.component.html',
})
export class MainArtistUpdateComponent implements OnInit {
  isSaving = false;
  mainArtist: IMainArtist | null = null;

  editForm: MainArtistFormGroup = this.mainArtistFormService.createMainArtistFormGroup();

  constructor(
    protected mainArtistService: MainArtistService,
    protected mainArtistFormService: MainArtistFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mainArtist }) => {
      this.mainArtist = mainArtist;
      if (mainArtist) {
        this.updateForm(mainArtist);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mainArtist = this.mainArtistFormService.getMainArtist(this.editForm);
    if (mainArtist.id !== null) {
      this.subscribeToSaveResponse(this.mainArtistService.update(mainArtist));
    } else {
      this.subscribeToSaveResponse(this.mainArtistService.create(mainArtist));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMainArtist>>): void {
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

  protected updateForm(mainArtist: IMainArtist): void {
    this.mainArtist = mainArtist;
    this.mainArtistFormService.resetForm(this.editForm, mainArtist);
  }
}
