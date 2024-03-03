import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SpotifyGenreEntityFormService } from './spotify-genre-entity-form.service';
import { SpotifyGenreEntityService } from '../service/spotify-genre-entity.service';
import { ISpotifyGenreEntity } from '../spotify-genre-entity.model';
import { ISong } from 'app/entities/song/song.model';
import { SongService } from 'app/entities/song/service/song.service';
import { IAlbum } from 'app/entities/album/album.model';
import { AlbumService } from 'app/entities/album/service/album.service';
import { IMainArtist } from 'app/entities/main-artist/main-artist.model';
import { MainArtistService } from 'app/entities/main-artist/service/main-artist.service';

import { SpotifyGenreEntityUpdateComponent } from './spotify-genre-entity-update.component';

describe('SpotifyGenreEntity Management Update Component', () => {
  let comp: SpotifyGenreEntityUpdateComponent;
  let fixture: ComponentFixture<SpotifyGenreEntityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let spotifyGenreEntityFormService: SpotifyGenreEntityFormService;
  let spotifyGenreEntityService: SpotifyGenreEntityService;
  let songService: SongService;
  let albumService: AlbumService;
  let mainArtistService: MainArtistService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SpotifyGenreEntityUpdateComponent],
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
      .overrideTemplate(SpotifyGenreEntityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SpotifyGenreEntityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    spotifyGenreEntityFormService = TestBed.inject(SpotifyGenreEntityFormService);
    spotifyGenreEntityService = TestBed.inject(SpotifyGenreEntityService);
    songService = TestBed.inject(SongService);
    albumService = TestBed.inject(AlbumService);
    mainArtistService = TestBed.inject(MainArtistService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Song query and add missing value', () => {
      const spotifyGenreEntity: ISpotifyGenreEntity = { id: 456 };
      const song: ISong = { id: 10001 };
      spotifyGenreEntity.song = song;

      const songCollection: ISong[] = [{ id: 11008 }];
      jest.spyOn(songService, 'query').mockReturnValue(of(new HttpResponse({ body: songCollection })));
      const additionalSongs = [song];
      const expectedCollection: ISong[] = [...additionalSongs, ...songCollection];
      jest.spyOn(songService, 'addSongToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ spotifyGenreEntity });
      comp.ngOnInit();

      expect(songService.query).toHaveBeenCalled();
      expect(songService.addSongToCollectionIfMissing).toHaveBeenCalledWith(
        songCollection,
        ...additionalSongs.map(expect.objectContaining)
      );
      expect(comp.songsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Album query and add missing value', () => {
      const spotifyGenreEntity: ISpotifyGenreEntity = { id: 456 };
      const album: IAlbum = { id: 50737 };
      spotifyGenreEntity.album = album;

      const albumCollection: IAlbum[] = [{ id: 38386 }];
      jest.spyOn(albumService, 'query').mockReturnValue(of(new HttpResponse({ body: albumCollection })));
      const additionalAlbums = [album];
      const expectedCollection: IAlbum[] = [...additionalAlbums, ...albumCollection];
      jest.spyOn(albumService, 'addAlbumToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ spotifyGenreEntity });
      comp.ngOnInit();

      expect(albumService.query).toHaveBeenCalled();
      expect(albumService.addAlbumToCollectionIfMissing).toHaveBeenCalledWith(
        albumCollection,
        ...additionalAlbums.map(expect.objectContaining)
      );
      expect(comp.albumsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call MainArtist query and add missing value', () => {
      const spotifyGenreEntity: ISpotifyGenreEntity = { id: 456 };
      const mainArtist: IMainArtist = { id: 35159 };
      spotifyGenreEntity.mainArtist = mainArtist;

      const mainArtistCollection: IMainArtist[] = [{ id: 76835 }];
      jest.spyOn(mainArtistService, 'query').mockReturnValue(of(new HttpResponse({ body: mainArtistCollection })));
      const additionalMainArtists = [mainArtist];
      const expectedCollection: IMainArtist[] = [...additionalMainArtists, ...mainArtistCollection];
      jest.spyOn(mainArtistService, 'addMainArtistToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ spotifyGenreEntity });
      comp.ngOnInit();

      expect(mainArtistService.query).toHaveBeenCalled();
      expect(mainArtistService.addMainArtistToCollectionIfMissing).toHaveBeenCalledWith(
        mainArtistCollection,
        ...additionalMainArtists.map(expect.objectContaining)
      );
      expect(comp.mainArtistsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const spotifyGenreEntity: ISpotifyGenreEntity = { id: 456 };
      const song: ISong = { id: 68769 };
      spotifyGenreEntity.song = song;
      const album: IAlbum = { id: 26607 };
      spotifyGenreEntity.album = album;
      const mainArtist: IMainArtist = { id: 95672 };
      spotifyGenreEntity.mainArtist = mainArtist;

      activatedRoute.data = of({ spotifyGenreEntity });
      comp.ngOnInit();

      expect(comp.songsSharedCollection).toContain(song);
      expect(comp.albumsSharedCollection).toContain(album);
      expect(comp.mainArtistsSharedCollection).toContain(mainArtist);
      expect(comp.spotifyGenreEntity).toEqual(spotifyGenreEntity);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISpotifyGenreEntity>>();
      const spotifyGenreEntity = { id: 123 };
      jest.spyOn(spotifyGenreEntityFormService, 'getSpotifyGenreEntity').mockReturnValue(spotifyGenreEntity);
      jest.spyOn(spotifyGenreEntityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ spotifyGenreEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: spotifyGenreEntity }));
      saveSubject.complete();

      // THEN
      expect(spotifyGenreEntityFormService.getSpotifyGenreEntity).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(spotifyGenreEntityService.update).toHaveBeenCalledWith(expect.objectContaining(spotifyGenreEntity));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISpotifyGenreEntity>>();
      const spotifyGenreEntity = { id: 123 };
      jest.spyOn(spotifyGenreEntityFormService, 'getSpotifyGenreEntity').mockReturnValue({ id: null });
      jest.spyOn(spotifyGenreEntityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ spotifyGenreEntity: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: spotifyGenreEntity }));
      saveSubject.complete();

      // THEN
      expect(spotifyGenreEntityFormService.getSpotifyGenreEntity).toHaveBeenCalled();
      expect(spotifyGenreEntityService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISpotifyGenreEntity>>();
      const spotifyGenreEntity = { id: 123 };
      jest.spyOn(spotifyGenreEntityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ spotifyGenreEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(spotifyGenreEntityService.update).toHaveBeenCalled();
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
