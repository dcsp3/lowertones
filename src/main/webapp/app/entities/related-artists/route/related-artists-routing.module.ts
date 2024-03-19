import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RelatedArtistsComponent } from '../list/related-artists.component';
import { RelatedArtistsDetailComponent } from '../detail/related-artists-detail.component';
import { RelatedArtistsUpdateComponent } from '../update/related-artists-update.component';
import { RelatedArtistsRoutingResolveService } from './related-artists-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const relatedArtistsRoute: Routes = [
  {
    path: '',
    component: RelatedArtistsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RelatedArtistsDetailComponent,
    resolve: {
      relatedArtists: RelatedArtistsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RelatedArtistsUpdateComponent,
    resolve: {
      relatedArtists: RelatedArtistsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RelatedArtistsUpdateComponent,
    resolve: {
      relatedArtists: RelatedArtistsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(relatedArtistsRoute)],
  exports: [RouterModule],
})
export class RelatedArtistsRoutingModule {}
