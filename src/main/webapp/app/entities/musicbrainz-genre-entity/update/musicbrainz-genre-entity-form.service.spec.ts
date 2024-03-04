import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../musicbrainz-genre-entity.test-samples';

import { MusicbrainzGenreEntityFormService } from './musicbrainz-genre-entity-form.service';

describe('MusicbrainzGenreEntity Form Service', () => {
  let service: MusicbrainzGenreEntityFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MusicbrainzGenreEntityFormService);
  });

  describe('Service methods', () => {
    describe('createMusicbrainzGenreEntityFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMusicbrainzGenreEntityFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            musicbrainzGenre: expect.any(Object),
            song: expect.any(Object),
            album: expect.any(Object),
            mainArtist: expect.any(Object),
          })
        );
      });

      it('passing IMusicbrainzGenreEntity should create a new form with FormGroup', () => {
        const formGroup = service.createMusicbrainzGenreEntityFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            musicbrainzGenre: expect.any(Object),
            song: expect.any(Object),
            album: expect.any(Object),
            mainArtist: expect.any(Object),
          })
        );
      });
    });

    describe('getMusicbrainzGenreEntity', () => {
      it('should return NewMusicbrainzGenreEntity for default MusicbrainzGenreEntity initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createMusicbrainzGenreEntityFormGroup(sampleWithNewData);

        const musicbrainzGenreEntity = service.getMusicbrainzGenreEntity(formGroup) as any;

        expect(musicbrainzGenreEntity).toMatchObject(sampleWithNewData);
      });

      it('should return NewMusicbrainzGenreEntity for empty MusicbrainzGenreEntity initial value', () => {
        const formGroup = service.createMusicbrainzGenreEntityFormGroup();

        const musicbrainzGenreEntity = service.getMusicbrainzGenreEntity(formGroup) as any;

        expect(musicbrainzGenreEntity).toMatchObject({});
      });

      it('should return IMusicbrainzGenreEntity', () => {
        const formGroup = service.createMusicbrainzGenreEntityFormGroup(sampleWithRequiredData);

        const musicbrainzGenreEntity = service.getMusicbrainzGenreEntity(formGroup) as any;

        expect(musicbrainzGenreEntity).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMusicbrainzGenreEntity should not enable id FormControl', () => {
        const formGroup = service.createMusicbrainzGenreEntityFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMusicbrainzGenreEntity should disable id FormControl', () => {
        const formGroup = service.createMusicbrainzGenreEntityFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
