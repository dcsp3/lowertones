import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MusicbrainzGenreEntityComponent } from './list/musicbrainz-genre-entity.component';
import { MusicbrainzGenreEntityDetailComponent } from './detail/musicbrainz-genre-entity-detail.component';
import { MusicbrainzGenreEntityUpdateComponent } from './update/musicbrainz-genre-entity-update.component';
import { MusicbrainzGenreEntityDeleteDialogComponent } from './delete/musicbrainz-genre-entity-delete-dialog.component';
import { MusicbrainzGenreEntityRoutingModule } from './route/musicbrainz-genre-entity-routing.module';

@NgModule({
  imports: [SharedModule, MusicbrainzGenreEntityRoutingModule],
  declarations: [
    MusicbrainzGenreEntityComponent,
    MusicbrainzGenreEntityDetailComponent,
    MusicbrainzGenreEntityUpdateComponent,
    MusicbrainzGenreEntityDeleteDialogComponent,
  ],
})
export class MusicbrainzGenreEntityModule {}
