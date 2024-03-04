import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MusicBrainzSongAttributionComponent } from './list/music-brainz-song-attribution.component';
import { MusicBrainzSongAttributionDetailComponent } from './detail/music-brainz-song-attribution-detail.component';
import { MusicBrainzSongAttributionUpdateComponent } from './update/music-brainz-song-attribution-update.component';
import { MusicBrainzSongAttributionDeleteDialogComponent } from './delete/music-brainz-song-attribution-delete-dialog.component';
import { MusicBrainzSongAttributionRoutingModule } from './route/music-brainz-song-attribution-routing.module';

@NgModule({
  imports: [SharedModule, MusicBrainzSongAttributionRoutingModule],
  declarations: [
    MusicBrainzSongAttributionComponent,
    MusicBrainzSongAttributionDetailComponent,
    MusicBrainzSongAttributionUpdateComponent,
    MusicBrainzSongAttributionDeleteDialogComponent,
  ],
})
export class MusicBrainzSongAttributionModule {}
