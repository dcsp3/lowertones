import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PreferencesComponent } from './preferences.component';

export const preferencesRoute: Route = {
  path: 'preferences',
  component: PreferencesComponent,
  data: {
    pageTitle: 'Preferences',
  },
  canActivate: [UserRouteAccessService],
};
