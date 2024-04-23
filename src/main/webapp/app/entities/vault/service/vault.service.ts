import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVault, NewVault } from '../vault.model';

export type PartialUpdateVault = Partial<IVault> & Pick<IVault, 'id'>;

type RestOf<T extends IVault | NewVault> = Omit<T, 'dateLastUpdated'> & {
  dateLastUpdated?: string | null;
};

export type RestVault = RestOf<IVault>;

export type NewRestVault = RestOf<NewVault>;

export type PartialUpdateRestVault = RestOf<PartialUpdateVault>;

export type EntityResponseType = HttpResponse<IVault>;
export type EntityArrayResponseType = HttpResponse<IVault[]>;

@Injectable({ providedIn: 'root' })
export class VaultService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vaults');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vault: NewVault): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vault);
    return this.http.post<RestVault>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(vault: IVault): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vault);
    return this.http
      .put<RestVault>(`${this.resourceUrl}/${this.getVaultIdentifier(vault)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(vault: PartialUpdateVault): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vault);
    return this.http
      .patch<RestVault>(`${this.resourceUrl}/${this.getVaultIdentifier(vault)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestVault>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestVault[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVaultIdentifier(vault: Pick<IVault, 'id'>): number {
    return vault.id;
  }

  compareVault(o1: Pick<IVault, 'id'> | null, o2: Pick<IVault, 'id'> | null): boolean {
    return o1 && o2 ? this.getVaultIdentifier(o1) === this.getVaultIdentifier(o2) : o1 === o2;
  }

  addVaultToCollectionIfMissing<Type extends Pick<IVault, 'id'>>(
    vaultCollection: Type[],
    ...vaultsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const vaults: Type[] = vaultsToCheck.filter(isPresent);
    if (vaults.length > 0) {
      const vaultCollectionIdentifiers = vaultCollection.map(vaultItem => this.getVaultIdentifier(vaultItem)!);
      const vaultsToAdd = vaults.filter(vaultItem => {
        const vaultIdentifier = this.getVaultIdentifier(vaultItem);
        if (vaultCollectionIdentifiers.includes(vaultIdentifier)) {
          return false;
        }
        vaultCollectionIdentifiers.push(vaultIdentifier);
        return true;
      });
      return [...vaultsToAdd, ...vaultCollection];
    }
    return vaultCollection;
  }

  protected convertDateFromClient<T extends IVault | NewVault | PartialUpdateVault>(vault: T): RestOf<T> {
    return {
      ...vault,
      dateLastUpdated: vault.dateLastUpdated?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restVault: RestVault): IVault {
    return {
      ...restVault,
      dateLastUpdated: restVault.dateLastUpdated ? dayjs(restVault.dateLastUpdated) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestVault>): HttpResponse<IVault> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestVault[]>): HttpResponse<IVault[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
