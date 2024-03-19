import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SongFormService } from './song-form.service';
import { SongService } from '../service/song.service';
import { ISong } from '../song.model';
import { IContributor } from 'app/entities/contributor/contributor.model';
import { ContributorService } from 'app/entities/contributor/service/contributor.service';
import { IAlbum } from 'app/entities/album/album.model';
import { AlbumService } from 'app/entities/album/service/album.service';

import { SongUpdateComponent } from './song-update.component';

describe('Song Management Update Component', () => {
  let comp: SongUpdateComponent;
  let fixture: ComponentFixture<SongUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let songFormService: SongFormService;
  let songService: SongService;
  let contributorService: ContributorService;
  let albumService: AlbumService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SongUpdateComponent],
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
      .overrideTemplate(SongUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SongUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    songFormService = TestBed.inject(SongFormService);
    songService = TestBed.inject(SongService);
    contributorService = TestBed.inject(ContributorService);
    albumService = TestBed.inject(AlbumService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Contributor query and add missing value', () => {
      const song: ISong = { id: 456 };
      const contributors: IContributor[] = [{ id: 27957 }];
      song.contributors = contributors;

      const contributorCollection: IContributor[] = [{ id: 13676 }];
      jest.spyOn(contributorService, 'query').mockReturnValue(of(new HttpResponse({ body: contributorCollection })));
      const additionalContributors = [...contributors];
      const expectedCollection: IContributor[] = [...additionalContributors, ...contributorCollection];
      jest.spyOn(contributorService, 'addContributorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ song });
      comp.ngOnInit();

      expect(contributorService.query).toHaveBeenCalled();
      expect(contributorService.addContributorToCollectionIfMissing).toHaveBeenCalledWith(
        contributorCollection,
        ...additionalContributors.map(expect.objectContaining)
      );
      expect(comp.contributorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Album query and add missing value', () => {
      const song: ISong = { id: 456 };
      const album: IAlbum = { id: 13290 };
      song.album = album;

      const albumCollection: IAlbum[] = [{ id: 86184 }];
      jest.spyOn(albumService, 'query').mockReturnValue(of(new HttpResponse({ body: albumCollection })));
      const additionalAlbums = [album];
      const expectedCollection: IAlbum[] = [...additionalAlbums, ...albumCollection];
      jest.spyOn(albumService, 'addAlbumToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ song });
      comp.ngOnInit();

      expect(albumService.query).toHaveBeenCalled();
      expect(albumService.addAlbumToCollectionIfMissing).toHaveBeenCalledWith(
        albumCollection,
        ...additionalAlbums.map(expect.objectContaining)
      );
      expect(comp.albumsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const song: ISong = { id: 456 };
      const contributor: IContributor = { id: 90438 };
      song.contributors = [contributor];
      const album: IAlbum = { id: 36859 };
      song.album = album;

      activatedRoute.data = of({ song });
      comp.ngOnInit();

      expect(comp.contributorsSharedCollection).toContain(contributor);
      expect(comp.albumsSharedCollection).toContain(album);
      expect(comp.song).toEqual(song);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISong>>();
      const song = { id: 123 };
      jest.spyOn(songFormService, 'getSong').mockReturnValue(song);
      jest.spyOn(songService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ song });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: song }));
      saveSubject.complete();

      // THEN
      expect(songFormService.getSong).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(songService.update).toHaveBeenCalledWith(expect.objectContaining(song));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISong>>();
      const song = { id: 123 };
      jest.spyOn(songFormService, 'getSong').mockReturnValue({ id: null });
      jest.spyOn(songService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ song: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: song }));
      saveSubject.complete();

      // THEN
      expect(songFormService.getSong).toHaveBeenCalled();
      expect(songService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISong>>();
      const song = { id: 123 };
      jest.spyOn(songService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ song });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(songService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareContributor', () => {
      it('Should forward to contributorService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(contributorService, 'compareContributor');
        comp.compareContributor(entity, entity2);
        expect(contributorService.compareContributor).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareAlbum', () => {
      it('Should forward to albumService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(albumService, 'compareAlbum');
        comp.compareAlbum(entity, entity2);
        expect(albumService.compareAlbum).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
