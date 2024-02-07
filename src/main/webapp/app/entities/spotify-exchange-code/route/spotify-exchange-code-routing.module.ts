import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SpotifyExchangeCodeComponent } from '../list/spotify-exchange-code.component';
import { SpotifyExchangeCodeDetailComponent } from '../detail/spotify-exchange-code-detail.component';
import { SpotifyExchangeCodeUpdateComponent } from '../update/spotify-exchange-code-update.component';
import { SpotifyExchangeCodeRoutingResolveService } from './spotify-exchange-code-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const spotifyExchangeCodeRoute: Routes = [
  {
    path: '',
    component: SpotifyExchangeCodeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SpotifyExchangeCodeDetailComponent,
    resolve: {
      spotifyExchangeCode: SpotifyExchangeCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SpotifyExchangeCodeUpdateComponent,
    resolve: {
      spotifyExchangeCode: SpotifyExchangeCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SpotifyExchangeCodeUpdateComponent,
    resolve: {
      spotifyExchangeCode: SpotifyExchangeCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(spotifyExchangeCodeRoute)],
  exports: [RouterModule],
})
export class SpotifyExchangeCodeRoutingModule {}
