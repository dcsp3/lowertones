import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PlaylistSongJoinComponent } from './list/playlist-song-join.component';
import { PlaylistSongJoinDetailComponent } from './detail/playlist-song-join-detail.component';
import { PlaylistSongJoinUpdateComponent } from './update/playlist-song-join-update.component';
import { PlaylistSongJoinDeleteDialogComponent } from './delete/playlist-song-join-delete-dialog.component';
import { PlaylistSongJoinRoutingModule } from './route/playlist-song-join-routing.module';

@NgModule({
  imports: [SharedModule, PlaylistSongJoinRoutingModule],
  declarations: [
    PlaylistSongJoinComponent,
    PlaylistSongJoinDetailComponent,
    PlaylistSongJoinUpdateComponent,
    PlaylistSongJoinDeleteDialogComponent,
  ],
})
export class PlaylistSongJoinModule {}
