import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IContributor, NewContributor } from '../contributor.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContributor for edit and NewContributorFormGroupInput for create.
 */
type ContributorFormGroupInput = IContributor | PartialWithRequiredKeyOf<NewContributor>;

type ContributorFormDefaults = Pick<NewContributor, 'id' | 'songs'>;

type ContributorFormGroupContent = {
  id: FormControl<IContributor['id'] | NewContributor['id']>;
  name: FormControl<IContributor['name']>;
  role: FormControl<IContributor['role']>;
  instrument: FormControl<IContributor['instrument']>;
  musicbrainzID: FormControl<IContributor['musicbrainzID']>;
  songs: FormControl<IContributor['songs']>;
};

export type ContributorFormGroup = FormGroup<ContributorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContributorFormService {
  createContributorFormGroup(contributor: ContributorFormGroupInput = { id: null }): ContributorFormGroup {
    const contributorRawValue = {
      ...this.getFormDefaults(),
      ...contributor,
    };
    return new FormGroup<ContributorFormGroupContent>({
      id: new FormControl(
        { value: contributorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(contributorRawValue.name),
      role: new FormControl(contributorRawValue.role),
      instrument: new FormControl(contributorRawValue.instrument),
      musicbrainzID: new FormControl(contributorRawValue.musicbrainzID),
      songs: new FormControl(contributorRawValue.songs ?? []),
    });
  }

  getContributor(form: ContributorFormGroup): IContributor | NewContributor {
    return form.getRawValue() as IContributor | NewContributor;
  }

  resetForm(form: ContributorFormGroup, contributor: ContributorFormGroupInput): void {
    const contributorRawValue = { ...this.getFormDefaults(), ...contributor };
    form.reset(
      {
        ...contributorRawValue,
        id: { value: contributorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ContributorFormDefaults {
    return {
      id: null,
      songs: [],
    };
  }
}
