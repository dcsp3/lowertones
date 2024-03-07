import { Component, OnInit, OnDestroy, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { LocationService } from '../shared/location.service'; // Update this path

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  particles: number[] = [];
  private readonly destroy$ = new Subject<void>();

  constructor(private accountService: AccountService, private router: Router, private locationService: LocationService) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
    const totalParticles = 360 * 2; // Or any number you prefer
    this.particles = Array.from({ length: totalParticles }, (_, i) => i + 1);
    this.locationService
      .getCurrentTab()
      .pipe(takeUntil(this.destroy$))
      .subscribe(tabId => {
        // Perform actions based on the current tab, if needed
        console.log(`Current tab is ${tabId}`);
      });
  }

  navigateToSection(sectionId: string): void {
    this.locationService.setCurrentTab(sectionId);
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
