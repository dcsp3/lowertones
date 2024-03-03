import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { MusicBrainzSongAttributionService } from '../service/music-brainz-song-attribution.service';

import { MusicBrainzSongAttributionComponent } from './music-brainz-song-attribution.component';

describe('MusicBrainzSongAttribution Management Component', () => {
  let comp: MusicBrainzSongAttributionComponent;
  let fixture: ComponentFixture<MusicBrainzSongAttributionComponent>;
  let service: MusicBrainzSongAttributionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'music-brainz-song-attribution', component: MusicBrainzSongAttributionComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [MusicBrainzSongAttributionComponent],
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
      .overrideTemplate(MusicBrainzSongAttributionComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MusicBrainzSongAttributionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MusicBrainzSongAttributionService);

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
    expect(comp.musicBrainzSongAttributions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to musicBrainzSongAttributionService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getMusicBrainzSongAttributionIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getMusicBrainzSongAttributionIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
