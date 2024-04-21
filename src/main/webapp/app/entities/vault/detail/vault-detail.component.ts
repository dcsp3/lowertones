import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVault } from '../vault.model';

@Component({
  selector: 'jhi-vault-detail',
  templateUrl: './vault-detail.component.html',
})
export class VaultDetailComponent implements OnInit {
  vault: IVault | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vault }) => {
      this.vault = vault;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
