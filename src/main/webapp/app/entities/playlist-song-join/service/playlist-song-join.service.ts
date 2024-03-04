import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlaylistSongJoin, NewPlaylistSongJoin } from '../playlist-song-join.model';

export type PartialUpdatePlaylistSongJoin = Partial<IPlaylistSongJoin> & Pick<IPlaylistSongJoin, 'id'>;

export type EntityResponseType = HttpResponse<IPlaylistSongJoin>;
export type EntityArrayResponseType = HttpResponse<IPlaylistSongJoin[]>;

@Injectable({ providedIn: 'root' })
export class PlaylistSongJoinService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/playlist-song-joins');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(playlistSongJoin: NewPlaylistSongJoin): Observable<EntityResponseType> {
    return this.http.post<IPlaylistSongJoin>(this.resourceUrl, playlistSongJoin, { observe: 'response' });
  }

  update(playlistSongJoin: IPlaylistSongJoin): Observable<EntityResponseType> {
    return this.http.put<IPlaylistSongJoin>(
      `${this.resourceUrl}/${this.getPlaylistSongJoinIdentifier(playlistSongJoin)}`,
      playlistSongJoin,
      { observe: 'response' }
    );
  }

  partialUpdate(playlistSongJoin: PartialUpdatePlaylistSongJoin): Observable<EntityResponseType> {
    return this.http.patch<IPlaylistSongJoin>(
      `${this.resourceUrl}/${this.getPlaylistSongJoinIdentifier(playlistSongJoin)}`,
      playlistSongJoin,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPlaylistSongJoin>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlaylistSongJoin[]>(this.resourceUrl, { params: options, observe: 'response' });
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
}
