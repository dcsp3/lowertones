import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMusicbrainzGenreEntity, NewMusicbrainzGenreEntity } from '../musicbrainz-genre-entity.model';

export type PartialUpdateMusicbrainzGenreEntity = Partial<IMusicbrainzGenreEntity> & Pick<IMusicbrainzGenreEntity, 'id'>;

export type EntityResponseType = HttpResponse<IMusicbrainzGenreEntity>;
export type EntityArrayResponseType = HttpResponse<IMusicbrainzGenreEntity[]>;

@Injectable({ providedIn: 'root' })
export class MusicbrainzGenreEntityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/musicbrainz-genre-entities');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(musicbrainzGenreEntity: NewMusicbrainzGenreEntity): Observable<EntityResponseType> {
    return this.http.post<IMusicbrainzGenreEntity>(this.resourceUrl, musicbrainzGenreEntity, { observe: 'response' });
  }

  update(musicbrainzGenreEntity: IMusicbrainzGenreEntity): Observable<EntityResponseType> {
    return this.http.put<IMusicbrainzGenreEntity>(
      `${this.resourceUrl}/${this.getMusicbrainzGenreEntityIdentifier(musicbrainzGenreEntity)}`,
      musicbrainzGenreEntity,
      { observe: 'response' }
    );
  }

  partialUpdate(musicbrainzGenreEntity: PartialUpdateMusicbrainzGenreEntity): Observable<EntityResponseType> {
    return this.http.patch<IMusicbrainzGenreEntity>(
      `${this.resourceUrl}/${this.getMusicbrainzGenreEntityIdentifier(musicbrainzGenreEntity)}`,
      musicbrainzGenreEntity,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMusicbrainzGenreEntity>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMusicbrainzGenreEntity[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMusicbrainzGenreEntityIdentifier(musicbrainzGenreEntity: Pick<IMusicbrainzGenreEntity, 'id'>): number {
    return musicbrainzGenreEntity.id;
  }

  compareMusicbrainzGenreEntity(o1: Pick<IMusicbrainzGenreEntity, 'id'> | null, o2: Pick<IMusicbrainzGenreEntity, 'id'> | null): boolean {
    return o1 && o2 ? this.getMusicbrainzGenreEntityIdentifier(o1) === this.getMusicbrainzGenreEntityIdentifier(o2) : o1 === o2;
  }

  addMusicbrainzGenreEntityToCollectionIfMissing<Type extends Pick<IMusicbrainzGenreEntity, 'id'>>(
    musicbrainzGenreEntityCollection: Type[],
    ...musicbrainzGenreEntitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const musicbrainzGenreEntities: Type[] = musicbrainzGenreEntitiesToCheck.filter(isPresent);
    if (musicbrainzGenreEntities.length > 0) {
      const musicbrainzGenreEntityCollectionIdentifiers = musicbrainzGenreEntityCollection.map(
        musicbrainzGenreEntityItem => this.getMusicbrainzGenreEntityIdentifier(musicbrainzGenreEntityItem)!
      );
      const musicbrainzGenreEntitiesToAdd = musicbrainzGenreEntities.filter(musicbrainzGenreEntityItem => {
        const musicbrainzGenreEntityIdentifier = this.getMusicbrainzGenreEntityIdentifier(musicbrainzGenreEntityItem);
        if (musicbrainzGenreEntityCollectionIdentifiers.includes(musicbrainzGenreEntityIdentifier)) {
          return false;
        }
        musicbrainzGenreEntityCollectionIdentifiers.push(musicbrainzGenreEntityIdentifier);
        return true;
      });
      return [...musicbrainzGenreEntitiesToAdd, ...musicbrainzGenreEntityCollection];
    }
    return musicbrainzGenreEntityCollection;
  }
}
