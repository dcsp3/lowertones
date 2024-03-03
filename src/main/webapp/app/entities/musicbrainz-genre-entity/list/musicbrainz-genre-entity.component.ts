import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMusicbrainzGenreEntity } from '../musicbrainz-genre-entity.model';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityArrayResponseType, MusicbrainzGenreEntityService } from '../service/musicbrainz-genre-entity.service';
import { MusicbrainzGenreEntityDeleteDialogComponent } from '../delete/musicbrainz-genre-entity-delete-dialog.component';
import { SortService } from 'app/shared/sort/sort.service';

@Component({
  selector: 'jhi-musicbrainz-genre-entity',
  templateUrl: './musicbrainz-genre-entity.component.html',
})
export class MusicbrainzGenreEntityComponent implements OnInit {
  musicbrainzGenreEntities?: IMusicbrainzGenreEntity[];
  isLoading = false;

  predicate = 'id';
  ascending = true;

  constructor(
    protected musicbrainzGenreEntityService: MusicbrainzGenreEntityService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected sortService: SortService,
    protected modalService: NgbModal
  ) {}

  trackId = (_index: number, item: IMusicbrainzGenreEntity): number =>
    this.musicbrainzGenreEntityService.getMusicbrainzGenreEntityIdentifier(item);

  ngOnInit(): void {
    this.load();
  }

  delete(musicbrainzGenreEntity: IMusicbrainzGenreEntity): void {
    const modalRef = this.modalService.open(MusicbrainzGenreEntityDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.musicbrainzGenreEntity = musicbrainzGenreEntity;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        switchMap(() => this.loadFromBackendWithRouteInformations())
      )
      .subscribe({
        next: (res: EntityArrayResponseType) => {
          this.onResponseSuccess(res);
        },
      });
  }

  load(): void {
    this.loadFromBackendWithRouteInformations().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(): void {
    this.handleNavigation(this.predicate, this.ascending);
  }

  protected loadFromBackendWithRouteInformations(): Observable<EntityArrayResponseType> {
    return combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data]).pipe(
      tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
      switchMap(() => this.queryBackend(this.predicate, this.ascending))
    );
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const sort = (params.get(SORT) ?? data[DEFAULT_SORT_DATA]).split(',');
    this.predicate = sort[0];
    this.ascending = sort[1] === ASC;
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.musicbrainzGenreEntities = this.refineData(dataFromBody);
  }

  protected refineData(data: IMusicbrainzGenreEntity[]): IMusicbrainzGenreEntity[] {
    return data.sort(this.sortService.startSort(this.predicate, this.ascending ? 1 : -1));
  }

  protected fillComponentAttributesFromResponseBody(data: IMusicbrainzGenreEntity[] | null): IMusicbrainzGenreEntity[] {
    return data ?? [];
  }

  protected queryBackend(predicate?: string, ascending?: boolean): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const queryObject = {
      sort: this.getSortQueryParam(predicate, ascending),
    };
    return this.musicbrainzGenreEntityService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(predicate?: string, ascending?: boolean): void {
    const queryParamsObj = {
      sort: this.getSortQueryParam(predicate, ascending),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }

  protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
    const ascendingQueryParam = ascending ? ASC : DESC;
    if (predicate === '') {
      return [];
    } else {
      return [predicate + ',' + ascendingQueryParam];
    }
  }
}
