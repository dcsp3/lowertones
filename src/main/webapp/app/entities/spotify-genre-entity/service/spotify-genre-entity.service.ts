import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISpotifyGenreEntity, NewSpotifyGenreEntity } from '../spotify-genre-entity.model';

export type PartialUpdateSpotifyGenreEntity = Partial<ISpotifyGenreEntity> & Pick<ISpotifyGenreEntity, 'id'>;

export type EntityResponseType = HttpResponse<ISpotifyGenreEntity>;
export type EntityArrayResponseType = HttpResponse<ISpotifyGenreEntity[]>;

@Injectable({ providedIn: 'root' })
export class SpotifyGenreEntityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/spotify-genre-entities');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(spotifyGenreEntity: NewSpotifyGenreEntity): Observable<EntityResponseType> {
    return this.http.post<ISpotifyGenreEntity>(this.resourceUrl, spotifyGenreEntity, { observe: 'response' });
  }

  update(spotifyGenreEntity: ISpotifyGenreEntity): Observable<EntityResponseType> {
    return this.http.put<ISpotifyGenreEntity>(
      `${this.resourceUrl}/${this.getSpotifyGenreEntityIdentifier(spotifyGenreEntity)}`,
      spotifyGenreEntity,
      { observe: 'response' }
    );
  }

  partialUpdate(spotifyGenreEntity: PartialUpdateSpotifyGenreEntity): Observable<EntityResponseType> {
    return this.http.patch<ISpotifyGenreEntity>(
      `${this.resourceUrl}/${this.getSpotifyGenreEntityIdentifier(spotifyGenreEntity)}`,
      spotifyGenreEntity,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISpotifyGenreEntity>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISpotifyGenreEntity[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSpotifyGenreEntityIdentifier(spotifyGenreEntity: Pick<ISpotifyGenreEntity, 'id'>): number {
    return spotifyGenreEntity.id;
  }

  compareSpotifyGenreEntity(o1: Pick<ISpotifyGenreEntity, 'id'> | null, o2: Pick<ISpotifyGenreEntity, 'id'> | null): boolean {
    return o1 && o2 ? this.getSpotifyGenreEntityIdentifier(o1) === this.getSpotifyGenreEntityIdentifier(o2) : o1 === o2;
  }

  addSpotifyGenreEntityToCollectionIfMissing<Type extends Pick<ISpotifyGenreEntity, 'id'>>(
    spotifyGenreEntityCollection: Type[],
    ...spotifyGenreEntitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const spotifyGenreEntities: Type[] = spotifyGenreEntitiesToCheck.filter(isPresent);
    if (spotifyGenreEntities.length > 0) {
      const spotifyGenreEntityCollectionIdentifiers = spotifyGenreEntityCollection.map(
        spotifyGenreEntityItem => this.getSpotifyGenreEntityIdentifier(spotifyGenreEntityItem)!
      );
      const spotifyGenreEntitiesToAdd = spotifyGenreEntities.filter(spotifyGenreEntityItem => {
        const spotifyGenreEntityIdentifier = this.getSpotifyGenreEntityIdentifier(spotifyGenreEntityItem);
        if (spotifyGenreEntityCollectionIdentifiers.includes(spotifyGenreEntityIdentifier)) {
          return false;
        }
        spotifyGenreEntityCollectionIdentifiers.push(spotifyGenreEntityIdentifier);
        return true;
      });
      return [...spotifyGenreEntitiesToAdd, ...spotifyGenreEntityCollection];
    }
    return spotifyGenreEntityCollection;
  }
}
