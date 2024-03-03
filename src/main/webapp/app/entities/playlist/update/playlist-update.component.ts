import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PlaylistFormService, PlaylistFormGroup } from './playlist-form.service';
import { IPlaylist } from '../playlist.model';
import { PlaylistService } from '../service/playlist.service';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';

@Component({
  selector: 'jhi-playlist-update',
  templateUrl: './playlist-update.component.html',
})
export class PlaylistUpdateComponent implements OnInit {
  isSaving = false;
  playlist: IPlaylist | null = null;

  appUsersSharedCollection: IAppUser[] = [];

  editForm: PlaylistFormGroup = this.playlistFormService.createPlaylistFormGroup();

  constructor(
    protected playlistService: PlaylistService,
    protected playlistFormService: PlaylistFormService,
    protected appUserService: AppUserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareAppUser = (o1: IAppUser | null, o2: IAppUser | null): boolean => this.appUserService.compareAppUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ playlist }) => {
      this.playlist = playlist;
      if (playlist) {
        this.updateForm(playlist);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const playlist = this.playlistFormService.getPlaylist(this.editForm);
    if (playlist.id !== null) {
      this.subscribeToSaveResponse(this.playlistService.update(playlist));
    } else {
      this.subscribeToSaveResponse(this.playlistService.create(playlist));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlaylist>>): void {
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

  protected updateForm(playlist: IPlaylist): void {
    this.playlist = playlist;
    this.playlistFormService.resetForm(this.editForm, playlist);

    this.appUsersSharedCollection = this.appUserService.addAppUserToCollectionIfMissing<IAppUser>(
      this.appUsersSharedCollection,
      playlist.appUser
    );
  }

  protected loadRelationshipsOptions(): void {
    this.appUserService
      .query()
      .pipe(map((res: HttpResponse<IAppUser[]>) => res.body ?? []))
      .pipe(map((appUsers: IAppUser[]) => this.appUserService.addAppUserToCollectionIfMissing<IAppUser>(appUsers, this.playlist?.appUser)))
      .subscribe((appUsers: IAppUser[]) => (this.appUsersSharedCollection = appUsers));
  }
}
