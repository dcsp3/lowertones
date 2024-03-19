import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../related-artists.test-samples';

import { RelatedArtistsFormService } from './related-artists-form.service';

describe('RelatedArtists Form Service', () => {
  let service: RelatedArtistsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RelatedArtistsFormService);
  });

  describe('Service methods', () => {
    describe('createRelatedArtistsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRelatedArtistsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            mainArtistSptfyID: expect.any(Object),
            relatedArtistSpotifyID1: expect.any(Object),
            relatedArtistSpotifyID2: expect.any(Object),
            relatedArtistSpotifyID3: expect.any(Object),
            relatedArtistSpotifyID4: expect.any(Object),
            relatedArtistSpotifyID5: expect.any(Object),
            relatedArtistSpotifyID6: expect.any(Object),
            relatedArtistSpotifyID7: expect.any(Object),
            relatedArtistSpotifyID8: expect.any(Object),
            relatedArtistSpotifyID9: expect.any(Object),
            relatedArtistSpotifyID10: expect.any(Object),
            relatedArtistSpotifyID11: expect.any(Object),
            relatedArtistSpotifyID12: expect.any(Object),
            relatedArtistSpotifyID13: expect.any(Object),
            relatedArtistSpotifyID14: expect.any(Object),
            relatedArtistSpotifyID15: expect.any(Object),
            relatedArtistSpotifyID16: expect.any(Object),
            relatedArtistSpotifyID17: expect.any(Object),
            relatedArtistSpotifyID18: expect.any(Object),
            relatedArtistSpotifyID19: expect.any(Object),
            relatedArtistSpotifyID20: expect.any(Object),
          })
        );
      });

      it('passing IRelatedArtists should create a new form with FormGroup', () => {
        const formGroup = service.createRelatedArtistsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            mainArtistSptfyID: expect.any(Object),
            relatedArtistSpotifyID1: expect.any(Object),
            relatedArtistSpotifyID2: expect.any(Object),
            relatedArtistSpotifyID3: expect.any(Object),
            relatedArtistSpotifyID4: expect.any(Object),
            relatedArtistSpotifyID5: expect.any(Object),
            relatedArtistSpotifyID6: expect.any(Object),
            relatedArtistSpotifyID7: expect.any(Object),
            relatedArtistSpotifyID8: expect.any(Object),
            relatedArtistSpotifyID9: expect.any(Object),
            relatedArtistSpotifyID10: expect.any(Object),
            relatedArtistSpotifyID11: expect.any(Object),
            relatedArtistSpotifyID12: expect.any(Object),
            relatedArtistSpotifyID13: expect.any(Object),
            relatedArtistSpotifyID14: expect.any(Object),
            relatedArtistSpotifyID15: expect.any(Object),
            relatedArtistSpotifyID16: expect.any(Object),
            relatedArtistSpotifyID17: expect.any(Object),
            relatedArtistSpotifyID18: expect.any(Object),
            relatedArtistSpotifyID19: expect.any(Object),
            relatedArtistSpotifyID20: expect.any(Object),
          })
        );
      });
    });

    describe('getRelatedArtists', () => {
      it('should return NewRelatedArtists for default RelatedArtists initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createRelatedArtistsFormGroup(sampleWithNewData);

        const relatedArtists = service.getRelatedArtists(formGroup) as any;

        expect(relatedArtists).toMatchObject(sampleWithNewData);
      });

      it('should return NewRelatedArtists for empty RelatedArtists initial value', () => {
        const formGroup = service.createRelatedArtistsFormGroup();

        const relatedArtists = service.getRelatedArtists(formGroup) as any;

        expect(relatedArtists).toMatchObject({});
      });

      it('should return IRelatedArtists', () => {
        const formGroup = service.createRelatedArtistsFormGroup(sampleWithRequiredData);

        const relatedArtists = service.getRelatedArtists(formGroup) as any;

        expect(relatedArtists).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRelatedArtists should not enable id FormControl', () => {
        const formGroup = service.createRelatedArtistsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRelatedArtists should disable id FormControl', () => {
        const formGroup = service.createRelatedArtistsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
