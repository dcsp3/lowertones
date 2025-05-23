import { Routes } from '@angular/router';

import { activateRoute } from './activate/activate.route';
import { passwordRoute } from './password/password.route';
import { passwordResetFinishRoute } from './password-reset/finish/password-reset-finish.route';
import { passwordResetInitRoute } from './password-reset/init/password-reset-init.route';
import { preferencesRoute } from './preferences/preferences.route';
import { registerRoute } from './register/register.route';

const ACCOUNT_ROUTES = [activateRoute, passwordRoute, passwordResetFinishRoute, passwordResetInitRoute, preferencesRoute, registerRoute];

export const accountState: Routes = [
  {
    path: '',
    children: ACCOUNT_ROUTES,
  },
];
