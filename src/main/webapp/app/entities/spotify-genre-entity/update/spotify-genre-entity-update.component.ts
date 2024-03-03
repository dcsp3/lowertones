import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { SpotifyGenreEntityFormService, SpotifyGenreEntityFormGroup } from './spotify-genre-entity-form.service';
import { ISpotifyGenreEntity } from '../spotify-genre-entity.model';
import { SpotifyGenreEntityService } from '../service/spotify-genre-entity.service';
import { ISong } from 'app/entities/song/song.model';
import { SongService } from 'app/entities/song/service/song.service';
import { IAlbum } from 'app/entities/album/album.model';
import { AlbumService } from 'app/entities/album/service/album.service';
import { IMainArtist } from 'app/entities/main-artist/main-artist.model';
import { MainArtistService } from 'app/entities/main-artist/service/main-artist.service';

@Component({
  selector: 'jhi-spotify-genre-entity-update',
  templateUrl: './spotify-genre-entity-update.component.html',
})
export class SpotifyGenreEntityUpdateComponent implements OnInit {
  isSaving = false;
  spotifyGenreEntity: ISpotifyGenreEntity | null = null;

  songsSharedCollection: ISong[] = [];
  albumsSharedCollection: IAlbum[] = [];
  mainArtistsSharedCollection: IMainArtist[] = [];

  editForm: SpotifyGenreEntityFormGroup = this.spotifyGenreEntityFormService.createSpotifyGenreEntityFormGroup();

  constructor(
    protected spotifyGenreEntityService: SpotifyGenreEntityService,
    protected spotifyGenreEntityFormService: SpotifyGenreEntityFormService,
    protected songService: SongService,
    protected albumService: AlbumService,
    protected mainArtistService: MainArtistService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareSong = (o1: ISong | null, o2: ISong | null): boolean => this.songService.compareSong(o1, o2);

  compareAlbum = (o1: IAlbum | null, o2: IAlbum | null): boolean => this.albumService.compareAlbum(o1, o2);

  compareMainArtist = (o1: IMainArtist | null, o2: IMainArtist | null): boolean => this.mainArtistService.compareMainArtist(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ spotifyGenreEntity }) => {
      this.spotifyGenreEntity = spotifyGenreEntity;
      if (spotifyGenreEntity) {
        this.updateForm(spotifyGenreEntity);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const spotifyGenreEntity = this.spotifyGenreEntityFormService.getSpotifyGenreEntity(this.editForm);
    if (spotifyGenreEntity.id !== null) {
      this.subscribeToSaveResponse(this.spotifyGenreEntityService.update(spotifyGenreEntity));
    } else {
      this.subscribeToSaveResponse(this.spotifyGenreEntityService.create(spotifyGenreEntity));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISpotifyGenreEntity>>): void {
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

  protected updateForm(spotifyGenreEntity: ISpotifyGenreEntity): void {
    this.spotifyGenreEntity = spotifyGenreEntity;
    this.spotifyGenreEntityFormService.resetForm(this.editForm, spotifyGenreEntity);

    this.songsSharedCollection = this.songService.addSongToCollectionIfMissing<ISong>(this.songsSharedCollection, spotifyGenreEntity.song);
    this.albumsSharedCollection = this.albumService.addAlbumToCollectionIfMissing<IAlbum>(
      this.albumsSharedCollection,
      spotifyGenreEntity.album
    );
    this.mainArtistsSharedCollection = this.mainArtistService.addMainArtistToCollectionIfMissing<IMainArtist>(
      this.mainArtistsSharedCollection,
      spotifyGenreEntity.mainArtist
    );
  }

  protected loadRelationshipsOptions(): void {
    this.songService
      .query()
      .pipe(map((res: HttpResponse<ISong[]>) => res.body ?? []))
      .pipe(map((songs: ISong[]) => this.songService.addSongToCollectionIfMissing<ISong>(songs, this.spotifyGenreEntity?.song)))
      .subscribe((songs: ISong[]) => (this.songsSharedCollection = songs));

    this.albumService
      .query()
      .pipe(map((res: HttpResponse<IAlbum[]>) => res.body ?? []))
      .pipe(map((albums: IAlbum[]) => this.albumService.addAlbumToCollectionIfMissing<IAlbum>(albums, this.spotifyGenreEntity?.album)))
      .subscribe((albums: IAlbum[]) => (this.albumsSharedCollection = albums));

    this.mainArtistService
      .query()
      .pipe(map((res: HttpResponse<IMainArtist[]>) => res.body ?? []))
      .pipe(
        map((mainArtists: IMainArtist[]) =>
          this.mainArtistService.addMainArtistToCollectionIfMissing<IMainArtist>(mainArtists, this.spotifyGenreEntity?.mainArtist)
        )
      )
      .subscribe((mainArtists: IMainArtist[]) => (this.mainArtistsSharedCollection = mainArtists));
  }
}
