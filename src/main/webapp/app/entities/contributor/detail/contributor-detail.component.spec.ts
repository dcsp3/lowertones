import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContributorDetailComponent } from './contributor-detail.component';

describe('Contributor Management Detail Component', () => {
  let comp: ContributorDetailComponent;
  let fixture: ComponentFixture<ContributorDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ContributorDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ contributor: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ContributorDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ContributorDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load contributor on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.contributor).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
