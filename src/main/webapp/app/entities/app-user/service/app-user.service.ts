import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAppUser, NewAppUser } from '../app-user.model';

export type PartialUpdateAppUser = Partial<IAppUser> & Pick<IAppUser, 'id'>;

type RestOf<T extends IAppUser | NewAppUser> = Omit<T, 'lastLoginDate'> & {
  lastLoginDate?: string | null;
};

export type RestAppUser = RestOf<IAppUser>;

export type NewRestAppUser = RestOf<NewAppUser>;

export type PartialUpdateRestAppUser = RestOf<PartialUpdateAppUser>;

export type EntityResponseType = HttpResponse<IAppUser>;
export type EntityArrayResponseType = HttpResponse<IAppUser[]>;

@Injectable({ providedIn: 'root' })
export class AppUserService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/app-users');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(appUser: NewAppUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appUser);
    return this.http
      .post<RestAppUser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(appUser: IAppUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appUser);
    return this.http
      .put<RestAppUser>(`${this.resourceUrl}/${this.getAppUserIdentifier(appUser)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(appUser: PartialUpdateAppUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appUser);
    return this.http
      .patch<RestAppUser>(`${this.resourceUrl}/${this.getAppUserIdentifier(appUser)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAppUser>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAppUser[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAppUserIdentifier(appUser: Pick<IAppUser, 'id'>): number {
    return appUser.id;
  }

  compareAppUser(o1: Pick<IAppUser, 'id'> | null, o2: Pick<IAppUser, 'id'> | null): boolean {
    return o1 && o2 ? this.getAppUserIdentifier(o1) === this.getAppUserIdentifier(o2) : o1 === o2;
  }

  addAppUserToCollectionIfMissing<Type extends Pick<IAppUser, 'id'>>(
    appUserCollection: Type[],
    ...appUsersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const appUsers: Type[] = appUsersToCheck.filter(isPresent);
    if (appUsers.length > 0) {
      const appUserCollectionIdentifiers = appUserCollection.map(appUserItem => this.getAppUserIdentifier(appUserItem)!);
      const appUsersToAdd = appUsers.filter(appUserItem => {
        const appUserIdentifier = this.getAppUserIdentifier(appUserItem);
        if (appUserCollectionIdentifiers.includes(appUserIdentifier)) {
          return false;
        }
        appUserCollectionIdentifiers.push(appUserIdentifier);
        return true;
      });
      return [...appUsersToAdd, ...appUserCollection];
    }
    return appUserCollection;
  }

  protected convertDateFromClient<T extends IAppUser | NewAppUser | PartialUpdateAppUser>(appUser: T): RestOf<T> {
    const { lastLoginDate, ...rest } = appUser;

    if (typeof lastLoginDate === 'string') {
      // Assuming lastLoginDate is in ISO format, you can directly assign it
      return {
        ...rest,
        lastLoginDate,
      };
    } else if (lastLoginDate instanceof dayjs) {
      return {
        ...rest,
        lastLoginDate: lastLoginDate.format(DATE_FORMAT),
      };
    } else {
      return {
        ...rest,
        lastLoginDate: null, // or handle this case accordingly
      };
    }
  }

  protected convertDateFromServer(restAppUser: RestAppUser): IAppUser {
    return {
      ...restAppUser,
      lastLoginDate: restAppUser.lastLoginDate ? dayjs(restAppUser.lastLoginDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAppUser>): HttpResponse<IAppUser> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAppUser[]>): HttpResponse<IAppUser[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
