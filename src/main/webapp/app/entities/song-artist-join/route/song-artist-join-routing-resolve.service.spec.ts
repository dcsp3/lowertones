import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ISongArtistJoin } from '../song-artist-join.model';
import { SongArtistJoinService } from '../service/song-artist-join.service';

import { SongArtistJoinRoutingResolveService } from './song-artist-join-routing-resolve.service';

describe('SongArtistJoin routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SongArtistJoinRoutingResolveService;
  let service: SongArtistJoinService;
  let resultSongArtistJoin: ISongArtistJoin | null | undefined;

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
    routingResolveService = TestBed.inject(SongArtistJoinRoutingResolveService);
    service = TestBed.inject(SongArtistJoinService);
    resultSongArtistJoin = undefined;
  });

  describe('resolve', () => {
    it('should return ISongArtistJoin returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSongArtistJoin = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSongArtistJoin).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSongArtistJoin = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSongArtistJoin).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ISongArtistJoin>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSongArtistJoin = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSongArtistJoin).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
