import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../spotify-genre-entity.test-samples';

import { SpotifyGenreEntityFormService } from './spotify-genre-entity-form.service';

describe('SpotifyGenreEntity Form Service', () => {
  let service: SpotifyGenreEntityFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SpotifyGenreEntityFormService);
  });

  describe('Service methods', () => {
    describe('createSpotifyGenreEntityFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSpotifyGenreEntityFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            spotifyGenre: expect.any(Object),
            song: expect.any(Object),
            album: expect.any(Object),
            mainArtist: expect.any(Object),
          })
        );
      });

      it('passing ISpotifyGenreEntity should create a new form with FormGroup', () => {
        const formGroup = service.createSpotifyGenreEntityFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            spotifyGenre: expect.any(Object),
            song: expect.any(Object),
            album: expect.any(Object),
            mainArtist: expect.any(Object),
          })
        );
      });
    });

    describe('getSpotifyGenreEntity', () => {
      it('should return NewSpotifyGenreEntity for default SpotifyGenreEntity initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSpotifyGenreEntityFormGroup(sampleWithNewData);

        const spotifyGenreEntity = service.getSpotifyGenreEntity(formGroup) as any;

        expect(spotifyGenreEntity).toMatchObject(sampleWithNewData);
      });

      it('should return NewSpotifyGenreEntity for empty SpotifyGenreEntity initial value', () => {
        const formGroup = service.createSpotifyGenreEntityFormGroup();

        const spotifyGenreEntity = service.getSpotifyGenreEntity(formGroup) as any;

        expect(spotifyGenreEntity).toMatchObject({});
      });

      it('should return ISpotifyGenreEntity', () => {
        const formGroup = service.createSpotifyGenreEntityFormGroup(sampleWithRequiredData);

        const spotifyGenreEntity = service.getSpotifyGenreEntity(formGroup) as any;

        expect(spotifyGenreEntity).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISpotifyGenreEntity should not enable id FormControl', () => {
        const formGroup = service.createSpotifyGenreEntityFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSpotifyGenreEntity should disable id FormControl', () => {
        const formGroup = service.createSpotifyGenreEntityFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
