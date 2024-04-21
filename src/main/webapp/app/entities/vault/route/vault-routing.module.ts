import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VaultComponent } from '../list/vault.component';
import { VaultDetailComponent } from '../detail/vault-detail.component';
import { VaultUpdateComponent } from '../update/vault-update.component';
import { VaultRoutingResolveService } from './vault-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const vaultRoute: Routes = [
  {
    path: '',
    component: VaultComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VaultDetailComponent,
    resolve: {
      vault: VaultRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VaultUpdateComponent,
    resolve: {
      vault: VaultRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VaultUpdateComponent,
    resolve: {
      vault: VaultRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(vaultRoute)],
  exports: [RouterModule],
})
export class VaultRoutingModule {}
