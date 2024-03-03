import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PlaylistSongJoinFormService, PlaylistSongJoinFormGroup } from './playlist-song-join-form.service';
import { IPlaylistSongJoin } from '../playlist-song-join.model';
import { PlaylistSongJoinService } from '../service/playlist-song-join.service';
import { IPlaylist } from 'app/entities/playlist/playlist.model';
import { PlaylistService } from 'app/entities/playlist/service/playlist.service';
import { ISong } from 'app/entities/song/song.model';
import { SongService } from 'app/entities/song/service/song.service';

@Component({
  selector: 'jhi-playlist-song-join-update',
  templateUrl: './playlist-song-join-update.component.html',
})
export class PlaylistSongJoinUpdateComponent implements OnInit {
  isSaving = false;
  playlistSongJoin: IPlaylistSongJoin | null = null;

  playlistsSharedCollection: IPlaylist[] = [];
  songsSharedCollection: ISong[] = [];

  editForm: PlaylistSongJoinFormGroup = this.playlistSongJoinFormService.createPlaylistSongJoinFormGroup();

  constructor(
    protected playlistSongJoinService: PlaylistSongJoinService,
    protected playlistSongJoinFormService: PlaylistSongJoinFormService,
    protected playlistService: PlaylistService,
    protected songService: SongService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePlaylist = (o1: IPlaylist | null, o2: IPlaylist | null): boolean => this.playlistService.comparePlaylist(o1, o2);

  compareSong = (o1: ISong | null, o2: ISong | null): boolean => this.songService.compareSong(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ playlistSongJoin }) => {
      this.playlistSongJoin = playlistSongJoin;
      if (playlistSongJoin) {
        this.updateForm(playlistSongJoin);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const playlistSongJoin = this.playlistSongJoinFormService.getPlaylistSongJoin(this.editForm);
    if (playlistSongJoin.id !== null) {
      this.subscribeToSaveResponse(this.playlistSongJoinService.update(playlistSongJoin));
    } else {
      this.subscribeToSaveResponse(this.playlistSongJoinService.create(playlistSongJoin));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlaylistSongJoin>>): void {
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

  protected updateForm(playlistSongJoin: IPlaylistSongJoin): void {
    this.playlistSongJoin = playlistSongJoin;
    this.playlistSongJoinFormService.resetForm(this.editForm, playlistSongJoin);

    this.playlistsSharedCollection = this.playlistService.addPlaylistToCollectionIfMissing<IPlaylist>(
      this.playlistsSharedCollection,
      playlistSongJoin.playlist
    );
    this.songsSharedCollection = this.songService.addSongToCollectionIfMissing<ISong>(this.songsSharedCollection, playlistSongJoin.song);
  }

  protected loadRelationshipsOptions(): void {
    this.playlistService
      .query()
      .pipe(map((res: HttpResponse<IPlaylist[]>) => res.body ?? []))
      .pipe(
        map((playlists: IPlaylist[]) =>
          this.playlistService.addPlaylistToCollectionIfMissing<IPlaylist>(playlists, this.playlistSongJoin?.playlist)
        )
      )
      .subscribe((playlists: IPlaylist[]) => (this.playlistsSharedCollection = playlists));

    this.songService
      .query()
      .pipe(map((res: HttpResponse<ISong[]>) => res.body ?? []))
      .pipe(map((songs: ISong[]) => this.songService.addSongToCollectionIfMissing<ISong>(songs, this.playlistSongJoin?.song)))
      .subscribe((songs: ISong[]) => (this.songsSharedCollection = songs));
  }
}
