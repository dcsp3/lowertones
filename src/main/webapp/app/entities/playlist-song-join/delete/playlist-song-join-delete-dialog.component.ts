import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlaylistSongJoin } from '../playlist-song-join.model';
import { PlaylistSongJoinService } from '../service/playlist-song-join.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './playlist-song-join-delete-dialog.component.html',
})
export class PlaylistSongJoinDeleteDialogComponent {
  playlistSongJoin?: IPlaylistSongJoin;

  constructor(protected playlistSongJoinService: PlaylistSongJoinService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.playlistSongJoinService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
