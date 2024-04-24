import { Component, OnInit } from '@angular/core';
import { PreferencesService } from 'app/account/preferences/preferences.service';

@Component({
  selector: 'jhi-gdpr',
  templateUrl: './gdpr.component.html',
  styleUrls: ['./gdpr.component.scss'],
})
export class GDPRComponent implements OnInit {
  highContrastMode = false;

  constructor(private preferencesService: PreferencesService) {}

  ngOnInit(): void {
    this.preferencesService.getHighContrast().subscribe(
      highContrast => {
        this.highContrastMode = highContrast;
        console.log('High contrast: ' + highContrast);
      },
      error => {
        console.error('Error retrieving high contrast mode:', error);
      }
    );
  }
}
