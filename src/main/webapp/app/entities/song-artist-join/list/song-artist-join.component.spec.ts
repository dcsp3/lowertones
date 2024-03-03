import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { SongArtistJoinService } from '../service/song-artist-join.service';

import { SongArtistJoinComponent } from './song-artist-join.component';

describe('SongArtistJoin Management Component', () => {
  let comp: SongArtistJoinComponent;
  let fixture: ComponentFixture<SongArtistJoinComponent>;
  let service: SongArtistJoinService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'song-artist-join', component: SongArtistJoinComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [SongArtistJoinComponent],
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
      .overrideTemplate(SongArtistJoinComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SongArtistJoinComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SongArtistJoinService);

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
    expect(comp.songArtistJoins?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to songArtistJoinService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getSongArtistJoinIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getSongArtistJoinIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
