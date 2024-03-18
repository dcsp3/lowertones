import { Component, OnInit, OnDestroy, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { LocationService } from '../shared/location.service';
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
  private sectionIds = ['hero', 'tableview-tab', 'recapd-tab', 'network-tab', 'visualisations-tab', 'getstarted-tab'];

  constructor(private accountService: AccountService, private router: Router, private locationService: LocationService) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
    const totalParticles = 360 * 2; // Or any number you prefer
    this.particles = Array.from({ length: totalParticles }, (_, i) => i + 1);
  }

  @HostListener('window:scroll', ['$event'])
  onWindowScroll(): void {
    const currentSection = this.getCurrentSectionInView();
    if (currentSection) {
      console.log('Current section:', currentSection);
      this.locationService.setCurrentTab(currentSection);
    }
  }

  private getCurrentSectionInView(): string | null {
    const threshold = 450; // Pixels from the top of the viewport, adjust as needed
    for (const sectionId of this.sectionIds) {
      const section = document.getElementById(sectionId);
      if (section) {
        const rect = section.getBoundingClientRect();
        const isVisible = rect.top < window.innerHeight - threshold && rect.bottom > threshold;
        if (isVisible) {
          return sectionId;
        }
      }
    }
    return null;
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
