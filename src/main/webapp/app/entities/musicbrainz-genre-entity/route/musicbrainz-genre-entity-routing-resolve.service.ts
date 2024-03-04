import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMusicbrainzGenreEntity } from '../musicbrainz-genre-entity.model';
import { MusicbrainzGenreEntityService } from '../service/musicbrainz-genre-entity.service';

@Injectable({ providedIn: 'root' })
export class MusicbrainzGenreEntityRoutingResolveService implements Resolve<IMusicbrainzGenreEntity | null> {
  constructor(protected service: MusicbrainzGenreEntityService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMusicbrainzGenreEntity | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((musicbrainzGenreEntity: HttpResponse<IMusicbrainzGenreEntity>) => {
          if (musicbrainzGenreEntity.body) {
            return of(musicbrainzGenreEntity.body);
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
