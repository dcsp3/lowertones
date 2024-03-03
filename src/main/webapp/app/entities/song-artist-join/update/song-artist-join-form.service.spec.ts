import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../song-artist-join.test-samples';

import { SongArtistJoinFormService } from './song-artist-join-form.service';

describe('SongArtistJoin Form Service', () => {
  let service: SongArtistJoinFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SongArtistJoinFormService);
  });

  describe('Service methods', () => {
    describe('createSongArtistJoinFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSongArtistJoinFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            topTrackIndex: expect.any(Object),
            song: expect.any(Object),
            mainArtist: expect.any(Object),
          })
        );
      });

      it('passing ISongArtistJoin should create a new form with FormGroup', () => {
        const formGroup = service.createSongArtistJoinFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            topTrackIndex: expect.any(Object),
            song: expect.any(Object),
            mainArtist: expect.any(Object),
          })
        );
      });
    });

    describe('getSongArtistJoin', () => {
      it('should return NewSongArtistJoin for default SongArtistJoin initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSongArtistJoinFormGroup(sampleWithNewData);

        const songArtistJoin = service.getSongArtistJoin(formGroup) as any;

        expect(songArtistJoin).toMatchObject(sampleWithNewData);
      });

      it('should return NewSongArtistJoin for empty SongArtistJoin initial value', () => {
        const formGroup = service.createSongArtistJoinFormGroup();

        const songArtistJoin = service.getSongArtistJoin(formGroup) as any;

        expect(songArtistJoin).toMatchObject({});
      });

      it('should return ISongArtistJoin', () => {
        const formGroup = service.createSongArtistJoinFormGroup(sampleWithRequiredData);

        const songArtistJoin = service.getSongArtistJoin(formGroup) as any;

        expect(songArtistJoin).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISongArtistJoin should not enable id FormControl', () => {
        const formGroup = service.createSongArtistJoinFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSongArtistJoin should disable id FormControl', () => {
        const formGroup = service.createSongArtistJoinFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
