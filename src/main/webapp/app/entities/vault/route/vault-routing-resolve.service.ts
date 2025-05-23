import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVault } from '../vault.model';
import { VaultService } from '../service/vault.service';

@Injectable({ providedIn: 'root' })
export class VaultRoutingResolveService implements Resolve<IVault | null> {
  constructor(protected service: VaultService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVault | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((vault: HttpResponse<IVault>) => {
          if (vault.body) {
            return of(vault.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
