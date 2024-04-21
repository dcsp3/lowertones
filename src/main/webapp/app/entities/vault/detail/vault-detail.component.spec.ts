import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VaultDetailComponent } from './vault-detail.component';

describe('Vault Management Detail Component', () => {
  let comp: VaultDetailComponent;
  let fixture: ComponentFixture<VaultDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VaultDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ vault: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(VaultDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(VaultDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load vault on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.vault).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
