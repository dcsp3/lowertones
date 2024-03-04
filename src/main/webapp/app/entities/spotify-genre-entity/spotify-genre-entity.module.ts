import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SpotifyGenreEntityComponent } from './list/spotify-genre-entity.component';
import { SpotifyGenreEntityDetailComponent } from './detail/spotify-genre-entity-detail.component';
import { SpotifyGenreEntityUpdateComponent } from './update/spotify-genre-entity-update.component';
import { SpotifyGenreEntityDeleteDialogComponent } from './delete/spotify-genre-entity-delete-dialog.component';
import { SpotifyGenreEntityRoutingModule } from './route/spotify-genre-entity-routing.module';

@NgModule({
  imports: [SharedModule, SpotifyGenreEntityRoutingModule],
  declarations: [
    SpotifyGenreEntityComponent,
    SpotifyGenreEntityDetailComponent,
    SpotifyGenreEntityUpdateComponent,
    SpotifyGenreEntityDeleteDialogComponent,
  ],
})
export class SpotifyGenreEntityModule {}
