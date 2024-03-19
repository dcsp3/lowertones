import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RelatedArtistsDetailComponent } from './related-artists-detail.component';

describe('RelatedArtists Management Detail Component', () => {
  let comp: RelatedArtistsDetailComponent;
  let fixture: ComponentFixture<RelatedArtistsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RelatedArtistsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ relatedArtists: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RelatedArtistsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RelatedArtistsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load relatedArtists on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.relatedArtists).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
