import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMainArtist } from '../main-artist.model';
import { MainArtistService } from '../service/main-artist.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './main-artist-delete-dialog.component.html',
})
export class MainArtistDeleteDialogComponent {
  mainArtist?: IMainArtist;

  constructor(protected mainArtistService: MainArtistService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mainArtistService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
