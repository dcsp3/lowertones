import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../main-artist.test-samples';

import { MainArtistFormService } from './main-artist-form.service';

describe('MainArtist Form Service', () => {
  let service: MainArtistFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MainArtistFormService);
  });

  describe('Service methods', () => {
    describe('createMainArtistFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMainArtistFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            artistSpotifyID: expect.any(Object),
            artistName: expect.any(Object),
            artistPopularity: expect.any(Object),
            artistImageSmall: expect.any(Object),
            artistImageMedium: expect.any(Object),
            artistImageLarge: expect.any(Object),
            artistFollowers: expect.any(Object),
            dateAddedToDB: expect.any(Object),
            dateLastModified: expect.any(Object),
            musicbrainzID: expect.any(Object),
            relatedArtists: expect.any(Object),
            albums: expect.any(Object),
          })
        );
      });

      it('passing IMainArtist should create a new form with FormGroup', () => {
        const formGroup = service.createMainArtistFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            artistSpotifyID: expect.any(Object),
            artistName: expect.any(Object),
            artistPopularity: expect.any(Object),
            artistImageSmall: expect.any(Object),
            artistImageMedium: expect.any(Object),
            artistImageLarge: expect.any(Object),
            artistFollowers: expect.any(Object),
            dateAddedToDB: expect.any(Object),
            dateLastModified: expect.any(Object),
            musicbrainzID: expect.any(Object),
            relatedArtists: expect.any(Object),
            albums: expect.any(Object),
          })
        );
      });
    });

    describe('getMainArtist', () => {
      it('should return NewMainArtist for default MainArtist initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createMainArtistFormGroup(sampleWithNewData);

        const mainArtist = service.getMainArtist(formGroup) as any;

        expect(mainArtist).toMatchObject(sampleWithNewData);
      });

      it('should return NewMainArtist for empty MainArtist initial value', () => {
        const formGroup = service.createMainArtistFormGroup();

        const mainArtist = service.getMainArtist(formGroup) as any;

        expect(mainArtist).toMatchObject({});
      });

      it('should return IMainArtist', () => {
        const formGroup = service.createMainArtistFormGroup(sampleWithRequiredData);

        const mainArtist = service.getMainArtist(formGroup) as any;

        expect(mainArtist).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMainArtist should not enable id FormControl', () => {
        const formGroup = service.createMainArtistFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMainArtist should disable id FormControl', () => {
        const formGroup = service.createMainArtistFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
