import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { MusicbrainzGenreEntityFormService, MusicbrainzGenreEntityFormGroup } from './musicbrainz-genre-entity-form.service';
import { IMusicbrainzGenreEntity } from '../musicbrainz-genre-entity.model';
import { MusicbrainzGenreEntityService } from '../service/musicbrainz-genre-entity.service';
import { ISong } from 'app/entities/song/song.model';
import { SongService } from 'app/entities/song/service/song.service';
import { IAlbum } from 'app/entities/album/album.model';
import { AlbumService } from 'app/entities/album/service/album.service';
import { IMainArtist } from 'app/entities/main-artist/main-artist.model';
import { MainArtistService } from 'app/entities/main-artist/service/main-artist.service';

@Component({
  selector: 'jhi-musicbrainz-genre-entity-update',
  templateUrl: './musicbrainz-genre-entity-update.component.html',
})
export class MusicbrainzGenreEntityUpdateComponent implements OnInit {
  isSaving = false;
  musicbrainzGenreEntity: IMusicbrainzGenreEntity | null = null;

  songsSharedCollection: ISong[] = [];
  albumsSharedCollection: IAlbum[] = [];
  mainArtistsSharedCollection: IMainArtist[] = [];

  editForm: MusicbrainzGenreEntityFormGroup = this.musicbrainzGenreEntityFormService.createMusicbrainzGenreEntityFormGroup();

  constructor(
    protected musicbrainzGenreEntityService: MusicbrainzGenreEntityService,
    protected musicbrainzGenreEntityFormService: MusicbrainzGenreEntityFormService,
    protected songService: SongService,
    protected albumService: AlbumService,
    protected mainArtistService: MainArtistService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareSong = (o1: ISong | null, o2: ISong | null): boolean => this.songService.compareSong(o1, o2);

  compareAlbum = (o1: IAlbum | null, o2: IAlbum | null): boolean => this.albumService.compareAlbum(o1, o2);

  compareMainArtist = (o1: IMainArtist | null, o2: IMainArtist | null): boolean => this.mainArtistService.compareMainArtist(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ musicbrainzGenreEntity }) => {
      this.musicbrainzGenreEntity = musicbrainzGenreEntity;
      if (musicbrainzGenreEntity) {
        this.updateForm(musicbrainzGenreEntity);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const musicbrainzGenreEntity = this.musicbrainzGenreEntityFormService.getMusicbrainzGenreEntity(this.editForm);
    if (musicbrainzGenreEntity.id !== null) {
      this.subscribeToSaveResponse(this.musicbrainzGenreEntityService.update(musicbrainzGenreEntity));
    } else {
      this.subscribeToSaveResponse(this.musicbrainzGenreEntityService.create(musicbrainzGenreEntity));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMusicbrainzGenreEntity>>): void {
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

  protected updateForm(musicbrainzGenreEntity: IMusicbrainzGenreEntity): void {
    this.musicbrainzGenreEntity = musicbrainzGenreEntity;
    this.musicbrainzGenreEntityFormService.resetForm(this.editForm, musicbrainzGenreEntity);

    this.songsSharedCollection = this.songService.addSongToCollectionIfMissing<ISong>(
      this.songsSharedCollection,
      musicbrainzGenreEntity.song
    );
    this.albumsSharedCollection = this.albumService.addAlbumToCollectionIfMissing<IAlbum>(
      this.albumsSharedCollection,
      musicbrainzGenreEntity.album
    );
    this.mainArtistsSharedCollection = this.mainArtistService.addMainArtistToCollectionIfMissing<IMainArtist>(
      this.mainArtistsSharedCollection,
      musicbrainzGenreEntity.mainArtist
    );
  }

  protected loadRelationshipsOptions(): void {
    this.songService
      .query()
      .pipe(map((res: HttpResponse<ISong[]>) => res.body ?? []))
      .pipe(map((songs: ISong[]) => this.songService.addSongToCollectionIfMissing<ISong>(songs, this.musicbrainzGenreEntity?.song)))
      .subscribe((songs: ISong[]) => (this.songsSharedCollection = songs));

    this.albumService
      .query()
      .pipe(map((res: HttpResponse<IAlbum[]>) => res.body ?? []))
      .pipe(map((albums: IAlbum[]) => this.albumService.addAlbumToCollectionIfMissing<IAlbum>(albums, this.musicbrainzGenreEntity?.album)))
      .subscribe((albums: IAlbum[]) => (this.albumsSharedCollection = albums));

    this.mainArtistService
      .query()
      .pipe(map((res: HttpResponse<IMainArtist[]>) => res.body ?? []))
      .pipe(
        map((mainArtists: IMainArtist[]) =>
          this.mainArtistService.addMainArtistToCollectionIfMissing<IMainArtist>(mainArtists, this.musicbrainzGenreEntity?.mainArtist)
        )
      )
      .subscribe((mainArtists: IMainArtist[]) => (this.mainArtistsSharedCollection = mainArtists));
  }
}
