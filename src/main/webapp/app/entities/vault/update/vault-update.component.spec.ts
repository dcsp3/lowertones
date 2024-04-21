import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VaultFormService } from './vault-form.service';
import { VaultService } from '../service/vault.service';
import { IVault } from '../vault.model';

import { VaultUpdateComponent } from './vault-update.component';

describe('Vault Management Update Component', () => {
  let comp: VaultUpdateComponent;
  let fixture: ComponentFixture<VaultUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vaultFormService: VaultFormService;
  let vaultService: VaultService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VaultUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(VaultUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VaultUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vaultFormService = TestBed.inject(VaultFormService);
    vaultService = TestBed.inject(VaultService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const vault: IVault = { id: 456 };

      activatedRoute.data = of({ vault });
      comp.ngOnInit();

      expect(comp.vault).toEqual(vault);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVault>>();
      const vault = { id: 123 };
      jest.spyOn(vaultFormService, 'getVault').mockReturnValue(vault);
      jest.spyOn(vaultService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vault });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vault }));
      saveSubject.complete();

      // THEN
      expect(vaultFormService.getVault).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vaultService.update).toHaveBeenCalledWith(expect.objectContaining(vault));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVault>>();
      const vault = { id: 123 };
      jest.spyOn(vaultFormService, 'getVault').mockReturnValue({ id: null });
      jest.spyOn(vaultService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vault: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vault }));
      saveSubject.complete();

      // THEN
      expect(vaultFormService.getVault).toHaveBeenCalled();
      expect(vaultService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVault>>();
      const vault = { id: 123 };
      jest.spyOn(vaultService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vault });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vaultService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
