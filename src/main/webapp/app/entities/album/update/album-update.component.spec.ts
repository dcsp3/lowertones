import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AlbumFormService } from './album-form.service';
import { AlbumService } from '../service/album.service';
import { IAlbum } from '../album.model';
import { IMainArtist } from 'app/entities/main-artist/main-artist.model';
import { MainArtistService } from 'app/entities/main-artist/service/main-artist.service';

import { AlbumUpdateComponent } from './album-update.component';

describe('Album Management Update Component', () => {
  let comp: AlbumUpdateComponent;
  let fixture: ComponentFixture<AlbumUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let albumFormService: AlbumFormService;
  let albumService: AlbumService;
  let mainArtistService: MainArtistService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AlbumUpdateComponent],
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
      .overrideTemplate(AlbumUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AlbumUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    albumFormService = TestBed.inject(AlbumFormService);
    albumService = TestBed.inject(AlbumService);
    mainArtistService = TestBed.inject(MainArtistService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call MainArtist query and add missing value', () => {
      const album: IAlbum = { id: 456 };
      const mainArtist: IMainArtist = { id: 57665 };
      album.mainArtist = mainArtist;

      const mainArtistCollection: IMainArtist[] = [{ id: 83984 }];
      jest.spyOn(mainArtistService, 'query').mockReturnValue(of(new HttpResponse({ body: mainArtistCollection })));
      const additionalMainArtists = [mainArtist];
      const expectedCollection: IMainArtist[] = [...additionalMainArtists, ...mainArtistCollection];
      jest.spyOn(mainArtistService, 'addMainArtistToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ album });
      comp.ngOnInit();

      expect(mainArtistService.query).toHaveBeenCalled();
      expect(mainArtistService.addMainArtistToCollectionIfMissing).toHaveBeenCalledWith(
        mainArtistCollection,
        ...additionalMainArtists.map(expect.objectContaining)
      );
      expect(comp.mainArtistsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const album: IAlbum = { id: 456 };
      const mainArtist: IMainArtist = { id: 5622 };
      album.mainArtist = mainArtist;

      activatedRoute.data = of({ album });
      comp.ngOnInit();

      expect(comp.mainArtistsSharedCollection).toContain(mainArtist);
      expect(comp.album).toEqual(album);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlbum>>();
      const album = { id: 123 };
      jest.spyOn(albumFormService, 'getAlbum').mockReturnValue(album);
      jest.spyOn(albumService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ album });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: album }));
      saveSubject.complete();

      // THEN
      expect(albumFormService.getAlbum).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(albumService.update).toHaveBeenCalledWith(expect.objectContaining(album));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlbum>>();
      const album = { id: 123 };
      jest.spyOn(albumFormService, 'getAlbum').mockReturnValue({ id: null });
      jest.spyOn(albumService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ album: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: album }));
      saveSubject.complete();

      // THEN
      expect(albumFormService.getAlbum).toHaveBeenCalled();
      expect(albumService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlbum>>();
      const album = { id: 123 };
      jest.spyOn(albumService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ album });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(albumService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMainArtist', () => {
      it('Should forward to mainArtistService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(mainArtistService, 'compareMainArtist');
        comp.compareMainArtist(entity, entity2);
        expect(mainArtistService.compareMainArtist).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
