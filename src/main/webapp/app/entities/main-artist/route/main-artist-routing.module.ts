import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MainArtistComponent } from '../list/main-artist.component';
import { MainArtistDetailComponent } from '../detail/main-artist-detail.component';
import { MainArtistUpdateComponent } from '../update/main-artist-update.component';
import { MainArtistRoutingResolveService } from './main-artist-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const mainArtistRoute: Routes = [
  {
    path: '',
    component: MainArtistComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MainArtistDetailComponent,
    resolve: {
      mainArtist: MainArtistRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MainArtistUpdateComponent,
    resolve: {
      mainArtist: MainArtistRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MainArtistUpdateComponent,
    resolve: {
      mainArtist: MainArtistRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(mainArtistRoute)],
  exports: [RouterModule],
})
export class MainArtistRoutingModule {}
