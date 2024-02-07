import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SpotifyExchangeCodeDetailComponent } from './spotify-exchange-code-detail.component';

describe('SpotifyExchangeCode Management Detail Component', () => {
  let comp: SpotifyExchangeCodeDetailComponent;
  let fixture: ComponentFixture<SpotifyExchangeCodeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SpotifyExchangeCodeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ spotifyExchangeCode: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SpotifyExchangeCodeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SpotifyExchangeCodeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load spotifyExchangeCode on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.spotifyExchangeCode).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
