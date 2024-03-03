import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MusicbrainzGenreEntityComponent } from '../list/musicbrainz-genre-entity.component';
import { MusicbrainzGenreEntityDetailComponent } from '../detail/musicbrainz-genre-entity-detail.component';
import { MusicbrainzGenreEntityUpdateComponent } from '../update/musicbrainz-genre-entity-update.component';
import { MusicbrainzGenreEntityRoutingResolveService } from './musicbrainz-genre-entity-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const musicbrainzGenreEntityRoute: Routes = [
  {
    path: '',
    component: MusicbrainzGenreEntityComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MusicbrainzGenreEntityDetailComponent,
    resolve: {
      musicbrainzGenreEntity: MusicbrainzGenreEntityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MusicbrainzGenreEntityUpdateComponent,
    resolve: {
      musicbrainzGenreEntity: MusicbrainzGenreEntityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MusicbrainzGenreEntityUpdateComponent,
    resolve: {
      musicbrainzGenreEntity: MusicbrainzGenreEntityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(musicbrainzGenreEntityRoute)],
  exports: [RouterModule],
})
export class MusicbrainzGenreEntityRoutingModule {}
