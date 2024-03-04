import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISongArtistJoin } from '../song-artist-join.model';
import { SongArtistJoinService } from '../service/song-artist-join.service';

@Injectable({ providedIn: 'root' })
export class SongArtistJoinRoutingResolveService implements Resolve<ISongArtistJoin | null> {
  constructor(protected service: SongArtistJoinService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISongArtistJoin | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((songArtistJoin: HttpResponse<ISongArtistJoin>) => {
          if (songArtistJoin.body) {
            return of(songArtistJoin.body);
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
