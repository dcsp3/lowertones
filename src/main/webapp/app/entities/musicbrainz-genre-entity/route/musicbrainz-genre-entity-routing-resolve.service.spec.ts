import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IMusicbrainzGenreEntity } from '../musicbrainz-genre-entity.model';
import { MusicbrainzGenreEntityService } from '../service/musicbrainz-genre-entity.service';

import { MusicbrainzGenreEntityRoutingResolveService } from './musicbrainz-genre-entity-routing-resolve.service';

describe('MusicbrainzGenreEntity routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: MusicbrainzGenreEntityRoutingResolveService;
  let service: MusicbrainzGenreEntityService;
  let resultMusicbrainzGenreEntity: IMusicbrainzGenreEntity | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(MusicbrainzGenreEntityRoutingResolveService);
    service = TestBed.inject(MusicbrainzGenreEntityService);
    resultMusicbrainzGenreEntity = undefined;
  });

  describe('resolve', () => {
    it('should return IMusicbrainzGenreEntity returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMusicbrainzGenreEntity = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMusicbrainzGenreEntity).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMusicbrainzGenreEntity = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultMusicbrainzGenreEntity).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IMusicbrainzGenreEntity>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMusicbrainzGenreEntity = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMusicbrainzGenreEntity).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
