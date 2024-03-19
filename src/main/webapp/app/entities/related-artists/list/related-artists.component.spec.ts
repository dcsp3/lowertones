import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { RelatedArtistsService } from '../service/related-artists.service';

import { RelatedArtistsComponent } from './related-artists.component';

describe('RelatedArtists Management Component', () => {
  let comp: RelatedArtistsComponent;
  let fixture: ComponentFixture<RelatedArtistsComponent>;
  let service: RelatedArtistsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'related-artists', component: RelatedArtistsComponent }]), HttpClientTestingModule],
      declarations: [RelatedArtistsComponent],
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
      .overrideTemplate(RelatedArtistsComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RelatedArtistsComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RelatedArtistsService);

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
    expect(comp.relatedArtists?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to relatedArtistsService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getRelatedArtistsIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getRelatedArtistsIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
