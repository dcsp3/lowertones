import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { errorRoute } from './layouts/error/error.route';
import { navbarRoute } from './layouts/navbar/navbar.route';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import { Authority } from 'app/config/authority.constants';
import { RecappedComponent } from 'app/recapped/recapped.component';
import { TableviewComponent } from 'app/tableview/tableview.component';
import { NetworkComponent } from './network/network.component';
import { VisualisationsComponent } from './visualisations/visualisations.component';
import { GDPRComponent } from './gdpr/gdpr.component';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

@NgModule({
  imports: [
    RouterModule.forRoot(
      [
        {
          path: 'admin',
          data: {
            authorities: [Authority.ADMIN],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () => import('./admin/admin-routing.module').then(m => m.AdminRoutingModule),
        },
        {
          path: 'account',
          loadChildren: () => import('./account/account.module').then(m => m.AccountModule),
        },
        {
          path: 'login',
          loadChildren: () => import('./login/login.module').then(m => m.LoginModule),
        },
        {
          path: '',
          loadChildren: () => import(`./entities/entity-routing.module`).then(m => m.EntityRoutingModule),
        },
        {
          path: 'recapped',
          component: RecappedComponent,
          data: {
            pageTitle: 'Recapped',
            authorities: [Authority.ADMIN, Authority.USER],
          },
          canActivate: [UserRouteAccessService],
        },
        {
          path: 'tableview',
          component: TableviewComponent,
          data: {
            pageTitle: 'Track Organizer',
            authorities: [Authority.ADMIN, Authority.USER],
          },
          canActivate: [UserRouteAccessService],
        },
        {
          path: 'your-network',
          component: NetworkComponent,
          data: {
            pageTitle: 'Your Network',
            authorities: [Authority.ADMIN, Authority.USER],
          },
          canActivate: [UserRouteAccessService],
        },
        {
          path: 'visualisations',
          component: VisualisationsComponent,
          data: {
            pageTitle: 'Visualisations',
            authorities: [Authority.ADMIN, Authority.USER],
          },
          canActivate: [UserRouteAccessService],
        },
        {
          path: 'gdpr-policy',
          component: GDPRComponent,
          data: {
            pageTitle: 'GDPR Policy',
          },
        },
        navbarRoute,
        ...errorRoute,
      ],
      { enableTracing: DEBUG_INFO_ENABLED }
    ),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
