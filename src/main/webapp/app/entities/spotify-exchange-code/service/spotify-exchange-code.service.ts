import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISpotifyExchangeCode, NewSpotifyExchangeCode } from '../spotify-exchange-code.model';

export type PartialUpdateSpotifyExchangeCode = Partial<ISpotifyExchangeCode> & Pick<ISpotifyExchangeCode, 'id'>;

export type EntityResponseType = HttpResponse<ISpotifyExchangeCode>;
export type EntityArrayResponseType = HttpResponse<ISpotifyExchangeCode[]>;

@Injectable({ providedIn: 'root' })
export class SpotifyExchangeCodeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/spotify-exchange-codes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(spotifyExchangeCode: NewSpotifyExchangeCode): Observable<EntityResponseType> {
    return this.http.post<ISpotifyExchangeCode>(this.resourceUrl, spotifyExchangeCode, { observe: 'response' });
  }

  update(spotifyExchangeCode: ISpotifyExchangeCode): Observable<EntityResponseType> {
    return this.http.put<ISpotifyExchangeCode>(
      `${this.resourceUrl}/${this.getSpotifyExchangeCodeIdentifier(spotifyExchangeCode)}`,
      spotifyExchangeCode,
      { observe: 'response' }
    );
  }

  partialUpdate(spotifyExchangeCode: PartialUpdateSpotifyExchangeCode): Observable<EntityResponseType> {
    return this.http.patch<ISpotifyExchangeCode>(
      `${this.resourceUrl}/${this.getSpotifyExchangeCodeIdentifier(spotifyExchangeCode)}`,
      spotifyExchangeCode,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISpotifyExchangeCode>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISpotifyExchangeCode[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSpotifyExchangeCodeIdentifier(spotifyExchangeCode: Pick<ISpotifyExchangeCode, 'id'>): number {
    return spotifyExchangeCode.id;
  }

  compareSpotifyExchangeCode(o1: Pick<ISpotifyExchangeCode, 'id'> | null, o2: Pick<ISpotifyExchangeCode, 'id'> | null): boolean {
    return o1 && o2 ? this.getSpotifyExchangeCodeIdentifier(o1) === this.getSpotifyExchangeCodeIdentifier(o2) : o1 === o2;
  }

  addSpotifyExchangeCodeToCollectionIfMissing<Type extends Pick<ISpotifyExchangeCode, 'id'>>(
    spotifyExchangeCodeCollection: Type[],
    ...spotifyExchangeCodesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const spotifyExchangeCodes: Type[] = spotifyExchangeCodesToCheck.filter(isPresent);
    if (spotifyExchangeCodes.length > 0) {
      const spotifyExchangeCodeCollectionIdentifiers = spotifyExchangeCodeCollection.map(
        spotifyExchangeCodeItem => this.getSpotifyExchangeCodeIdentifier(spotifyExchangeCodeItem)!
      );
      const spotifyExchangeCodesToAdd = spotifyExchangeCodes.filter(spotifyExchangeCodeItem => {
        const spotifyExchangeCodeIdentifier = this.getSpotifyExchangeCodeIdentifier(spotifyExchangeCodeItem);
        if (spotifyExchangeCodeCollectionIdentifiers.includes(spotifyExchangeCodeIdentifier)) {
          return false;
        }
        spotifyExchangeCodeCollectionIdentifiers.push(spotifyExchangeCodeIdentifier);
        return true;
      });
      return [...spotifyExchangeCodesToAdd, ...spotifyExchangeCodeCollection];
    }
    return spotifyExchangeCodeCollection;
  }
}
