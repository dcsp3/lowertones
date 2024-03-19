import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { RelatedArtistsFormService, RelatedArtistsFormGroup } from './related-artists-form.service';
import { IRelatedArtists } from '../related-artists.model';
import { RelatedArtistsService } from '../service/related-artists.service';

@Component({
  selector: 'jhi-related-artists-update',
  templateUrl: './related-artists-update.component.html',
})
export class RelatedArtistsUpdateComponent implements OnInit {
  isSaving = false;
  relatedArtists: IRelatedArtists | null = null;

  editForm: RelatedArtistsFormGroup = this.relatedArtistsFormService.createRelatedArtistsFormGroup();

  constructor(
    protected relatedArtistsService: RelatedArtistsService,
    protected relatedArtistsFormService: RelatedArtistsFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ relatedArtists }) => {
      this.relatedArtists = relatedArtists;
      if (relatedArtists) {
        this.updateForm(relatedArtists);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const relatedArtists = this.relatedArtistsFormService.getRelatedArtists(this.editForm);
    if (relatedArtists.id !== null) {
      this.subscribeToSaveResponse(this.relatedArtistsService.update(relatedArtists));
    } else {
      this.subscribeToSaveResponse(this.relatedArtistsService.create(relatedArtists));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRelatedArtists>>): void {
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

  protected updateForm(relatedArtists: IRelatedArtists): void {
    this.relatedArtists = relatedArtists;
    this.relatedArtistsFormService.resetForm(this.editForm, relatedArtists);
  }
}
