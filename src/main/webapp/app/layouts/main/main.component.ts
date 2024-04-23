import { Component, OnInit, Renderer2 } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router, ActivatedRouteSnapshot, NavigationEnd } from '@angular/router';
import { PreferencesService } from 'app/account/preferences/preferences.service';

import { AccountService } from 'app/core/auth/account.service';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';

@Component({
  selector: 'jhi-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss'],
})
export class MainComponent implements OnInit {
  constructor(
    private accountService: AccountService,
    private titleService: Title,
    private router: Router,
    private preferencesService: PreferencesService,
    private renderer: Renderer2
  ) {} // Inject Renderer2 for DOM manipulation

  ngOnInit(): void {
    // try to log in automatically
    this.accountService.identity().subscribe();

    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.updateTitle();
      }
    });

    // Get user preferences and apply styles
    this.preferencesService.getAppUser().subscribe(appUser => {
      // Apply styles based on user preferences
      if (appUser.highContrastMode) {
        this.renderer.addClass(document.body, 'high-contrast'); // Apply high contrast styles
        console.log('High contrast mode applied!');
      } else {
        this.renderer.removeClass(document.body, 'high-contrast'); // Remove high contrast styles
      }
    });
  }

  private getPageTitle(routeSnapshot: ActivatedRouteSnapshot): string {
    const title: string = routeSnapshot.data['pageTitle'] ?? '';
    if (routeSnapshot.firstChild) {
      return this.getPageTitle(routeSnapshot.firstChild) || title;
    }
    return title;
  }

  private updateTitle(): void {
    let pageTitle = this.getPageTitle(this.router.routerState.snapshot.root);
    if (!pageTitle) {
      pageTitle = 'Teamproject';
    }
    this.titleService.setTitle(pageTitle);
  }
}
