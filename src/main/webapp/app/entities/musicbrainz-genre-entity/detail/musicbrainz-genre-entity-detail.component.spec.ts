import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MusicbrainzGenreEntityDetailComponent } from './musicbrainz-genre-entity-detail.component';

describe('MusicbrainzGenreEntity Management Detail Component', () => {
  let comp: MusicbrainzGenreEntityDetailComponent;
  let fixture: ComponentFixture<MusicbrainzGenreEntityDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MusicbrainzGenreEntityDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ musicbrainzGenreEntity: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MusicbrainzGenreEntityDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MusicbrainzGenreEntityDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load musicbrainzGenreEntity on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.musicbrainzGenreEntity).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
