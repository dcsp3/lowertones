import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MusicBrainzSongAttributionFormService } from './music-brainz-song-attribution-form.service';
import { MusicBrainzSongAttributionService } from '../service/music-brainz-song-attribution.service';
import { IMusicBrainzSongAttribution } from '../music-brainz-song-attribution.model';

import { MusicBrainzSongAttributionUpdateComponent } from './music-brainz-song-attribution-update.component';

describe('MusicBrainzSongAttribution Management Update Component', () => {
  let comp: MusicBrainzSongAttributionUpdateComponent;
  let fixture: ComponentFixture<MusicBrainzSongAttributionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let musicBrainzSongAttributionFormService: MusicBrainzSongAttributionFormService;
  let musicBrainzSongAttributionService: MusicBrainzSongAttributionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MusicBrainzSongAttributionUpdateComponent],
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
      .overrideTemplate(MusicBrainzSongAttributionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MusicBrainzSongAttributionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    musicBrainzSongAttributionFormService = TestBed.inject(MusicBrainzSongAttributionFormService);
    musicBrainzSongAttributionService = TestBed.inject(MusicBrainzSongAttributionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const musicBrainzSongAttribution: IMusicBrainzSongAttribution = { id: 456 };

      activatedRoute.data = of({ musicBrainzSongAttribution });
      comp.ngOnInit();

      expect(comp.musicBrainzSongAttribution).toEqual(musicBrainzSongAttribution);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMusicBrainzSongAttribution>>();
      const musicBrainzSongAttribution = { id: 123 };
      jest.spyOn(musicBrainzSongAttributionFormService, 'getMusicBrainzSongAttribution').mockReturnValue(musicBrainzSongAttribution);
      jest.spyOn(musicBrainzSongAttributionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ musicBrainzSongAttribution });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: musicBrainzSongAttribution }));
      saveSubject.complete();

      // THEN
      expect(musicBrainzSongAttributionFormService.getMusicBrainzSongAttribution).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(musicBrainzSongAttributionService.update).toHaveBeenCalledWith(expect.objectContaining(musicBrainzSongAttribution));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMusicBrainzSongAttribution>>();
      const musicBrainzSongAttribution = { id: 123 };
      jest.spyOn(musicBrainzSongAttributionFormService, 'getMusicBrainzSongAttribution').mockReturnValue({ id: null });
      jest.spyOn(musicBrainzSongAttributionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ musicBrainzSongAttribution: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: musicBrainzSongAttribution }));
      saveSubject.complete();

      // THEN
      expect(musicBrainzSongAttributionFormService.getMusicBrainzSongAttribution).toHaveBeenCalled();
      expect(musicBrainzSongAttributionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMusicBrainzSongAttribution>>();
      const musicBrainzSongAttribution = { id: 123 };
      jest.spyOn(musicBrainzSongAttributionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ musicBrainzSongAttribution });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(musicBrainzSongAttributionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
