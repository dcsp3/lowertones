import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { AlbumFormService, AlbumFormGroup } from './album-form.service';
import { IAlbum } from '../album.model';
import { AlbumService } from '../service/album.service';
import { IMainArtist } from 'app/entities/main-artist/main-artist.model';
import { MainArtistService } from 'app/entities/main-artist/service/main-artist.service';
import { ReleaseDatePrecision } from 'app/entities/enumerations/release-date-precision.model';
import { AlbumType } from 'app/entities/enumerations/album-type.model';

@Component({
  selector: 'jhi-album-update',
  templateUrl: './album-update.component.html',
})
export class AlbumUpdateComponent implements OnInit {
  isSaving = false;
  album: IAlbum | null = null;
  releaseDatePrecisionValues = Object.keys(ReleaseDatePrecision);
  albumTypeValues = Object.keys(AlbumType);

  mainArtistsSharedCollection: IMainArtist[] = [];

  editForm: AlbumFormGroup = this.albumFormService.createAlbumFormGroup();

  constructor(
    protected albumService: AlbumService,
    protected albumFormService: AlbumFormService,
    protected mainArtistService: MainArtistService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareMainArtist = (o1: IMainArtist | null, o2: IMainArtist | null): boolean => this.mainArtistService.compareMainArtist(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ album }) => {
      this.album = album;
      if (album) {
        this.updateForm(album);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const album = this.albumFormService.getAlbum(this.editForm);
    if (album.id !== null) {
      this.subscribeToSaveResponse(this.albumService.update(album));
    } else {
      this.subscribeToSaveResponse(this.albumService.create(album));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAlbum>>): void {
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

  protected updateForm(album: IAlbum): void {
    this.album = album;
    this.albumFormService.resetForm(this.editForm, album);

    this.mainArtistsSharedCollection = this.mainArtistService.addMainArtistToCollectionIfMissing<IMainArtist>(
      this.mainArtistsSharedCollection,
      album.mainArtist
    );
  }

  protected loadRelationshipsOptions(): void {
    this.mainArtistService
      .query()
      .pipe(map((res: HttpResponse<IMainArtist[]>) => res.body ?? []))
      .pipe(
        map((mainArtists: IMainArtist[]) =>
          this.mainArtistService.addMainArtistToCollectionIfMissing<IMainArtist>(mainArtists, this.album?.mainArtist)
        )
      )
      .subscribe((mainArtists: IMainArtist[]) => (this.mainArtistsSharedCollection = mainArtists));
  }
}
