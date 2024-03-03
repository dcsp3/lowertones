import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SpotifyGenreEntityDetailComponent } from './spotify-genre-entity-detail.component';

describe('SpotifyGenreEntity Management Detail Component', () => {
  let comp: SpotifyGenreEntityDetailComponent;
  let fixture: ComponentFixture<SpotifyGenreEntityDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SpotifyGenreEntityDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ spotifyGenreEntity: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SpotifyGenreEntityDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SpotifyGenreEntityDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load spotifyGenreEntity on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.spotifyGenreEntity).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
