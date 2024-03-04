import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISpotifyGenreEntity } from '../spotify-genre-entity.model';
import { SpotifyGenreEntityService } from '../service/spotify-genre-entity.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './spotify-genre-entity-delete-dialog.component.html',
})
export class SpotifyGenreEntityDeleteDialogComponent {
  spotifyGenreEntity?: ISpotifyGenreEntity;

  constructor(protected spotifyGenreEntityService: SpotifyGenreEntityService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.spotifyGenreEntityService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
