import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ContributorFormService } from './contributor-form.service';
import { ContributorService } from '../service/contributor.service';
import { IContributor } from '../contributor.model';

import { ContributorUpdateComponent } from './contributor-update.component';

describe('Contributor Management Update Component', () => {
  let comp: ContributorUpdateComponent;
  let fixture: ComponentFixture<ContributorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contributorFormService: ContributorFormService;
  let contributorService: ContributorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ContributorUpdateComponent],
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
      .overrideTemplate(ContributorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContributorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contributorFormService = TestBed.inject(ContributorFormService);
    contributorService = TestBed.inject(ContributorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const contributor: IContributor = { id: 456 };

      activatedRoute.data = of({ contributor });
      comp.ngOnInit();

      expect(comp.contributor).toEqual(contributor);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContributor>>();
      const contributor = { id: 123 };
      jest.spyOn(contributorFormService, 'getContributor').mockReturnValue(contributor);
      jest.spyOn(contributorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contributor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contributor }));
      saveSubject.complete();

      // THEN
      expect(contributorFormService.getContributor).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contributorService.update).toHaveBeenCalledWith(expect.objectContaining(contributor));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContributor>>();
      const contributor = { id: 123 };
      jest.spyOn(contributorFormService, 'getContributor').mockReturnValue({ id: null });
      jest.spyOn(contributorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contributor: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contributor }));
      saveSubject.complete();

      // THEN
      expect(contributorFormService.getContributor).toHaveBeenCalled();
      expect(contributorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContributor>>();
      const contributor = { id: 123 };
      jest.spyOn(contributorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contributor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contributorService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
