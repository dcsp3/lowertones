import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRelatedArtists } from '../related-artists.model';
import { RelatedArtistsService } from '../service/related-artists.service';

@Injectable({ providedIn: 'root' })
export class RelatedArtistsRoutingResolveService implements Resolve<IRelatedArtists | null> {
  constructor(protected service: RelatedArtistsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRelatedArtists | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((relatedArtists: HttpResponse<IRelatedArtists>) => {
          if (relatedArtists.body) {
            return of(relatedArtists.body);
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
