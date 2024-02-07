import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../spotify-exchange-code.test-samples';

import { SpotifyExchangeCodeFormService } from './spotify-exchange-code-form.service';

describe('SpotifyExchangeCode Form Service', () => {
  let service: SpotifyExchangeCodeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SpotifyExchangeCodeFormService);
  });

  describe('Service methods', () => {
    describe('createSpotifyExchangeCodeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSpotifyExchangeCodeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });

      it('passing ISpotifyExchangeCode should create a new form with FormGroup', () => {
        const formGroup = service.createSpotifyExchangeCodeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });
    });

    describe('getSpotifyExchangeCode', () => {
      it('should return NewSpotifyExchangeCode for default SpotifyExchangeCode initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSpotifyExchangeCodeFormGroup(sampleWithNewData);

        const spotifyExchangeCode = service.getSpotifyExchangeCode(formGroup) as any;

        expect(spotifyExchangeCode).toMatchObject(sampleWithNewData);
      });

      it('should return NewSpotifyExchangeCode for empty SpotifyExchangeCode initial value', () => {
        const formGroup = service.createSpotifyExchangeCodeFormGroup();

        const spotifyExchangeCode = service.getSpotifyExchangeCode(formGroup) as any;

        expect(spotifyExchangeCode).toMatchObject({});
      });

      it('should return ISpotifyExchangeCode', () => {
        const formGroup = service.createSpotifyExchangeCodeFormGroup(sampleWithRequiredData);

        const spotifyExchangeCode = service.getSpotifyExchangeCode(formGroup) as any;

        expect(spotifyExchangeCode).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISpotifyExchangeCode should not enable id FormControl', () => {
        const formGroup = service.createSpotifyExchangeCodeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSpotifyExchangeCode should disable id FormControl', () => {
        const formGroup = service.createSpotifyExchangeCodeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
