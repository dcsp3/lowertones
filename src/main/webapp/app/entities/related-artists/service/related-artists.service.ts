import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRelatedArtists, NewRelatedArtists } from '../related-artists.model';

export type PartialUpdateRelatedArtists = Partial<IRelatedArtists> & Pick<IRelatedArtists, 'id'>;

export type EntityResponseType = HttpResponse<IRelatedArtists>;
export type EntityArrayResponseType = HttpResponse<IRelatedArtists[]>;

@Injectable({ providedIn: 'root' })
export class RelatedArtistsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/related-artists');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(relatedArtists: NewRelatedArtists): Observable<EntityResponseType> {
    return this.http.post<IRelatedArtists>(this.resourceUrl, relatedArtists, { observe: 'response' });
  }

  update(relatedArtists: IRelatedArtists): Observable<EntityResponseType> {
    return this.http.put<IRelatedArtists>(`${this.resourceUrl}/${this.getRelatedArtistsIdentifier(relatedArtists)}`, relatedArtists, {
      observe: 'response',
    });
  }

  partialUpdate(relatedArtists: PartialUpdateRelatedArtists): Observable<EntityResponseType> {
    return this.http.patch<IRelatedArtists>(`${this.resourceUrl}/${this.getRelatedArtistsIdentifier(relatedArtists)}`, relatedArtists, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRelatedArtists>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRelatedArtists[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRelatedArtistsIdentifier(relatedArtists: Pick<IRelatedArtists, 'id'>): number {
    return relatedArtists.id;
  }

  compareRelatedArtists(o1: Pick<IRelatedArtists, 'id'> | null, o2: Pick<IRelatedArtists, 'id'> | null): boolean {
    return o1 && o2 ? this.getRelatedArtistsIdentifier(o1) === this.getRelatedArtistsIdentifier(o2) : o1 === o2;
  }

  addRelatedArtistsToCollectionIfMissing<Type extends Pick<IRelatedArtists, 'id'>>(
    relatedArtistsCollection: Type[],
    ...relatedArtistsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const relatedArtists: Type[] = relatedArtistsToCheck.filter(isPresent);
    if (relatedArtists.length > 0) {
      const relatedArtistsCollectionIdentifiers = relatedArtistsCollection.map(
        relatedArtistsItem => this.getRelatedArtistsIdentifier(relatedArtistsItem)!
      );
      const relatedArtistsToAdd = relatedArtists.filter(relatedArtistsItem => {
        const relatedArtistsIdentifier = this.getRelatedArtistsIdentifier(relatedArtistsItem);
        if (relatedArtistsCollectionIdentifiers.includes(relatedArtistsIdentifier)) {
          return false;
        }
        relatedArtistsCollectionIdentifiers.push(relatedArtistsIdentifier);
        return true;
      });
      return [...relatedArtistsToAdd, ...relatedArtistsCollection];
    }
    return relatedArtistsCollection;
  }
}
