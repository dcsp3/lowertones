import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../playlist-song-join.test-samples';

import { PlaylistSongJoinFormService } from './playlist-song-join-form.service';

describe('PlaylistSongJoin Form Service', () => {
  let service: PlaylistSongJoinFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PlaylistSongJoinFormService);
  });

  describe('Service methods', () => {
    describe('createPlaylistSongJoinFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPlaylistSongJoinFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            songOrderIndex: expect.any(Object),
            songDateAdded: expect.any(Object),
            playlist: expect.any(Object),
            song: expect.any(Object),
          })
        );
      });

      it('passing IPlaylistSongJoin should create a new form with FormGroup', () => {
        const formGroup = service.createPlaylistSongJoinFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            songOrderIndex: expect.any(Object),
            songDateAdded: expect.any(Object),
            playlist: expect.any(Object),
            song: expect.any(Object),
          })
        );
      });
    });

    describe('getPlaylistSongJoin', () => {
      it('should return NewPlaylistSongJoin for default PlaylistSongJoin initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPlaylistSongJoinFormGroup(sampleWithNewData);

        const playlistSongJoin = service.getPlaylistSongJoin(formGroup) as any;

        expect(playlistSongJoin).toMatchObject(sampleWithNewData);
      });

      it('should return NewPlaylistSongJoin for empty PlaylistSongJoin initial value', () => {
        const formGroup = service.createPlaylistSongJoinFormGroup();

        const playlistSongJoin = service.getPlaylistSongJoin(formGroup) as any;

        expect(playlistSongJoin).toMatchObject({});
      });

      it('should return IPlaylistSongJoin', () => {
        const formGroup = service.createPlaylistSongJoinFormGroup(sampleWithRequiredData);

        const playlistSongJoin = service.getPlaylistSongJoin(formGroup) as any;

        expect(playlistSongJoin).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPlaylistSongJoin should not enable id FormControl', () => {
        const formGroup = service.createPlaylistSongJoinFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPlaylistSongJoin should disable id FormControl', () => {
        const formGroup = service.createPlaylistSongJoinFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
