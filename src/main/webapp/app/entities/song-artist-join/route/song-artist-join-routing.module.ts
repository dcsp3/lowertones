import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SongArtistJoinComponent } from '../list/song-artist-join.component';
import { SongArtistJoinDetailComponent } from '../detail/song-artist-join-detail.component';
import { SongArtistJoinUpdateComponent } from '../update/song-artist-join-update.component';
import { SongArtistJoinRoutingResolveService } from './song-artist-join-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const songArtistJoinRoute: Routes = [
  {
    path: '',
    component: SongArtistJoinComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SongArtistJoinDetailComponent,
    resolve: {
      songArtistJoin: SongArtistJoinRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SongArtistJoinUpdateComponent,
    resolve: {
      songArtistJoin: SongArtistJoinRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SongArtistJoinUpdateComponent,
    resolve: {
      songArtistJoin: SongArtistJoinRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(songArtistJoinRoute)],
  exports: [RouterModule],
})
export class SongArtistJoinRoutingModule {}
