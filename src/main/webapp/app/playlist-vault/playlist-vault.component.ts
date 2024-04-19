import { Component } from '@angular/core';

@Component({
  selector: 'jhi-playlist-vault',
  templateUrl: './playlist-vault.component.html',
  styleUrls: ['./playlist-vault.component.scss'],
})
export class PlaylistVaultComponent {
  selectedFrequency: string = 'Updates Every Monday'; // default frequency display

  activeTab: 'custom' | 'spotify' = 'custom';

  setActiveTab(tabName: 'custom' | 'spotify'): void {
    this.activeTab = tabName;
  }

  onPlaylistTypeChange(event: Event): void {
    const selectElement = event.target as HTMLSelectElement;
    const value = selectElement.value;
    if (value === 'Discover Weekly') {
      this.selectedFrequency = 'Updates Every Monday';
    } else if (value === 'Release Radar') {
      this.selectedFrequency = 'Updates Every Friday';
    } else {
      this.selectedFrequency = 'Updates Daily';
    }
    // Make sure to update your template or add additional logic if needed

    // Add other conditions for different types
  }
}
