import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RelatedArtistsFormService } from './related-artists-form.service';
import { RelatedArtistsService } from '../service/related-artists.service';
import { IRelatedArtists } from '../related-artists.model';

import { RelatedArtistsUpdateComponent } from './related-artists-update.component';

describe('RelatedArtists Management Update Component', () => {
  let comp: RelatedArtistsUpdateComponent;
  let fixture: ComponentFixture<RelatedArtistsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let relatedArtistsFormService: RelatedArtistsFormService;
  let relatedArtistsService: RelatedArtistsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RelatedArtistsUpdateComponent],
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
      .overrideTemplate(RelatedArtistsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RelatedArtistsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    relatedArtistsFormService = TestBed.inject(RelatedArtistsFormService);
    relatedArtistsService = TestBed.inject(RelatedArtistsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const relatedArtists: IRelatedArtists = { id: 456 };

      activatedRoute.data = of({ relatedArtists });
      comp.ngOnInit();

      expect(comp.relatedArtists).toEqual(relatedArtists);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRelatedArtists>>();
      const relatedArtists = { id: 123 };
      jest.spyOn(relatedArtistsFormService, 'getRelatedArtists').mockReturnValue(relatedArtists);
      jest.spyOn(relatedArtistsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ relatedArtists });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: relatedArtists }));
      saveSubject.complete();

      // THEN
      expect(relatedArtistsFormService.getRelatedArtists).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(relatedArtistsService.update).toHaveBeenCalledWith(expect.objectContaining(relatedArtists));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRelatedArtists>>();
      const relatedArtists = { id: 123 };
      jest.spyOn(relatedArtistsFormService, 'getRelatedArtists').mockReturnValue({ id: null });
      jest.spyOn(relatedArtistsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ relatedArtists: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: relatedArtists }));
      saveSubject.complete();

      // THEN
      expect(relatedArtistsFormService.getRelatedArtists).toHaveBeenCalled();
      expect(relatedArtistsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRelatedArtists>>();
      const relatedArtists = { id: 123 };
      jest.spyOn(relatedArtistsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ relatedArtists });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(relatedArtistsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
