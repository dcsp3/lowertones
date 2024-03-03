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

import { MainArtistUpdateComponent } from './main-artist-update.component';

describe('MainArtist Management Update Component', () => {
  let comp: MainArtistUpdateComponent;
  let fixture: ComponentFixture<MainArtistUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let mainArtistFormService: MainArtistFormService;
  let mainArtistService: MainArtistService;

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

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const mainArtist: IMainArtist = { id: 456 };

      activatedRoute.data = of({ mainArtist });
      comp.ngOnInit();

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
});
