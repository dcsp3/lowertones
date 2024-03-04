import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMusicbrainzGenreEntity } from '../musicbrainz-genre-entity.model';
import { MusicbrainzGenreEntityService } from '../service/musicbrainz-genre-entity.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './musicbrainz-genre-entity-delete-dialog.component.html',
})
export class MusicbrainzGenreEntityDeleteDialogComponent {
  musicbrainzGenreEntity?: IMusicbrainzGenreEntity;

  constructor(protected musicbrainzGenreEntityService: MusicbrainzGenreEntityService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.musicbrainzGenreEntityService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
