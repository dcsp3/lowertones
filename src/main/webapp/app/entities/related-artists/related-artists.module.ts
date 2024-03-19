import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RelatedArtistsComponent } from './list/related-artists.component';
import { RelatedArtistsDetailComponent } from './detail/related-artists-detail.component';
import { RelatedArtistsUpdateComponent } from './update/related-artists-update.component';
import { RelatedArtistsDeleteDialogComponent } from './delete/related-artists-delete-dialog.component';
import { RelatedArtistsRoutingModule } from './route/related-artists-routing.module';

@NgModule({
  imports: [SharedModule, RelatedArtistsRoutingModule],
  declarations: [
    RelatedArtistsComponent,
    RelatedArtistsDetailComponent,
    RelatedArtistsUpdateComponent,
    RelatedArtistsDeleteDialogComponent,
  ],
})
export class RelatedArtistsModule {}
