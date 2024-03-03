import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMainArtist, NewMainArtist } from '../main-artist.model';

export type PartialUpdateMainArtist = Partial<IMainArtist> & Pick<IMainArtist, 'id'>;

type RestOf<T extends IMainArtist | NewMainArtist> = Omit<T, 'dateAddedToDB' | 'dateLastModified'> & {
  dateAddedToDB?: string | null;
  dateLastModified?: string | null;
};

export type RestMainArtist = RestOf<IMainArtist>;

export type NewRestMainArtist = RestOf<NewMainArtist>;

export type PartialUpdateRestMainArtist = RestOf<PartialUpdateMainArtist>;

export type EntityResponseType = HttpResponse<IMainArtist>;
export type EntityArrayResponseType = HttpResponse<IMainArtist[]>;

@Injectable({ providedIn: 'root' })
export class MainArtistService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/main-artists');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(mainArtist: NewMainArtist): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mainArtist);
    return this.http
      .post<RestMainArtist>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(mainArtist: IMainArtist): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mainArtist);
    return this.http
      .put<RestMainArtist>(`${this.resourceUrl}/${this.getMainArtistIdentifier(mainArtist)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(mainArtist: PartialUpdateMainArtist): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mainArtist);
    return this.http
      .patch<RestMainArtist>(`${this.resourceUrl}/${this.getMainArtistIdentifier(mainArtist)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMainArtist>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMainArtist[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMainArtistIdentifier(mainArtist: Pick<IMainArtist, 'id'>): number {
    return mainArtist.id;
  }

  compareMainArtist(o1: Pick<IMainArtist, 'id'> | null, o2: Pick<IMainArtist, 'id'> | null): boolean {
    return o1 && o2 ? this.getMainArtistIdentifier(o1) === this.getMainArtistIdentifier(o2) : o1 === o2;
  }

  addMainArtistToCollectionIfMissing<Type extends Pick<IMainArtist, 'id'>>(
    mainArtistCollection: Type[],
    ...mainArtistsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const mainArtists: Type[] = mainArtistsToCheck.filter(isPresent);
    if (mainArtists.length > 0) {
      const mainArtistCollectionIdentifiers = mainArtistCollection.map(mainArtistItem => this.getMainArtistIdentifier(mainArtistItem)!);
      const mainArtistsToAdd = mainArtists.filter(mainArtistItem => {
        const mainArtistIdentifier = this.getMainArtistIdentifier(mainArtistItem);
        if (mainArtistCollectionIdentifiers.includes(mainArtistIdentifier)) {
          return false;
        }
        mainArtistCollectionIdentifiers.push(mainArtistIdentifier);
        return true;
      });
      return [...mainArtistsToAdd, ...mainArtistCollection];
    }
    return mainArtistCollection;
  }

  protected convertDateFromClient<T extends IMainArtist | NewMainArtist | PartialUpdateMainArtist>(mainArtist: T): RestOf<T> {
    return {
      ...mainArtist,
      dateAddedToDB: mainArtist.dateAddedToDB?.format(DATE_FORMAT) ?? null,
      dateLastModified: mainArtist.dateLastModified?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restMainArtist: RestMainArtist): IMainArtist {
    return {
      ...restMainArtist,
      dateAddedToDB: restMainArtist.dateAddedToDB ? dayjs(restMainArtist.dateAddedToDB) : undefined,
      dateLastModified: restMainArtist.dateLastModified ? dayjs(restMainArtist.dateLastModified) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMainArtist>): HttpResponse<IMainArtist> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMainArtist[]>): HttpResponse<IMainArtist[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
