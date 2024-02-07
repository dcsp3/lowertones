import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ISpotifyExchangeCode } from '../spotify-exchange-code.model';
import { SpotifyExchangeCodeService } from '../service/spotify-exchange-code.service';

import { SpotifyExchangeCodeRoutingResolveService } from './spotify-exchange-code-routing-resolve.service';

describe('SpotifyExchangeCode routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SpotifyExchangeCodeRoutingResolveService;
  let service: SpotifyExchangeCodeService;
  let resultSpotifyExchangeCode: ISpotifyExchangeCode | null | undefined;

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
    routingResolveService = TestBed.inject(SpotifyExchangeCodeRoutingResolveService);
    service = TestBed.inject(SpotifyExchangeCodeService);
    resultSpotifyExchangeCode = undefined;
  });

  describe('resolve', () => {
    it('should return ISpotifyExchangeCode returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSpotifyExchangeCode = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSpotifyExchangeCode).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSpotifyExchangeCode = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSpotifyExchangeCode).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ISpotifyExchangeCode>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSpotifyExchangeCode = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSpotifyExchangeCode).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
