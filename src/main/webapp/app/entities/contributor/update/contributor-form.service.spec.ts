import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../contributor.test-samples';

import { ContributorFormService } from './contributor-form.service';

describe('Contributor Form Service', () => {
  let service: ContributorFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ContributorFormService);
  });

  describe('Service methods', () => {
    describe('createContributorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createContributorFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            role: expect.any(Object),
            instrument: expect.any(Object),
            musicbrainzID: expect.any(Object),
            song: expect.any(Object),
          })
        );
      });

      it('passing IContributor should create a new form with FormGroup', () => {
        const formGroup = service.createContributorFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            role: expect.any(Object),
            instrument: expect.any(Object),
            musicbrainzID: expect.any(Object),
            song: expect.any(Object),
          })
        );
      });
    });

    describe('getContributor', () => {
      it('should return NewContributor for default Contributor initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createContributorFormGroup(sampleWithNewData);

        const contributor = service.getContributor(formGroup) as any;

        expect(contributor).toMatchObject(sampleWithNewData);
      });

      it('should return NewContributor for empty Contributor initial value', () => {
        const formGroup = service.createContributorFormGroup();

        const contributor = service.getContributor(formGroup) as any;

        expect(contributor).toMatchObject({});
      });

      it('should return IContributor', () => {
        const formGroup = service.createContributorFormGroup(sampleWithRequiredData);

        const contributor = service.getContributor(formGroup) as any;

        expect(contributor).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IContributor should not enable id FormControl', () => {
        const formGroup = service.createContributorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewContributor should disable id FormControl', () => {
        const formGroup = service.createContributorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
