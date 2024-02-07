import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SpotifyExchangeCodeFormService } from './spotify-exchange-code-form.service';
import { SpotifyExchangeCodeService } from '../service/spotify-exchange-code.service';
import { ISpotifyExchangeCode } from '../spotify-exchange-code.model';

import { SpotifyExchangeCodeUpdateComponent } from './spotify-exchange-code-update.component';

describe('SpotifyExchangeCode Management Update Component', () => {
  let comp: SpotifyExchangeCodeUpdateComponent;
  let fixture: ComponentFixture<SpotifyExchangeCodeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let spotifyExchangeCodeFormService: SpotifyExchangeCodeFormService;
  let spotifyExchangeCodeService: SpotifyExchangeCodeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SpotifyExchangeCodeUpdateComponent],
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
      .overrideTemplate(SpotifyExchangeCodeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SpotifyExchangeCodeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    spotifyExchangeCodeFormService = TestBed.inject(SpotifyExchangeCodeFormService);
    spotifyExchangeCodeService = TestBed.inject(SpotifyExchangeCodeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const spotifyExchangeCode: ISpotifyExchangeCode = { id: 456 };

      activatedRoute.data = of({ spotifyExchangeCode });
      comp.ngOnInit();

      expect(comp.spotifyExchangeCode).toEqual(spotifyExchangeCode);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISpotifyExchangeCode>>();
      const spotifyExchangeCode = { id: 123 };
      jest.spyOn(spotifyExchangeCodeFormService, 'getSpotifyExchangeCode').mockReturnValue(spotifyExchangeCode);
      jest.spyOn(spotifyExchangeCodeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ spotifyExchangeCode });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: spotifyExchangeCode }));
      saveSubject.complete();

      // THEN
      expect(spotifyExchangeCodeFormService.getSpotifyExchangeCode).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(spotifyExchangeCodeService.update).toHaveBeenCalledWith(expect.objectContaining(spotifyExchangeCode));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISpotifyExchangeCode>>();
      const spotifyExchangeCode = { id: 123 };
      jest.spyOn(spotifyExchangeCodeFormService, 'getSpotifyExchangeCode').mockReturnValue({ id: null });
      jest.spyOn(spotifyExchangeCodeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ spotifyExchangeCode: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: spotifyExchangeCode }));
      saveSubject.complete();

      // THEN
      expect(spotifyExchangeCodeFormService.getSpotifyExchangeCode).toHaveBeenCalled();
      expect(spotifyExchangeCodeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISpotifyExchangeCode>>();
      const spotifyExchangeCode = { id: 123 };
      jest.spyOn(spotifyExchangeCodeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ spotifyExchangeCode });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(spotifyExchangeCodeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
