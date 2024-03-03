import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { SongArtistJoinFormService, SongArtistJoinFormGroup } from './song-artist-join-form.service';
import { ISongArtistJoin } from '../song-artist-join.model';
import { SongArtistJoinService } from '../service/song-artist-join.service';
import { ISong } from 'app/entities/song/song.model';
import { SongService } from 'app/entities/song/service/song.service';
import { IMainArtist } from 'app/entities/main-artist/main-artist.model';
import { MainArtistService } from 'app/entities/main-artist/service/main-artist.service';

@Component({
  selector: 'jhi-song-artist-join-update',
  templateUrl: './song-artist-join-update.component.html',
})
export class SongArtistJoinUpdateComponent implements OnInit {
  isSaving = false;
  songArtistJoin: ISongArtistJoin | null = null;

  songsSharedCollection: ISong[] = [];
  mainArtistsSharedCollection: IMainArtist[] = [];

  editForm: SongArtistJoinFormGroup = this.songArtistJoinFormService.createSongArtistJoinFormGroup();

  constructor(
    protected songArtistJoinService: SongArtistJoinService,
    protected songArtistJoinFormService: SongArtistJoinFormService,
    protected songService: SongService,
    protected mainArtistService: MainArtistService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareSong = (o1: ISong | null, o2: ISong | null): boolean => this.songService.compareSong(o1, o2);

  compareMainArtist = (o1: IMainArtist | null, o2: IMainArtist | null): boolean => this.mainArtistService.compareMainArtist(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ songArtistJoin }) => {
      this.songArtistJoin = songArtistJoin;
      if (songArtistJoin) {
        this.updateForm(songArtistJoin);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const songArtistJoin = this.songArtistJoinFormService.getSongArtistJoin(this.editForm);
    if (songArtistJoin.id !== null) {
      this.subscribeToSaveResponse(this.songArtistJoinService.update(songArtistJoin));
    } else {
      this.subscribeToSaveResponse(this.songArtistJoinService.create(songArtistJoin));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISongArtistJoin>>): void {
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

  protected updateForm(songArtistJoin: ISongArtistJoin): void {
    this.songArtistJoin = songArtistJoin;
    this.songArtistJoinFormService.resetForm(this.editForm, songArtistJoin);

    this.songsSharedCollection = this.songService.addSongToCollectionIfMissing<ISong>(this.songsSharedCollection, songArtistJoin.song);
    this.mainArtistsSharedCollection = this.mainArtistService.addMainArtistToCollectionIfMissing<IMainArtist>(
      this.mainArtistsSharedCollection,
      songArtistJoin.mainArtist
    );
  }

  protected loadRelationshipsOptions(): void {
    this.songService
      .query()
      .pipe(map((res: HttpResponse<ISong[]>) => res.body ?? []))
      .pipe(map((songs: ISong[]) => this.songService.addSongToCollectionIfMissing<ISong>(songs, this.songArtistJoin?.song)))
      .subscribe((songs: ISong[]) => (this.songsSharedCollection = songs));

    this.mainArtistService
      .query()
      .pipe(map((res: HttpResponse<IMainArtist[]>) => res.body ?? []))
      .pipe(
        map((mainArtists: IMainArtist[]) =>
          this.mainArtistService.addMainArtistToCollectionIfMissing<IMainArtist>(mainArtists, this.songArtistJoin?.mainArtist)
        )
      )
      .subscribe((mainArtists: IMainArtist[]) => (this.mainArtistsSharedCollection = mainArtists));
  }
}
