import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ContributorFormService, ContributorFormGroup } from './contributor-form.service';
import { IContributor } from '../contributor.model';
import { ContributorService } from '../service/contributor.service';

@Component({
  selector: 'jhi-contributor-update',
  templateUrl: './contributor-update.component.html',
})
export class ContributorUpdateComponent implements OnInit {
  isSaving = false;
  contributor: IContributor | null = null;

  editForm: ContributorFormGroup = this.contributorFormService.createContributorFormGroup();

  constructor(
    protected contributorService: ContributorService,
    protected contributorFormService: ContributorFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contributor }) => {
      this.contributor = contributor;
      if (contributor) {
        this.updateForm(contributor);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contributor = this.contributorFormService.getContributor(this.editForm);
    if (contributor.id !== null) {
      this.subscribeToSaveResponse(this.contributorService.update(contributor));
    } else {
      this.subscribeToSaveResponse(this.contributorService.create(contributor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContributor>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(contributor: IContributor): void {
    this.contributor = contributor;
    this.contributorFormService.resetForm(this.editForm, contributor);
  }
}
