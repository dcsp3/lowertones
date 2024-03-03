import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MusicBrainzSongAttributionComponent } from '../list/music-brainz-song-attribution.component';
import { MusicBrainzSongAttributionDetailComponent } from '../detail/music-brainz-song-attribution-detail.component';
import { MusicBrainzSongAttributionUpdateComponent } from '../update/music-brainz-song-attribution-update.component';
import { MusicBrainzSongAttributionRoutingResolveService } from './music-brainz-song-attribution-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const musicBrainzSongAttributionRoute: Routes = [
  {
    path: '',
    component: MusicBrainzSongAttributionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MusicBrainzSongAttributionDetailComponent,
    resolve: {
      musicBrainzSongAttribution: MusicBrainzSongAttributionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MusicBrainzSongAttributionUpdateComponent,
    resolve: {
      musicBrainzSongAttribution: MusicBrainzSongAttributionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MusicBrainzSongAttributionUpdateComponent,
    resolve: {
      musicBrainzSongAttribution: MusicBrainzSongAttributionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(musicBrainzSongAttributionRoute)],
  exports: [RouterModule],
})
export class MusicBrainzSongAttributionRoutingModule {}
