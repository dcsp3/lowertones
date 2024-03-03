import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISongArtistJoin } from '../song-artist-join.model';
import { SongArtistJoinService } from '../service/song-artist-join.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './song-artist-join-delete-dialog.component.html',
})
export class SongArtistJoinDeleteDialogComponent {
  songArtistJoin?: ISongArtistJoin;

  constructor(protected songArtistJoinService: SongArtistJoinService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.songArtistJoinService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
