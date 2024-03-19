import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRelatedArtists, NewRelatedArtists } from '../related-artists.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRelatedArtists for edit and NewRelatedArtistsFormGroupInput for create.
 */
type RelatedArtistsFormGroupInput = IRelatedArtists | PartialWithRequiredKeyOf<NewRelatedArtists>;

type RelatedArtistsFormDefaults = Pick<NewRelatedArtists, 'id'>;

type RelatedArtistsFormGroupContent = {
  id: FormControl<IRelatedArtists['id'] | NewRelatedArtists['id']>;
  mainArtistSptfyID: FormControl<IRelatedArtists['mainArtistSptfyID']>;
  relatedArtistSpotifyID1: FormControl<IRelatedArtists['relatedArtistSpotifyID1']>;
  relatedArtistSpotifyID2: FormControl<IRelatedArtists['relatedArtistSpotifyID2']>;
  relatedArtistSpotifyID3: FormControl<IRelatedArtists['relatedArtistSpotifyID3']>;
  relatedArtistSpotifyID4: FormControl<IRelatedArtists['relatedArtistSpotifyID4']>;
  relatedArtistSpotifyID5: FormControl<IRelatedArtists['relatedArtistSpotifyID5']>;
  relatedArtistSpotifyID6: FormControl<IRelatedArtists['relatedArtistSpotifyID6']>;
  relatedArtistSpotifyID7: FormControl<IRelatedArtists['relatedArtistSpotifyID7']>;
  relatedArtistSpotifyID8: FormControl<IRelatedArtists['relatedArtistSpotifyID8']>;
  relatedArtistSpotifyID9: FormControl<IRelatedArtists['relatedArtistSpotifyID9']>;
  relatedArtistSpotifyID10: FormControl<IRelatedArtists['relatedArtistSpotifyID10']>;
  relatedArtistSpotifyID11: FormControl<IRelatedArtists['relatedArtistSpotifyID11']>;
  relatedArtistSpotifyID12: FormControl<IRelatedArtists['relatedArtistSpotifyID12']>;
  relatedArtistSpotifyID13: FormControl<IRelatedArtists['relatedArtistSpotifyID13']>;
  relatedArtistSpotifyID14: FormControl<IRelatedArtists['relatedArtistSpotifyID14']>;
  relatedArtistSpotifyID15: FormControl<IRelatedArtists['relatedArtistSpotifyID15']>;
  relatedArtistSpotifyID16: FormControl<IRelatedArtists['relatedArtistSpotifyID16']>;
  relatedArtistSpotifyID17: FormControl<IRelatedArtists['relatedArtistSpotifyID17']>;
  relatedArtistSpotifyID18: FormControl<IRelatedArtists['relatedArtistSpotifyID18']>;
  relatedArtistSpotifyID19: FormControl<IRelatedArtists['relatedArtistSpotifyID19']>;
  relatedArtistSpotifyID20: FormControl<IRelatedArtists['relatedArtistSpotifyID20']>;
};

export type RelatedArtistsFormGroup = FormGroup<RelatedArtistsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RelatedArtistsFormService {
  createRelatedArtistsFormGroup(relatedArtists: RelatedArtistsFormGroupInput = { id: null }): RelatedArtistsFormGroup {
    const relatedArtistsRawValue = {
      ...this.getFormDefaults(),
      ...relatedArtists,
    };
    return new FormGroup<RelatedArtistsFormGroupContent>({
      id: new FormControl(
        { value: relatedArtistsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      mainArtistSptfyID: new FormControl(relatedArtistsRawValue.mainArtistSptfyID, {
        validators: [Validators.required],
      }),
      relatedArtistSpotifyID1: new FormControl(relatedArtistsRawValue.relatedArtistSpotifyID1, {
        validators: [Validators.required],
      }),
      relatedArtistSpotifyID2: new FormControl(relatedArtistsRawValue.relatedArtistSpotifyID2, {
        validators: [Validators.required],
      }),
      relatedArtistSpotifyID3: new FormControl(relatedArtistsRawValue.relatedArtistSpotifyID3, {
        validators: [Validators.required],
      }),
      relatedArtistSpotifyID4: new FormControl(relatedArtistsRawValue.relatedArtistSpotifyID4, {
        validators: [Validators.required],
      }),
      relatedArtistSpotifyID5: new FormControl(relatedArtistsRawValue.relatedArtistSpotifyID5, {
        validators: [Validators.required],
      }),
      relatedArtistSpotifyID6: new FormControl(relatedArtistsRawValue.relatedArtistSpotifyID6, {
        validators: [Validators.required],
      }),
      relatedArtistSpotifyID7: new FormControl(relatedArtistsRawValue.relatedArtistSpotifyID7, {
        validators: [Validators.required],
      }),
      relatedArtistSpotifyID8: new FormControl(relatedArtistsRawValue.relatedArtistSpotifyID8, {
        validators: [Validators.required],
      }),
      relatedArtistSpotifyID9: new FormControl(relatedArtistsRawValue.relatedArtistSpotifyID9, {
        validators: [Validators.required],
      }),
      relatedArtistSpotifyID10: new FormControl(relatedArtistsRawValue.relatedArtistSpotifyID10, {
        validators: [Validators.required],
      }),
      relatedArtistSpotifyID11: new FormControl(relatedArtistsRawValue.relatedArtistSpotifyID11, {
        validators: [Validators.required],
      }),
      relatedArtistSpotifyID12: new FormControl(relatedArtistsRawValue.relatedArtistSpotifyID12, {
        validators: [Validators.required],
      }),
      relatedArtistSpotifyID13: new FormControl(relatedArtistsRawValue.relatedArtistSpotifyID13, {
        validators: [Validators.required],
      }),
      relatedArtistSpotifyID14: new FormControl(relatedArtistsRawValue.relatedArtistSpotifyID14, {
        validators: [Validators.required],
      }),
      relatedArtistSpotifyID15: new FormControl(relatedArtistsRawValue.relatedArtistSpotifyID15, {
        validators: [Validators.required],
      }),
      relatedArtistSpotifyID16: new FormControl(relatedArtistsRawValue.relatedArtistSpotifyID16, {
        validators: [Validators.required],
      }),
      relatedArtistSpotifyID17: new FormControl(relatedArtistsRawValue.relatedArtistSpotifyID17, {
        validators: [Validators.required],
      }),
      relatedArtistSpotifyID18: new FormControl(relatedArtistsRawValue.relatedArtistSpotifyID18, {
        validators: [Validators.required],
      }),
      relatedArtistSpotifyID19: new FormControl(relatedArtistsRawValue.relatedArtistSpotifyID19, {
        validators: [Validators.required],
      }),
      relatedArtistSpotifyID20: new FormControl(relatedArtistsRawValue.relatedArtistSpotifyID20, {
        validators: [Validators.required],
      }),
    });
  }

  getRelatedArtists(form: RelatedArtistsFormGroup): IRelatedArtists | NewRelatedArtists {
    return form.getRawValue() as IRelatedArtists | NewRelatedArtists;
  }

  resetForm(form: RelatedArtistsFormGroup, relatedArtists: RelatedArtistsFormGroupInput): void {
    const relatedArtistsRawValue = { ...this.getFormDefaults(), ...relatedArtists };
    form.reset(
      {
        ...relatedArtistsRawValue,
        id: { value: relatedArtistsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RelatedArtistsFormDefaults {
    return {
      id: null,
    };
  }
}
