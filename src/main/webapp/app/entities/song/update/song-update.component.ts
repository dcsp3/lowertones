import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { SongFormService, SongFormGroup } from './song-form.service';
import { ISong } from '../song.model';
import { SongService } from '../service/song.service';
import { IContributor } from 'app/entities/contributor/contributor.model';
import { ContributorService } from 'app/entities/contributor/service/contributor.service';
import { IAlbum } from 'app/entities/album/album.model';
import { AlbumService } from 'app/entities/album/service/album.service';
import { AlbumType } from 'app/entities/enumerations/album-type.model';

@Component({
  selector: 'jhi-song-update',
  templateUrl: './song-update.component.html',
})
export class SongUpdateComponent implements OnInit {
  isSaving = false;
  song: ISong | null = null;
  albumTypeValues = Object.keys(AlbumType);

  contributorsSharedCollection: IContributor[] = [];
  albumsSharedCollection: IAlbum[] = [];

  editForm: SongFormGroup = this.songFormService.createSongFormGroup();

  constructor(
    protected songService: SongService,
    protected songFormService: SongFormService,
    protected contributorService: ContributorService,
    protected albumService: AlbumService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareContributor = (o1: IContributor | null, o2: IContributor | null): boolean => this.contributorService.compareContributor(o1, o2);

  compareAlbum = (o1: IAlbum | null, o2: IAlbum | null): boolean => this.albumService.compareAlbum(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ song }) => {
      this.song = song;
      if (song) {
        this.updateForm(song);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const song = this.songFormService.getSong(this.editForm);
    if (song.id !== null) {
      this.subscribeToSaveResponse(this.songService.update(song));
    } else {
      this.subscribeToSaveResponse(this.songService.create(song));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISong>>): void {
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

  protected updateForm(song: ISong): void {
    this.song = song;
    this.songFormService.resetForm(this.editForm, song);

    this.contributorsSharedCollection = this.contributorService.addContributorToCollectionIfMissing<IContributor>(
      this.contributorsSharedCollection,
      ...(song.contributors ?? [])
    );
    this.albumsSharedCollection = this.albumService.addAlbumToCollectionIfMissing<IAlbum>(this.albumsSharedCollection, song.album);
  }

  protected loadRelationshipsOptions(): void {
    this.contributorService
      .query()
      .pipe(map((res: HttpResponse<IContributor[]>) => res.body ?? []))
      .pipe(
        map((contributors: IContributor[]) =>
          this.contributorService.addContributorToCollectionIfMissing<IContributor>(contributors, ...(this.song?.contributors ?? []))
        )
      )
      .subscribe((contributors: IContributor[]) => (this.contributorsSharedCollection = contributors));

    this.albumService
      .query()
      .pipe(map((res: HttpResponse<IAlbum[]>) => res.body ?? []))
      .pipe(map((albums: IAlbum[]) => this.albumService.addAlbumToCollectionIfMissing<IAlbum>(albums, this.song?.album)))
      .subscribe((albums: IAlbum[]) => (this.albumsSharedCollection = albums));
  }
}
