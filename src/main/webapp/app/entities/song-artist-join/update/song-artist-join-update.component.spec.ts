import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SongArtistJoinFormService } from './song-artist-join-form.service';
import { SongArtistJoinService } from '../service/song-artist-join.service';
import { ISongArtistJoin } from '../song-artist-join.model';
import { ISong } from 'app/entities/song/song.model';
import { SongService } from 'app/entities/song/service/song.service';
import { IMainArtist } from 'app/entities/main-artist/main-artist.model';
import { MainArtistService } from 'app/entities/main-artist/service/main-artist.service';

import { SongArtistJoinUpdateComponent } from './song-artist-join-update.component';

describe('SongArtistJoin Management Update Component', () => {
  let comp: SongArtistJoinUpdateComponent;
  let fixture: ComponentFixture<SongArtistJoinUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let songArtistJoinFormService: SongArtistJoinFormService;
  let songArtistJoinService: SongArtistJoinService;
  let songService: SongService;
  let mainArtistService: MainArtistService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SongArtistJoinUpdateComponent],
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
      .overrideTemplate(SongArtistJoinUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SongArtistJoinUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    songArtistJoinFormService = TestBed.inject(SongArtistJoinFormService);
    songArtistJoinService = TestBed.inject(SongArtistJoinService);
    songService = TestBed.inject(SongService);
    mainArtistService = TestBed.inject(MainArtistService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Song query and add missing value', () => {
      const songArtistJoin: ISongArtistJoin = { id: 456 };
      const song: ISong = { id: 22999 };
      songArtistJoin.song = song;

      const songCollection: ISong[] = [{ id: 51094 }];
      jest.spyOn(songService, 'query').mockReturnValue(of(new HttpResponse({ body: songCollection })));
      const additionalSongs = [song];
      const expectedCollection: ISong[] = [...additionalSongs, ...songCollection];
      jest.spyOn(songService, 'addSongToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ songArtistJoin });
      comp.ngOnInit();

      expect(songService.query).toHaveBeenCalled();
      expect(songService.addSongToCollectionIfMissing).toHaveBeenCalledWith(
        songCollection,
        ...additionalSongs.map(expect.objectContaining)
      );
      expect(comp.songsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call MainArtist query and add missing value', () => {
      const songArtistJoin: ISongArtistJoin = { id: 456 };
      const mainArtist: IMainArtist = { id: 12364 };
      songArtistJoin.mainArtist = mainArtist;

      const mainArtistCollection: IMainArtist[] = [{ id: 45224 }];
      jest.spyOn(mainArtistService, 'query').mockReturnValue(of(new HttpResponse({ body: mainArtistCollection })));
      const additionalMainArtists = [mainArtist];
      const expectedCollection: IMainArtist[] = [...additionalMainArtists, ...mainArtistCollection];
      jest.spyOn(mainArtistService, 'addMainArtistToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ songArtistJoin });
      comp.ngOnInit();

      expect(mainArtistService.query).toHaveBeenCalled();
      expect(mainArtistService.addMainArtistToCollectionIfMissing).toHaveBeenCalledWith(
        mainArtistCollection,
        ...additionalMainArtists.map(expect.objectContaining)
      );
      expect(comp.mainArtistsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const songArtistJoin: ISongArtistJoin = { id: 456 };
      const song: ISong = { id: 58355 };
      songArtistJoin.song = song;
      const mainArtist: IMainArtist = { id: 90662 };
      songArtistJoin.mainArtist = mainArtist;

      activatedRoute.data = of({ songArtistJoin });
      comp.ngOnInit();

      expect(comp.songsSharedCollection).toContain(song);
      expect(comp.mainArtistsSharedCollection).toContain(mainArtist);
      expect(comp.songArtistJoin).toEqual(songArtistJoin);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISongArtistJoin>>();
      const songArtistJoin = { id: 123 };
      jest.spyOn(songArtistJoinFormService, 'getSongArtistJoin').mockReturnValue(songArtistJoin);
      jest.spyOn(songArtistJoinService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ songArtistJoin });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: songArtistJoin }));
      saveSubject.complete();

      // THEN
      expect(songArtistJoinFormService.getSongArtistJoin).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(songArtistJoinService.update).toHaveBeenCalledWith(expect.objectContaining(songArtistJoin));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISongArtistJoin>>();
      const songArtistJoin = { id: 123 };
      jest.spyOn(songArtistJoinFormService, 'getSongArtistJoin').mockReturnValue({ id: null });
      jest.spyOn(songArtistJoinService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ songArtistJoin: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: songArtistJoin }));
      saveSubject.complete();

      // THEN
      expect(songArtistJoinFormService.getSongArtistJoin).toHaveBeenCalled();
      expect(songArtistJoinService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISongArtistJoin>>();
      const songArtistJoin = { id: 123 };
      jest.spyOn(songArtistJoinService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ songArtistJoin });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(songArtistJoinService.update).toHaveBeenCalled();
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
