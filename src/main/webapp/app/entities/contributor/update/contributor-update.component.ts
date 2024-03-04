import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ContributorFormService, ContributorFormGroup } from './contributor-form.service';
import { IContributor } from '../contributor.model';
import { ContributorService } from '../service/contributor.service';
import { ISong } from 'app/entities/song/song.model';
import { SongService } from 'app/entities/song/service/song.service';

@Component({
  selector: 'jhi-contributor-update',
  templateUrl: './contributor-update.component.html',
})
export class ContributorUpdateComponent implements OnInit {
  isSaving = false;
  contributor: IContributor | null = null;

  songsSharedCollection: ISong[] = [];

  editForm: ContributorFormGroup = this.contributorFormService.createContributorFormGroup();

  constructor(
    protected contributorService: ContributorService,
    protected contributorFormService: ContributorFormService,
    protected songService: SongService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareSong = (o1: ISong | null, o2: ISong | null): boolean => this.songService.compareSong(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contributor }) => {
      this.contributor = contributor;
      if (contributor) {
        this.updateForm(contributor);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contributor = this.contributorFormService.getContributor(this.editForm);
    if (contributor.id !== null) {
      this.subscribeToSaveResponse(this.contributorService.update(contributor));
    } else {
      this.subscribeToSaveResponse(this.contributorService.create(contributor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContributor>>): void {
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

  protected updateForm(contributor: IContributor): void {
    this.contributor = contributor;
    this.contributorFormService.resetForm(this.editForm, contributor);

    this.songsSharedCollection = this.songService.addSongToCollectionIfMissing<ISong>(this.songsSharedCollection, contributor.song);
  }

  protected loadRelationshipsOptions(): void {
    this.songService
      .query()
      .pipe(map((res: HttpResponse<ISong[]>) => res.body ?? []))
      .pipe(map((songs: ISong[]) => this.songService.addSongToCollectionIfMissing<ISong>(songs, this.contributor?.song)))
      .subscribe((songs: ISong[]) => (this.songsSharedCollection = songs));
  }
}
