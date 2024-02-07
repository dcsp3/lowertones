import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { SpotifyExchangeCodeService } from '../service/spotify-exchange-code.service';

import { SpotifyExchangeCodeComponent } from './spotify-exchange-code.component';

describe('SpotifyExchangeCode Management Component', () => {
  let comp: SpotifyExchangeCodeComponent;
  let fixture: ComponentFixture<SpotifyExchangeCodeComponent>;
  let service: SpotifyExchangeCodeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'spotify-exchange-code', component: SpotifyExchangeCodeComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [SpotifyExchangeCodeComponent],
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
      .overrideTemplate(SpotifyExchangeCodeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SpotifyExchangeCodeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SpotifyExchangeCodeService);

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
    expect(comp.spotifyExchangeCodes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to spotifyExchangeCodeService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getSpotifyExchangeCodeIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getSpotifyExchangeCodeIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
