import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PlaylistSongJoinComponent } from '../list/playlist-song-join.component';
import { PlaylistSongJoinDetailComponent } from '../detail/playlist-song-join-detail.component';
import { PlaylistSongJoinUpdateComponent } from '../update/playlist-song-join-update.component';
import { PlaylistSongJoinRoutingResolveService } from './playlist-song-join-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const playlistSongJoinRoute: Routes = [
  {
    path: '',
    component: PlaylistSongJoinComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PlaylistSongJoinDetailComponent,
    resolve: {
      playlistSongJoin: PlaylistSongJoinRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PlaylistSongJoinUpdateComponent,
    resolve: {
      playlistSongJoin: PlaylistSongJoinRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PlaylistSongJoinUpdateComponent,
    resolve: {
      playlistSongJoin: PlaylistSongJoinRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(playlistSongJoinRoute)],
  exports: [RouterModule],
})
export class PlaylistSongJoinRoutingModule {}
