import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPlaylistSongJoin } from '../playlist-song-join.model';
import { PlaylistSongJoinService } from '../service/playlist-song-join.service';

@Injectable({ providedIn: 'root' })
export class PlaylistSongJoinRoutingResolveService implements Resolve<IPlaylistSongJoin | null> {
  constructor(protected service: PlaylistSongJoinService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPlaylistSongJoin | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((playlistSongJoin: HttpResponse<IPlaylistSongJoin>) => {
          if (playlistSongJoin.body) {
            return of(playlistSongJoin.body);
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
