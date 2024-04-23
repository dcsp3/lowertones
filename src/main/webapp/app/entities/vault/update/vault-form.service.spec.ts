import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../vault.test-samples';

import { VaultFormService } from './vault-form.service';

describe('Vault Form Service', () => {
  let service: VaultFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VaultFormService);
  });

  describe('Service methods', () => {
    describe('createVaultFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVaultFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            userId: expect.any(Object),
            sourcePlaylistID: expect.any(Object),
            playlistName: expect.any(Object),
            resultPlaylistID: expect.any(Object),
            frequency: expect.any(Object),
            type: expect.any(Object),
            playlistCoverURL: expect.any(Object),
            playlistSnapshotID: expect.any(Object),
            dateLastUpdated: expect.any(Object),
          })
        );
      });

      it('passing IVault should create a new form with FormGroup', () => {
        const formGroup = service.createVaultFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            userId: expect.any(Object),
            sourcePlaylistID: expect.any(Object),
            playlistName: expect.any(Object),
            resultPlaylistID: expect.any(Object),
            frequency: expect.any(Object),
            type: expect.any(Object),
            playlistCoverURL: expect.any(Object),
            playlistSnapshotID: expect.any(Object),
            dateLastUpdated: expect.any(Object),
          })
        );
      });
    });

    describe('getVault', () => {
      it('should return NewVault for default Vault initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createVaultFormGroup(sampleWithNewData);

        const vault = service.getVault(formGroup) as any;

        expect(vault).toMatchObject(sampleWithNewData);
      });

      it('should return NewVault for empty Vault initial value', () => {
        const formGroup = service.createVaultFormGroup();

        const vault = service.getVault(formGroup) as any;

        expect(vault).toMatchObject({});
      });

      it('should return IVault', () => {
        const formGroup = service.createVaultFormGroup(sampleWithRequiredData);

        const vault = service.getVault(formGroup) as any;

        expect(vault).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVault should not enable id FormControl', () => {
        const formGroup = service.createVaultFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVault should disable id FormControl', () => {
        const formGroup = service.createVaultFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
