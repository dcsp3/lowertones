import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IVault, NewVault } from '../vault.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVault for edit and NewVaultFormGroupInput for create.
 */
type VaultFormGroupInput = IVault | PartialWithRequiredKeyOf<NewVault>;

type VaultFormDefaults = Pick<NewVault, 'id'>;

type VaultFormGroupContent = {
  id: FormControl<IVault['id'] | NewVault['id']>;
  sourcePlaylistID: FormControl<IVault['sourcePlaylistID']>;
  playlistName: FormControl<IVault['playlistName']>;
  resultPlaylistID: FormControl<IVault['resultPlaylistID']>;
  frequency: FormControl<IVault['frequency']>;
  type: FormControl<IVault['type']>;
  playlistCoverURL: FormControl<IVault['playlistCoverURL']>;
  playlistSnapshotID: FormControl<IVault['playlistSnapshotID']>;
};

export type VaultFormGroup = FormGroup<VaultFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VaultFormService {
  createVaultFormGroup(vault: VaultFormGroupInput = { id: null }): VaultFormGroup {
    const vaultRawValue = {
      ...this.getFormDefaults(),
      ...vault,
    };
    return new FormGroup<VaultFormGroupContent>({
      id: new FormControl(
        { value: vaultRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      sourcePlaylistID: new FormControl(vaultRawValue.sourcePlaylistID),
      playlistName: new FormControl(vaultRawValue.playlistName),
      resultPlaylistID: new FormControl(vaultRawValue.resultPlaylistID),
      frequency: new FormControl(vaultRawValue.frequency),
      type: new FormControl(vaultRawValue.type),
      playlistCoverURL: new FormControl(vaultRawValue.playlistCoverURL),
      playlistSnapshotID: new FormControl(vaultRawValue.playlistSnapshotID),
    });
  }

  getVault(form: VaultFormGroup): IVault | NewVault {
    return form.getRawValue() as IVault | NewVault;
  }

  resetForm(form: VaultFormGroup, vault: VaultFormGroupInput): void {
    const vaultRawValue = { ...this.getFormDefaults(), ...vault };
    form.reset(
      {
        ...vaultRawValue,
        id: { value: vaultRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): VaultFormDefaults {
    return {
      id: null,
    };
  }
}
