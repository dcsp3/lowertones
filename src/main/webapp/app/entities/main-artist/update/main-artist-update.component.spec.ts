import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MainArtistFormService } from './main-artist-form.service';
import { MainArtistService } from '../service/main-artist.service';
import { IMainArtist } from '../main-artist.model';
import { IRelatedArtists } from 'app/entities/related-artists/related-artists.model';
import { RelatedArtistsService } from 'app/entities/related-artists/service/related-artists.service';

import { MainArtistUpdateComponent } from './main-artist-update.component';

describe('MainArtist Management Update Component', () => {
  let comp: MainArtistUpdateComponent;
  let fixture: ComponentFixture<MainArtistUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let mainArtistFormService: MainArtistFormService;
  let mainArtistService: MainArtistService;
  let relatedArtistsService: RelatedArtistsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MainArtistUpdateComponent],
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
      .overrideTemplate(MainArtistUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MainArtistUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    mainArtistFormService = TestBed.inject(MainArtistFormService);
    mainArtistService = TestBed.inject(MainArtistService);
    relatedArtistsService = TestBed.inject(RelatedArtistsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call relatedArtists query and add missing value', () => {
      const mainArtist: IMainArtist = { id: 456 };
      const relatedArtists: IRelatedArtists = { id: 87828 };
      mainArtist.relatedArtists = relatedArtists;

      const relatedArtistsCollection: IRelatedArtists[] = [{ id: 28257 }];
      jest.spyOn(relatedArtistsService, 'query').mockReturnValue(of(new HttpResponse({ body: relatedArtistsCollection })));
      const expectedCollection: IRelatedArtists[] = [relatedArtists, ...relatedArtistsCollection];
      jest.spyOn(relatedArtistsService, 'addRelatedArtistsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ mainArtist });
      comp.ngOnInit();

      expect(relatedArtistsService.query).toHaveBeenCalled();
      expect(relatedArtistsService.addRelatedArtistsToCollectionIfMissing).toHaveBeenCalledWith(relatedArtistsCollection, relatedArtists);
      expect(comp.relatedArtistsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const mainArtist: IMainArtist = { id: 456 };
      const relatedArtists: IRelatedArtists = { id: 50852 };
      mainArtist.relatedArtists = relatedArtists;

      activatedRoute.data = of({ mainArtist });
      comp.ngOnInit();

      expect(comp.relatedArtistsCollection).toContain(relatedArtists);
      expect(comp.mainArtist).toEqual(mainArtist);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMainArtist>>();
      const mainArtist = { id: 123 };
      jest.spyOn(mainArtistFormService, 'getMainArtist').mockReturnValue(mainArtist);
      jest.spyOn(mainArtistService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mainArtist });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mainArtist }));
      saveSubject.complete();

      // THEN
      expect(mainArtistFormService.getMainArtist).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(mainArtistService.update).toHaveBeenCalledWith(expect.objectContaining(mainArtist));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMainArtist>>();
      const mainArtist = { id: 123 };
      jest.spyOn(mainArtistFormService, 'getMainArtist').mockReturnValue({ id: null });
      jest.spyOn(mainArtistService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mainArtist: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mainArtist }));
      saveSubject.complete();

      // THEN
      expect(mainArtistFormService.getMainArtist).toHaveBeenCalled();
      expect(mainArtistService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMainArtist>>();
      const mainArtist = { id: 123 };
      jest.spyOn(mainArtistService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mainArtist });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(mainArtistService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareRelatedArtists', () => {
      it('Should forward to relatedArtistsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(relatedArtistsService, 'compareRelatedArtists');
        comp.compareRelatedArtists(entity, entity2);
        expect(relatedArtistsService.compareRelatedArtists).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
