import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISongArtistJoin, NewSongArtistJoin } from '../song-artist-join.model';

export type PartialUpdateSongArtistJoin = Partial<ISongArtistJoin> & Pick<ISongArtistJoin, 'id'>;

export type EntityResponseType = HttpResponse<ISongArtistJoin>;
export type EntityArrayResponseType = HttpResponse<ISongArtistJoin[]>;

@Injectable({ providedIn: 'root' })
export class SongArtistJoinService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/song-artist-joins');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(songArtistJoin: NewSongArtistJoin): Observable<EntityResponseType> {
    return this.http.post<ISongArtistJoin>(this.resourceUrl, songArtistJoin, { observe: 'response' });
  }

  update(songArtistJoin: ISongArtistJoin): Observable<EntityResponseType> {
    return this.http.put<ISongArtistJoin>(`${this.resourceUrl}/${this.getSongArtistJoinIdentifier(songArtistJoin)}`, songArtistJoin, {
      observe: 'response',
    });
  }

  partialUpdate(songArtistJoin: PartialUpdateSongArtistJoin): Observable<EntityResponseType> {
    return this.http.patch<ISongArtistJoin>(`${this.resourceUrl}/${this.getSongArtistJoinIdentifier(songArtistJoin)}`, songArtistJoin, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISongArtistJoin>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISongArtistJoin[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSongArtistJoinIdentifier(songArtistJoin: Pick<ISongArtistJoin, 'id'>): number {
    return songArtistJoin.id;
  }

  compareSongArtistJoin(o1: Pick<ISongArtistJoin, 'id'> | null, o2: Pick<ISongArtistJoin, 'id'> | null): boolean {
    return o1 && o2 ? this.getSongArtistJoinIdentifier(o1) === this.getSongArtistJoinIdentifier(o2) : o1 === o2;
  }

  addSongArtistJoinToCollectionIfMissing<Type extends Pick<ISongArtistJoin, 'id'>>(
    songArtistJoinCollection: Type[],
    ...songArtistJoinsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const songArtistJoins: Type[] = songArtistJoinsToCheck.filter(isPresent);
    if (songArtistJoins.length > 0) {
      const songArtistJoinCollectionIdentifiers = songArtistJoinCollection.map(
        songArtistJoinItem => this.getSongArtistJoinIdentifier(songArtistJoinItem)!
      );
      const songArtistJoinsToAdd = songArtistJoins.filter(songArtistJoinItem => {
        const songArtistJoinIdentifier = this.getSongArtistJoinIdentifier(songArtistJoinItem);
        if (songArtistJoinCollectionIdentifiers.includes(songArtistJoinIdentifier)) {
          return false;
        }
        songArtistJoinCollectionIdentifiers.push(songArtistJoinIdentifier);
        return true;
      });
      return [...songArtistJoinsToAdd, ...songArtistJoinCollection];
    }
    return songArtistJoinCollection;
  }
}
