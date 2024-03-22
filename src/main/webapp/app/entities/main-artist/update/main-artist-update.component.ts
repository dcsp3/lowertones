import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { MainArtistFormService, MainArtistFormGroup } from './main-artist-form.service';
import { IMainArtist } from '../main-artist.model';
import { MainArtistService } from '../service/main-artist.service';
import { IRelatedArtists } from 'app/entities/related-artists/related-artists.model';
import { RelatedArtistsService } from 'app/entities/related-artists/service/related-artists.service';
import { IAlbum } from 'app/entities/album/album.model';
import { AlbumService } from 'app/entities/album/service/album.service';

@Component({
  selector: 'jhi-main-artist-update',
  templateUrl: './main-artist-update.component.html',
})
export class MainArtistUpdateComponent implements OnInit {
  isSaving = false;
  mainArtist: IMainArtist | null = null;

  relatedArtistsCollection: IRelatedArtists[] = [];
  albumsSharedCollection: IAlbum[] = [];

  editForm: MainArtistFormGroup = this.mainArtistFormService.createMainArtistFormGroup();

  constructor(
    protected mainArtistService: MainArtistService,
    protected mainArtistFormService: MainArtistFormService,
    protected relatedArtistsService: RelatedArtistsService,
    protected albumService: AlbumService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareRelatedArtists = (o1: IRelatedArtists | null, o2: IRelatedArtists | null): boolean =>
    this.relatedArtistsService.compareRelatedArtists(o1, o2);

  compareAlbum = (o1: IAlbum | null, o2: IAlbum | null): boolean => this.albumService.compareAlbum(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mainArtist }) => {
      this.mainArtist = mainArtist;
      if (mainArtist) {
        this.updateForm(mainArtist);
      }

      this.loadRelationshipsOptions();
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

    this.relatedArtistsCollection = this.relatedArtistsService.addRelatedArtistsToCollectionIfMissing<IRelatedArtists>(
      this.relatedArtistsCollection,
      mainArtist.relatedArtists
    );
    this.albumsSharedCollection = this.albumService.addAlbumToCollectionIfMissing<IAlbum>(
      this.albumsSharedCollection,
      ...(mainArtist.albums ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.relatedArtistsService
      .query({ filter: 'mainartist-is-null' })
      .pipe(map((res: HttpResponse<IRelatedArtists[]>) => res.body ?? []))
      .pipe(
        map((relatedArtists: IRelatedArtists[]) =>
          this.relatedArtistsService.addRelatedArtistsToCollectionIfMissing<IRelatedArtists>(
            relatedArtists,
            this.mainArtist?.relatedArtists
          )
        )
      )
      .subscribe((relatedArtists: IRelatedArtists[]) => (this.relatedArtistsCollection = relatedArtists));

    this.albumService
      .query()
      .pipe(map((res: HttpResponse<IAlbum[]>) => res.body ?? []))
      .pipe(map((albums: IAlbum[]) => this.albumService.addAlbumToCollectionIfMissing<IAlbum>(albums, ...(this.mainArtist?.albums ?? []))))
      .subscribe((albums: IAlbum[]) => (this.albumsSharedCollection = albums));
  }
}
