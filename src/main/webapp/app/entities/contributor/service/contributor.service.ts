import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContributor, NewContributor } from '../contributor.model';

export type PartialUpdateContributor = Partial<IContributor> & Pick<IContributor, 'id'>;

export type EntityResponseType = HttpResponse<IContributor>;
export type EntityArrayResponseType = HttpResponse<IContributor[]>;

@Injectable({ providedIn: 'root' })
export class ContributorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contributors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contributor: NewContributor): Observable<EntityResponseType> {
    return this.http.post<IContributor>(this.resourceUrl, contributor, { observe: 'response' });
  }

  update(contributor: IContributor): Observable<EntityResponseType> {
    return this.http.put<IContributor>(`${this.resourceUrl}/${this.getContributorIdentifier(contributor)}`, contributor, {
      observe: 'response',
    });
  }

  partialUpdate(contributor: PartialUpdateContributor): Observable<EntityResponseType> {
    return this.http.patch<IContributor>(`${this.resourceUrl}/${this.getContributorIdentifier(contributor)}`, contributor, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContributor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContributor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getContributorIdentifier(contributor: Pick<IContributor, 'id'>): number {
    return contributor.id;
  }

  compareContributor(o1: Pick<IContributor, 'id'> | null, o2: Pick<IContributor, 'id'> | null): boolean {
    return o1 && o2 ? this.getContributorIdentifier(o1) === this.getContributorIdentifier(o2) : o1 === o2;
  }

  addContributorToCollectionIfMissing<Type extends Pick<IContributor, 'id'>>(
    contributorCollection: Type[],
    ...contributorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const contributors: Type[] = contributorsToCheck.filter(isPresent);
    if (contributors.length > 0) {
      const contributorCollectionIdentifiers = contributorCollection.map(
        contributorItem => this.getContributorIdentifier(contributorItem)!
      );
      const contributorsToAdd = contributors.filter(contributorItem => {
        const contributorIdentifier = this.getContributorIdentifier(contributorItem);
        if (contributorCollectionIdentifiers.includes(contributorIdentifier)) {
          return false;
        }
        contributorCollectionIdentifiers.push(contributorIdentifier);
        return true;
      });
      return [...contributorsToAdd, ...contributorCollection];
    }
    return contributorCollection;
  }
}
