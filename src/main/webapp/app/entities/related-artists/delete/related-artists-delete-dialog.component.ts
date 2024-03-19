import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRelatedArtists } from '../related-artists.model';
import { RelatedArtistsService } from '../service/related-artists.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './related-artists-delete-dialog.component.html',
})
export class RelatedArtistsDeleteDialogComponent {
  relatedArtists?: IRelatedArtists;

  constructor(protected relatedArtistsService: RelatedArtistsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.relatedArtistsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
