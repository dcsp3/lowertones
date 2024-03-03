import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SpotifyGenreEntityComponent } from '../list/spotify-genre-entity.component';
import { SpotifyGenreEntityDetailComponent } from '../detail/spotify-genre-entity-detail.component';
import { SpotifyGenreEntityUpdateComponent } from '../update/spotify-genre-entity-update.component';
import { SpotifyGenreEntityRoutingResolveService } from './spotify-genre-entity-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const spotifyGenreEntityRoute: Routes = [
  {
    path: '',
    component: SpotifyGenreEntityComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SpotifyGenreEntityDetailComponent,
    resolve: {
      spotifyGenreEntity: SpotifyGenreEntityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SpotifyGenreEntityUpdateComponent,
    resolve: {
      spotifyGenreEntity: SpotifyGenreEntityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SpotifyGenreEntityUpdateComponent,
    resolve: {
      spotifyGenreEntity: SpotifyGenreEntityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(spotifyGenreEntityRoute)],
  exports: [RouterModule],
})
export class SpotifyGenreEntityRoutingModule {}
