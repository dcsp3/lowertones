import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MainArtistDetailComponent } from './main-artist-detail.component';

describe('MainArtist Management Detail Component', () => {
  let comp: MainArtistDetailComponent;
  let fixture: ComponentFixture<MainArtistDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MainArtistDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ mainArtist: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MainArtistDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MainArtistDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load mainArtist on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.mainArtist).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
