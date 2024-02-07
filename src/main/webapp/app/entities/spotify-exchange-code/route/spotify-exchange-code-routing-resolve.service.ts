import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISpotifyExchangeCode } from '../spotify-exchange-code.model';
import { SpotifyExchangeCodeService } from '../service/spotify-exchange-code.service';

@Injectable({ providedIn: 'root' })
export class SpotifyExchangeCodeRoutingResolveService implements Resolve<ISpotifyExchangeCode | null> {
  constructor(protected service: SpotifyExchangeCodeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISpotifyExchangeCode | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((spotifyExchangeCode: HttpResponse<ISpotifyExchangeCode>) => {
          if (spotifyExchangeCode.body) {
            return of(spotifyExchangeCode.body);
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
