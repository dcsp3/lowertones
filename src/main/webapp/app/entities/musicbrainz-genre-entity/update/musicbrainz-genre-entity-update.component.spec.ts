import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MusicbrainzGenreEntityFormService } from './musicbrainz-genre-entity-form.service';
import { MusicbrainzGenreEntityService } from '../service/musicbrainz-genre-entity.service';
import { IMusicbrainzGenreEntity } from '../musicbrainz-genre-entity.model';
import { ISong } from 'app/entities/song/song.model';
import { SongService } from 'app/entities/song/service/song.service';
import { IAlbum } from 'app/entities/album/album.model';
import { AlbumService } from 'app/entities/album/service/album.service';
import { IMainArtist } from 'app/entities/main-artist/main-artist.model';
import { MainArtistService } from 'app/entities/main-artist/service/main-artist.service';

import { MusicbrainzGenreEntityUpdateComponent } from './musicbrainz-genre-entity-update.component';

describe('MusicbrainzGenreEntity Management Update Component', () => {
  let comp: MusicbrainzGenreEntityUpdateComponent;
  let fixture: ComponentFixture<MusicbrainzGenreEntityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let musicbrainzGenreEntityFormService: MusicbrainzGenreEntityFormService;
  let musicbrainzGenreEntityService: MusicbrainzGenreEntityService;
  let songService: SongService;
  let albumService: AlbumService;
  let mainArtistService: MainArtistService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MusicbrainzGenreEntityUpdateComponent],
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
      .overrideTemplate(MusicbrainzGenreEntityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MusicbrainzGenreEntityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    musicbrainzGenreEntityFormService = TestBed.inject(MusicbrainzGenreEntityFormService);
    musicbrainzGenreEntityService = TestBed.inject(MusicbrainzGenreEntityService);
    songService = TestBed.inject(SongService);
    albumService = TestBed.inject(AlbumService);
    mainArtistService = TestBed.inject(MainArtistService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Song query and add missing value', () => {
      const musicbrainzGenreEntity: IMusicbrainzGenreEntity = { id: 456 };
      const song: ISong = { id: 33694 };
      musicbrainzGenreEntity.song = song;

      const songCollection: ISong[] = [{ id: 90345 }];
      jest.spyOn(songService, 'query').mockReturnValue(of(new HttpResponse({ body: songCollection })));
      const additionalSongs = [song];
      const expectedCollection: ISong[] = [...additionalSongs, ...songCollection];
      jest.spyOn(songService, 'addSongToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ musicbrainzGenreEntity });
      comp.ngOnInit();

      expect(songService.query).toHaveBeenCalled();
      expect(songService.addSongToCollectionIfMissing).toHaveBeenCalledWith(
        songCollection,
        ...additionalSongs.map(expect.objectContaining)
      );
      expect(comp.songsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Album query and add missing value', () => {
      const musicbrainzGenreEntity: IMusicbrainzGenreEntity = { id: 456 };
      const album: IAlbum = { id: 59316 };
      musicbrainzGenreEntity.album = album;

      const albumCollection: IAlbum[] = [{ id: 95907 }];
      jest.spyOn(albumService, 'query').mockReturnValue(of(new HttpResponse({ body: albumCollection })));
      const additionalAlbums = [album];
      const expectedCollection: IAlbum[] = [...additionalAlbums, ...albumCollection];
      jest.spyOn(albumService, 'addAlbumToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ musicbrainzGenreEntity });
      comp.ngOnInit();

      expect(albumService.query).toHaveBeenCalled();
      expect(albumService.addAlbumToCollectionIfMissing).toHaveBeenCalledWith(
        albumCollection,
        ...additionalAlbums.map(expect.objectContaining)
      );
      expect(comp.albumsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call MainArtist query and add missing value', () => {
      const musicbrainzGenreEntity: IMusicbrainzGenreEntity = { id: 456 };
      const mainArtist: IMainArtist = { id: 28294 };
      musicbrainzGenreEntity.mainArtist = mainArtist;

      const mainArtistCollection: IMainArtist[] = [{ id: 11310 }];
      jest.spyOn(mainArtistService, 'query').mockReturnValue(of(new HttpResponse({ body: mainArtistCollection })));
      const additionalMainArtists = [mainArtist];
      const expectedCollection: IMainArtist[] = [...additionalMainArtists, ...mainArtistCollection];
      jest.spyOn(mainArtistService, 'addMainArtistToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ musicbrainzGenreEntity });
      comp.ngOnInit();

      expect(mainArtistService.query).toHaveBeenCalled();
      expect(mainArtistService.addMainArtistToCollectionIfMissing).toHaveBeenCalledWith(
        mainArtistCollection,
        ...additionalMainArtists.map(expect.objectContaining)
      );
      expect(comp.mainArtistsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const musicbrainzGenreEntity: IMusicbrainzGenreEntity = { id: 456 };
      const song: ISong = { id: 18459 };
      musicbrainzGenreEntity.song = song;
      const album: IAlbum = { id: 18033 };
      musicbrainzGenreEntity.album = album;
      const mainArtist: IMainArtist = { id: 97021 };
      musicbrainzGenreEntity.mainArtist = mainArtist;

      activatedRoute.data = of({ musicbrainzGenreEntity });
      comp.ngOnInit();

      expect(comp.songsSharedCollection).toContain(song);
      expect(comp.albumsSharedCollection).toContain(album);
      expect(comp.mainArtistsSharedCollection).toContain(mainArtist);
      expect(comp.musicbrainzGenreEntity).toEqual(musicbrainzGenreEntity);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMusicbrainzGenreEntity>>();
      const musicbrainzGenreEntity = { id: 123 };
      jest.spyOn(musicbrainzGenreEntityFormService, 'getMusicbrainzGenreEntity').mockReturnValue(musicbrainzGenreEntity);
      jest.spyOn(musicbrainzGenreEntityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ musicbrainzGenreEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: musicbrainzGenreEntity }));
      saveSubject.complete();

      // THEN
      expect(musicbrainzGenreEntityFormService.getMusicbrainzGenreEntity).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(musicbrainzGenreEntityService.update).toHaveBeenCalledWith(expect.objectContaining(musicbrainzGenreEntity));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMusicbrainzGenreEntity>>();
      const musicbrainzGenreEntity = { id: 123 };
      jest.spyOn(musicbrainzGenreEntityFormService, 'getMusicbrainzGenreEntity').mockReturnValue({ id: null });
      jest.spyOn(musicbrainzGenreEntityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ musicbrainzGenreEntity: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: musicbrainzGenreEntity }));
      saveSubject.complete();

      // THEN
      expect(musicbrainzGenreEntityFormService.getMusicbrainzGenreEntity).toHaveBeenCalled();
      expect(musicbrainzGenreEntityService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMusicbrainzGenreEntity>>();
      const musicbrainzGenreEntity = { id: 123 };
      jest.spyOn(musicbrainzGenreEntityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ musicbrainzGenreEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(musicbrainzGenreEntityService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSong', () => {
      it('Should forward to songService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(songService, 'compareSong');
        comp.compareSong(entity, entity2);
        expect(songService.compareSong).toHaveBeenCalledWith(entity, entity2);
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
