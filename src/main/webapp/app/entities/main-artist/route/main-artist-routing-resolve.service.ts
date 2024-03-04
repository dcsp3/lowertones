import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMainArtist } from '../main-artist.model';
import { MainArtistService } from '../service/main-artist.service';

@Injectable({ providedIn: 'root' })
export class MainArtistRoutingResolveService implements Resolve<IMainArtist | null> {
  constructor(protected service: MainArtistService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMainArtist | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((mainArtist: HttpResponse<IMainArtist>) => {
          if (mainArtist.body) {
            return of(mainArtist.body);
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
