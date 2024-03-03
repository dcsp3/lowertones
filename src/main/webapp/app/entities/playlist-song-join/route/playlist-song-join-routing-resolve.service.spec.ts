import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IPlaylistSongJoin } from '../playlist-song-join.model';
import { PlaylistSongJoinService } from '../service/playlist-song-join.service';

import { PlaylistSongJoinRoutingResolveService } from './playlist-song-join-routing-resolve.service';

describe('PlaylistSongJoin routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PlaylistSongJoinRoutingResolveService;
  let service: PlaylistSongJoinService;
  let resultPlaylistSongJoin: IPlaylistSongJoin | null | undefined;

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
    routingResolveService = TestBed.inject(PlaylistSongJoinRoutingResolveService);
    service = TestBed.inject(PlaylistSongJoinService);
    resultPlaylistSongJoin = undefined;
  });

  describe('resolve', () => {
    it('should return IPlaylistSongJoin returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPlaylistSongJoin = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPlaylistSongJoin).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPlaylistSongJoin = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPlaylistSongJoin).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IPlaylistSongJoin>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPlaylistSongJoin = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPlaylistSongJoin).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
