import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MusicBrainzSongAttributionDetailComponent } from './music-brainz-song-attribution-detail.component';

describe('MusicBrainzSongAttribution Management Detail Component', () => {
  let comp: MusicBrainzSongAttributionDetailComponent;
  let fixture: ComponentFixture<MusicBrainzSongAttributionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MusicBrainzSongAttributionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ musicBrainzSongAttribution: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MusicBrainzSongAttributionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MusicBrainzSongAttributionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load musicBrainzSongAttribution on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.musicBrainzSongAttribution).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
