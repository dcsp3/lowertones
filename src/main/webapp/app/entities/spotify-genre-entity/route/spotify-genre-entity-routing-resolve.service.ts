import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISpotifyGenreEntity } from '../spotify-genre-entity.model';
import { SpotifyGenreEntityService } from '../service/spotify-genre-entity.service';

@Injectable({ providedIn: 'root' })
export class SpotifyGenreEntityRoutingResolveService implements Resolve<ISpotifyGenreEntity | null> {
  constructor(protected service: SpotifyGenreEntityService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISpotifyGenreEntity | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((spotifyGenreEntity: HttpResponse<ISpotifyGenreEntity>) => {
          if (spotifyGenreEntity.body) {
            return of(spotifyGenreEntity.body);
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
