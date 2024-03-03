import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContributor } from '../contributor.model';
import { ContributorService } from '../service/contributor.service';

@Injectable({ providedIn: 'root' })
export class ContributorRoutingResolveService implements Resolve<IContributor | null> {
  constructor(protected service: ContributorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContributor | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contributor: HttpResponse<IContributor>) => {
          if (contributor.body) {
            return of(contributor.body);
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
