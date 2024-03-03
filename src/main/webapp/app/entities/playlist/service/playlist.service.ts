import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlaylist, NewPlaylist } from '../playlist.model';

export type PartialUpdatePlaylist = Partial<IPlaylist> & Pick<IPlaylist, 'id'>;

type RestOf<T extends IPlaylist | NewPlaylist> = Omit<T, 'dateAddedToDB' | 'dateLastModified'> & {
  dateAddedToDB?: string | null;
  dateLastModified?: string | null;
};

export type RestPlaylist = RestOf<IPlaylist>;

export type NewRestPlaylist = RestOf<NewPlaylist>;

export type PartialUpdateRestPlaylist = RestOf<PartialUpdatePlaylist>;

export type EntityResponseType = HttpResponse<IPlaylist>;
export type EntityArrayResponseType = HttpResponse<IPlaylist[]>;

@Injectable({ providedIn: 'root' })
export class PlaylistService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/playlists');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(playlist: NewPlaylist): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(playlist);
    return this.http
      .post<RestPlaylist>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(playlist: IPlaylist): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(playlist);
    return this.http
      .put<RestPlaylist>(`${this.resourceUrl}/${this.getPlaylistIdentifier(playlist)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(playlist: PartialUpdatePlaylist): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(playlist);
    return this.http
      .patch<RestPlaylist>(`${this.resourceUrl}/${this.getPlaylistIdentifier(playlist)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPlaylist>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPlaylist[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPlaylistIdentifier(playlist: Pick<IPlaylist, 'id'>): number {
    return playlist.id;
  }

  comparePlaylist(o1: Pick<IPlaylist, 'id'> | null, o2: Pick<IPlaylist, 'id'> | null): boolean {
    return o1 && o2 ? this.getPlaylistIdentifier(o1) === this.getPlaylistIdentifier(o2) : o1 === o2;
  }

  addPlaylistToCollectionIfMissing<Type extends Pick<IPlaylist, 'id'>>(
    playlistCollection: Type[],
    ...playlistsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const playlists: Type[] = playlistsToCheck.filter(isPresent);
    if (playlists.length > 0) {
      const playlistCollectionIdentifiers = playlistCollection.map(playlistItem => this.getPlaylistIdentifier(playlistItem)!);
      const playlistsToAdd = playlists.filter(playlistItem => {
        const playlistIdentifier = this.getPlaylistIdentifier(playlistItem);
        if (playlistCollectionIdentifiers.includes(playlistIdentifier)) {
          return false;
        }
        playlistCollectionIdentifiers.push(playlistIdentifier);
        return true;
      });
      return [...playlistsToAdd, ...playlistCollection];
    }
    return playlistCollection;
  }

  protected convertDateFromClient<T extends IPlaylist | NewPlaylist | PartialUpdatePlaylist>(playlist: T): RestOf<T> {
    return {
      ...playlist,
      dateAddedToDB: playlist.dateAddedToDB?.format(DATE_FORMAT) ?? null,
      dateLastModified: playlist.dateLastModified?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restPlaylist: RestPlaylist): IPlaylist {
    return {
      ...restPlaylist,
      dateAddedToDB: restPlaylist.dateAddedToDB ? dayjs(restPlaylist.dateAddedToDB) : undefined,
      dateLastModified: restPlaylist.dateLastModified ? dayjs(restPlaylist.dateLastModified) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPlaylist>): HttpResponse<IPlaylist> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPlaylist[]>): HttpResponse<IPlaylist[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
