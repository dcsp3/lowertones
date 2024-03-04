import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { MainArtistService } from '../service/main-artist.service';

import { MainArtistComponent } from './main-artist.component';

describe('MainArtist Management Component', () => {
  let comp: MainArtistComponent;
  let fixture: ComponentFixture<MainArtistComponent>;
  let service: MainArtistService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'main-artist', component: MainArtistComponent }]), HttpClientTestingModule],
      declarations: [MainArtistComponent],
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
      .overrideTemplate(MainArtistComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MainArtistComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MainArtistService);

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
    expect(comp.mainArtists?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to mainArtistService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getMainArtistIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getMainArtistIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
