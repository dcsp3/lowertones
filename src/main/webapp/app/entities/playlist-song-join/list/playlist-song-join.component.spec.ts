import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PlaylistSongJoinService } from '../service/playlist-song-join.service';

import { PlaylistSongJoinComponent } from './playlist-song-join.component';

describe('PlaylistSongJoin Management Component', () => {
  let comp: PlaylistSongJoinComponent;
  let fixture: ComponentFixture<PlaylistSongJoinComponent>;
  let service: PlaylistSongJoinService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'playlist-song-join', component: PlaylistSongJoinComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [PlaylistSongJoinComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(PlaylistSongJoinComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlaylistSongJoinComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PlaylistSongJoinService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.playlistSongJoins?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to playlistSongJoinService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getPlaylistSongJoinIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getPlaylistSongJoinIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
