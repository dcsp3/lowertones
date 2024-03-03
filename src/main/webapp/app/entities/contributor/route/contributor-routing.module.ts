import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContributorComponent } from '../list/contributor.component';
import { ContributorDetailComponent } from '../detail/contributor-detail.component';
import { ContributorUpdateComponent } from '../update/contributor-update.component';
import { ContributorRoutingResolveService } from './contributor-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const contributorRoute: Routes = [
  {
    path: '',
    component: ContributorComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContributorDetailComponent,
    resolve: {
      contributor: ContributorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContributorUpdateComponent,
    resolve: {
      contributor: ContributorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContributorUpdateComponent,
    resolve: {
      contributor: ContributorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contributorRoute)],
  exports: [RouterModule],
})
export class ContributorRoutingModule {}
