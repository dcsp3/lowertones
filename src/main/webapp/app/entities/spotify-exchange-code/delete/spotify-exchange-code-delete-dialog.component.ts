import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISpotifyExchangeCode } from '../spotify-exchange-code.model';
import { SpotifyExchangeCodeService } from '../service/spotify-exchange-code.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './spotify-exchange-code-delete-dialog.component.html',
})
export class SpotifyExchangeCodeDeleteDialogComponent {
  spotifyExchangeCode?: ISpotifyExchangeCode;

  constructor(protected spotifyExchangeCodeService: SpotifyExchangeCodeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.spotifyExchangeCodeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
