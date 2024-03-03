import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SongArtistJoinComponent } from './list/song-artist-join.component';
import { SongArtistJoinDetailComponent } from './detail/song-artist-join-detail.component';
import { SongArtistJoinUpdateComponent } from './update/song-artist-join-update.component';
import { SongArtistJoinDeleteDialogComponent } from './delete/song-artist-join-delete-dialog.component';
import { SongArtistJoinRoutingModule } from './route/song-artist-join-routing.module';

@NgModule({
  imports: [SharedModule, SongArtistJoinRoutingModule],
  declarations: [
    SongArtistJoinComponent,
    SongArtistJoinDetailComponent,
    SongArtistJoinUpdateComponent,
    SongArtistJoinDeleteDialogComponent,
  ],
})
export class SongArtistJoinModule {}
