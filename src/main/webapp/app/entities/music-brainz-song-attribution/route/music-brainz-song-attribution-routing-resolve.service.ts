import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMusicBrainzSongAttribution } from '../music-brainz-song-attribution.model';
import { MusicBrainzSongAttributionService } from '../service/music-brainz-song-attribution.service';

@Injectable({ providedIn: 'root' })
export class MusicBrainzSongAttributionRoutingResolveService implements Resolve<IMusicBrainzSongAttribution | null> {
  constructor(protected service: MusicBrainzSongAttributionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMusicBrainzSongAttribution | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((musicBrainzSongAttribution: HttpResponse<IMusicBrainzSongAttribution>) => {
          if (musicBrainzSongAttribution.body) {
            return of(musicBrainzSongAttribution.body);
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
