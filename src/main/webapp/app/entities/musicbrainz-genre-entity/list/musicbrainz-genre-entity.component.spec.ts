import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { MusicbrainzGenreEntityService } from '../service/musicbrainz-genre-entity.service';

import { MusicbrainzGenreEntityComponent } from './musicbrainz-genre-entity.component';

describe('MusicbrainzGenreEntity Management Component', () => {
  let comp: MusicbrainzGenreEntityComponent;
  let fixture: ComponentFixture<MusicbrainzGenreEntityComponent>;
  let service: MusicbrainzGenreEntityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'musicbrainz-genre-entity', component: MusicbrainzGenreEntityComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [MusicbrainzGenreEntityComponent],
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
      .overrideTemplate(MusicbrainzGenreEntityComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MusicbrainzGenreEntityComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MusicbrainzGenreEntityService);

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
    expect(comp.musicbrainzGenreEntities?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to musicbrainzGenreEntityService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getMusicbrainzGenreEntityIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getMusicbrainzGenreEntityIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
