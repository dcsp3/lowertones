import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { SpotifyGenreEntityService } from '../service/spotify-genre-entity.service';

import { SpotifyGenreEntityComponent } from './spotify-genre-entity.component';

describe('SpotifyGenreEntity Management Component', () => {
  let comp: SpotifyGenreEntityComponent;
  let fixture: ComponentFixture<SpotifyGenreEntityComponent>;
  let service: SpotifyGenreEntityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'spotify-genre-entity', component: SpotifyGenreEntityComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [SpotifyGenreEntityComponent],
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
      .overrideTemplate(SpotifyGenreEntityComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SpotifyGenreEntityComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SpotifyGenreEntityService);

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
    expect(comp.spotifyGenreEntities?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to spotifyGenreEntityService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getSpotifyGenreEntityIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getSpotifyGenreEntityIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
