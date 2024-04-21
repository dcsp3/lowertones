import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVault, NewVault } from '../vault.model';

export type PartialUpdateVault = Partial<IVault> & Pick<IVault, 'id'>;

export type EntityResponseType = HttpResponse<IVault>;
export type EntityArrayResponseType = HttpResponse<IVault[]>;

@Injectable({ providedIn: 'root' })
export class VaultService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vaults');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vault: NewVault): Observable<EntityResponseType> {
    return this.http.post<IVault>(this.resourceUrl, vault, { observe: 'response' });
  }

  update(vault: IVault): Observable<EntityResponseType> {
    return this.http.put<IVault>(`${this.resourceUrl}/${this.getVaultIdentifier(vault)}`, vault, { observe: 'response' });
  }

  partialUpdate(vault: PartialUpdateVault): Observable<EntityResponseType> {
    return this.http.patch<IVault>(`${this.resourceUrl}/${this.getVaultIdentifier(vault)}`, vault, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVault>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVault[]>(this.resourceUrl, { params: options, observe: 'response' });
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
}
