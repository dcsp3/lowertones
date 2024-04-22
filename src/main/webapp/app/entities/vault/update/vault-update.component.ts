import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { VaultFormService, VaultFormGroup } from './vault-form.service';
import { IVault } from '../vault.model';
import { VaultService } from '../service/vault.service';

@Component({
  selector: 'jhi-vault-update',
  templateUrl: './vault-update.component.html',
})
export class VaultUpdateComponent implements OnInit {
  isSaving = false;
  vault: IVault | null = null;

  editForm: VaultFormGroup = this.vaultFormService.createVaultFormGroup();

  constructor(
    protected vaultService: VaultService,
    protected vaultFormService: VaultFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vault }) => {
      this.vault = vault;
      if (vault) {
        this.updateForm(vault);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vault = this.vaultFormService.getVault(this.editForm);
    if (vault.id !== null) {
      this.subscribeToSaveResponse(this.vaultService.update(vault));
    } else {
      this.subscribeToSaveResponse(this.vaultService.create(vault));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVault>>): void {
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

  protected updateForm(vault: IVault): void {
    this.vault = vault;
    this.vaultFormService.resetForm(this.editForm, vault);
  }
}
