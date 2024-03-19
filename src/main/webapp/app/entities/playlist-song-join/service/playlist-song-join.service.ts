import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlaylistSongJoin, NewPlaylistSongJoin } from '../playlist-song-join.model';

export type PartialUpdatePlaylistSongJoin = Partial<IPlaylistSongJoin> & Pick<IPlaylistSongJoin, 'id'>;

type RestOf<T extends IPlaylistSongJoin | NewPlaylistSongJoin> = Omit<T, 'songDateAdded'> & {
  songDateAdded?: string | null;
};

export type RestPlaylistSongJoin = RestOf<IPlaylistSongJoin>;

export type NewRestPlaylistSongJoin = RestOf<NewPlaylistSongJoin>;

export type PartialUpdateRestPlaylistSongJoin = RestOf<PartialUpdatePlaylistSongJoin>;

export type EntityResponseType = HttpResponse<IPlaylistSongJoin>;
export type EntityArrayResponseType = HttpResponse<IPlaylistSongJoin[]>;

@Injectable({ providedIn: 'root' })
export class PlaylistSongJoinService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/playlist-song-joins');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(playlistSongJoin: NewPlaylistSongJoin): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(playlistSongJoin);
    return this.http
      .post<RestPlaylistSongJoin>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(playlistSongJoin: IPlaylistSongJoin): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(playlistSongJoin);
    return this.http
      .put<RestPlaylistSongJoin>(`${this.resourceUrl}/${this.getPlaylistSongJoinIdentifier(playlistSongJoin)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(playlistSongJoin: PartialUpdatePlaylistSongJoin): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(playlistSongJoin);
    return this.http
      .patch<RestPlaylistSongJoin>(`${this.resourceUrl}/${this.getPlaylistSongJoinIdentifier(playlistSongJoin)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPlaylistSongJoin>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPlaylistSongJoin[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPlaylistSongJoinIdentifier(playlistSongJoin: Pick<IPlaylistSongJoin, 'id'>): number {
    return playlistSongJoin.id;
  }

  comparePlaylistSongJoin(o1: Pick<IPlaylistSongJoin, 'id'> | null, o2: Pick<IPlaylistSongJoin, 'id'> | null): boolean {
    return o1 && o2 ? this.getPlaylistSongJoinIdentifier(o1) === this.getPlaylistSongJoinIdentifier(o2) : o1 === o2;
  }

  addPlaylistSongJoinToCollectionIfMissing<Type extends Pick<IPlaylistSongJoin, 'id'>>(
    playlistSongJoinCollection: Type[],
    ...playlistSongJoinsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const playlistSongJoins: Type[] = playlistSongJoinsToCheck.filter(isPresent);
    if (playlistSongJoins.length > 0) {
      const playlistSongJoinCollectionIdentifiers = playlistSongJoinCollection.map(
        playlistSongJoinItem => this.getPlaylistSongJoinIdentifier(playlistSongJoinItem)!
      );
      const playlistSongJoinsToAdd = playlistSongJoins.filter(playlistSongJoinItem => {
        const playlistSongJoinIdentifier = this.getPlaylistSongJoinIdentifier(playlistSongJoinItem);
        if (playlistSongJoinCollectionIdentifiers.includes(playlistSongJoinIdentifier)) {
          return false;
        }
        playlistSongJoinCollectionIdentifiers.push(playlistSongJoinIdentifier);
        return true;
      });
      return [...playlistSongJoinsToAdd, ...playlistSongJoinCollection];
    }
    return playlistSongJoinCollection;
  }

  protected convertDateFromClient<T extends IPlaylistSongJoin | NewPlaylistSongJoin | PartialUpdatePlaylistSongJoin>(
    playlistSongJoin: T
  ): RestOf<T> {
    return {
      ...playlistSongJoin,
      songDateAdded: playlistSongJoin.songDateAdded?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restPlaylistSongJoin: RestPlaylistSongJoin): IPlaylistSongJoin {
    return {
      ...restPlaylistSongJoin,
      songDateAdded: restPlaylistSongJoin.songDateAdded ? dayjs(restPlaylistSongJoin.songDateAdded) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPlaylistSongJoin>): HttpResponse<IPlaylistSongJoin> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPlaylistSongJoin[]>): HttpResponse<IPlaylistSongJoin[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
