import { Component, OnInit } from '@angular/core';
import { VaultService } from './vault.service';

import { VaultRequest } from './models';

@Component({
  selector: 'jhi-playlist-vault',
  templateUrl: './playlist-vault.component.html',
  styleUrls: ['./playlist-vault.component.scss'],
})
export class PlaylistVaultComponent implements OnInit {
  vaults: any[] = [];
  playlistName: string = '';
  playlistId: string | undefined = '';
  link: string = '';
  playlistCover: string = '';
  frequency: string = 'weekly';

  showToast: boolean = false;
  toastMessage: string = '';
  toastSuccess: boolean = false;
  toastError: boolean = false;

  tableLength: number = 0;

  constructor(private vaultService: VaultService) {}

  ngOnInit() {
    this.loadVaults();
  }

  loadVaults() {
    this.vaultService.getVaultsByUserId().subscribe(data => {
      this.vaults = data;
      this.tableLength = this.vaults.length;
    });
  }

  showCustomToast(message: string, success: boolean = false, error: boolean = false) {
    this.toastMessage = message;
    this.toastSuccess = success;
    this.toastError = error;
    this.showToast = true;

    setTimeout(() => {
      this.showToast = false;
    }, 3000);
  }

  addVault() {
    if (!this.playlistName || !this.link) {
      console.error('Required fields are missing');
      this.showCustomToast('Required fields are missing', false, true);
      return;
    }

    try {
      this.playlistId = new URL(this.link).pathname.split('/').pop();
    } catch {
      this.showCustomToast('Please enter a valid link', false, true);
    }

    const request: VaultRequest = {
      playlistName: this.playlistName,
      sourcePlaylistId: this.playlistId!,
      frequency: this.frequency,
    };

    this.vaultService.createVault(request).subscribe({
      next: data => {
        this.showCustomToast('Successfully added to Vault!', true, false);
        this.loadVaults();
      },
      error: error => {
        if (!this.playlistId) {
          this.showCustomToast('Please enter a valid link', false, true);
        } else {
          this.showCustomToast('Error adding to Vault, please try again', false, true);
        }
      },
      complete: () => {
        console.log('Complete');
      },
    });
  }

  deleteVault(id: number) {
    this.vaultService.deleteVault(id).subscribe(() => {
      this.loadVaults();
      this.showCustomToast('Deleted Successfully', true, false);
    });
  }
}
