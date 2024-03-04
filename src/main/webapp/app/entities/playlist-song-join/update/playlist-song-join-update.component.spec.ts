import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PlaylistSongJoinFormService } from './playlist-song-join-form.service';
import { PlaylistSongJoinService } from '../service/playlist-song-join.service';
import { IPlaylistSongJoin } from '../playlist-song-join.model';
import { IPlaylist } from 'app/entities/playlist/playlist.model';
import { PlaylistService } from 'app/entities/playlist/service/playlist.service';
import { ISong } from 'app/entities/song/song.model';
import { SongService } from 'app/entities/song/service/song.service';

import { PlaylistSongJoinUpdateComponent } from './playlist-song-join-update.component';

describe('PlaylistSongJoin Management Update Component', () => {
  let comp: PlaylistSongJoinUpdateComponent;
  let fixture: ComponentFixture<PlaylistSongJoinUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let playlistSongJoinFormService: PlaylistSongJoinFormService;
  let playlistSongJoinService: PlaylistSongJoinService;
  let playlistService: PlaylistService;
  let songService: SongService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PlaylistSongJoinUpdateComponent],
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
      .overrideTemplate(PlaylistSongJoinUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlaylistSongJoinUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    playlistSongJoinFormService = TestBed.inject(PlaylistSongJoinFormService);
    playlistSongJoinService = TestBed.inject(PlaylistSongJoinService);
    playlistService = TestBed.inject(PlaylistService);
    songService = TestBed.inject(SongService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Playlist query and add missing value', () => {
      const playlistSongJoin: IPlaylistSongJoin = { id: 456 };
      const playlist: IPlaylist = { id: 56167 };
      playlistSongJoin.playlist = playlist;

      const playlistCollection: IPlaylist[] = [{ id: 86033 }];
      jest.spyOn(playlistService, 'query').mockReturnValue(of(new HttpResponse({ body: playlistCollection })));
      const additionalPlaylists = [playlist];
      const expectedCollection: IPlaylist[] = [...additionalPlaylists, ...playlistCollection];
      jest.spyOn(playlistService, 'addPlaylistToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ playlistSongJoin });
      comp.ngOnInit();

      expect(playlistService.query).toHaveBeenCalled();
      expect(playlistService.addPlaylistToCollectionIfMissing).toHaveBeenCalledWith(
        playlistCollection,
        ...additionalPlaylists.map(expect.objectContaining)
      );
      expect(comp.playlistsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Song query and add missing value', () => {
      const playlistSongJoin: IPlaylistSongJoin = { id: 456 };
      const song: ISong = { id: 50123 };
      playlistSongJoin.song = song;

      const songCollection: ISong[] = [{ id: 21271 }];
      jest.spyOn(songService, 'query').mockReturnValue(of(new HttpResponse({ body: songCollection })));
      const additionalSongs = [song];
      const expectedCollection: ISong[] = [...additionalSongs, ...songCollection];
      jest.spyOn(songService, 'addSongToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ playlistSongJoin });
      comp.ngOnInit();

      expect(songService.query).toHaveBeenCalled();
      expect(songService.addSongToCollectionIfMissing).toHaveBeenCalledWith(
        songCollection,
        ...additionalSongs.map(expect.objectContaining)
      );
      expect(comp.songsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const playlistSongJoin: IPlaylistSongJoin = { id: 456 };
      const playlist: IPlaylist = { id: 16750 };
      playlistSongJoin.playlist = playlist;
      const song: ISong = { id: 77584 };
      playlistSongJoin.song = song;

      activatedRoute.data = of({ playlistSongJoin });
      comp.ngOnInit();

      expect(comp.playlistsSharedCollection).toContain(playlist);
      expect(comp.songsSharedCollection).toContain(song);
      expect(comp.playlistSongJoin).toEqual(playlistSongJoin);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlaylistSongJoin>>();
      const playlistSongJoin = { id: 123 };
      jest.spyOn(playlistSongJoinFormService, 'getPlaylistSongJoin').mockReturnValue(playlistSongJoin);
      jest.spyOn(playlistSongJoinService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playlistSongJoin });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: playlistSongJoin }));
      saveSubject.complete();

      // THEN
      expect(playlistSongJoinFormService.getPlaylistSongJoin).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(playlistSongJoinService.update).toHaveBeenCalledWith(expect.objectContaining(playlistSongJoin));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlaylistSongJoin>>();
      const playlistSongJoin = { id: 123 };
      jest.spyOn(playlistSongJoinFormService, 'getPlaylistSongJoin').mockReturnValue({ id: null });
      jest.spyOn(playlistSongJoinService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playlistSongJoin: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: playlistSongJoin }));
      saveSubject.complete();

      // THEN
      expect(playlistSongJoinFormService.getPlaylistSongJoin).toHaveBeenCalled();
      expect(playlistSongJoinService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlaylistSongJoin>>();
      const playlistSongJoin = { id: 123 };
      jest.spyOn(playlistSongJoinService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playlistSongJoin });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(playlistSongJoinService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePlaylist', () => {
      it('Should forward to playlistService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(playlistService, 'comparePlaylist');
        comp.comparePlaylist(entity, entity2);
        expect(playlistService.comparePlaylist).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSong', () => {
      it('Should forward to songService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(songService, 'compareSong');
        comp.compareSong(entity, entity2);
        expect(songService.compareSong).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
