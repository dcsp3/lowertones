import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMusicBrainzSongAttribution } from '../music-brainz-song-attribution.model';
import { MusicBrainzSongAttributionService } from '../service/music-brainz-song-attribution.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './music-brainz-song-attribution-delete-dialog.component.html',
})
export class MusicBrainzSongAttributionDeleteDialogComponent {
  musicBrainzSongAttribution?: IMusicBrainzSongAttribution;

  constructor(protected musicBrainzSongAttributionService: MusicBrainzSongAttributionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.musicBrainzSongAttributionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
