import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ISpotifyGenreEntity } from '../spotify-genre-entity.model';
import { SpotifyGenreEntityService } from '../service/spotify-genre-entity.service';

import { SpotifyGenreEntityRoutingResolveService } from './spotify-genre-entity-routing-resolve.service';

describe('SpotifyGenreEntity routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SpotifyGenreEntityRoutingResolveService;
  let service: SpotifyGenreEntityService;
  let resultSpotifyGenreEntity: ISpotifyGenreEntity | null | undefined;

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
    routingResolveService = TestBed.inject(SpotifyGenreEntityRoutingResolveService);
    service = TestBed.inject(SpotifyGenreEntityService);
    resultSpotifyGenreEntity = undefined;
  });

  describe('resolve', () => {
    it('should return ISpotifyGenreEntity returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSpotifyGenreEntity = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSpotifyGenreEntity).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSpotifyGenreEntity = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSpotifyGenreEntity).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ISpotifyGenreEntity>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSpotifyGenreEntity = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSpotifyGenreEntity).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
