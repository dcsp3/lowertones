import { Component, OnInit, Renderer2 } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router, ActivatedRouteSnapshot, NavigationEnd } from '@angular/router';
import { PreferencesService } from 'app/account/preferences/preferences.service';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss'],
})
export class MainComponent implements OnInit {
  // Define the list of all CSS selectors representing elements to style
  elementsToStyle: string[] = ['.navbar', '.footer', '.container', '.text-container', '.card'];

  constructor(
    private accountService: AccountService,
    private titleService: Title,
    private router: Router,
    private preferencesService: PreferencesService,
    private renderer: Renderer2
  ) {}

  ngOnInit(): void {
    // try to log in automatically
    this.accountService.identity().subscribe();

    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.updateTitle();
      }
    });

    this.checkHighContrast();
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
    this.checkHighContrast();
  }

  private checkHighContrast(): void {
    // Apply or remove high contrast to the CSS of the current page
    this.preferencesService.getHighContrast().subscribe(
      highContrast => {
        // Apply or remove highContrast class to the body element based on the condition
        if (highContrast) {
          this.applyHighContrast();
        } else {
          this.removeHighContrast();
        }
      },
      error => {
        this.removeHighContrast();
      }
    );
  }

  private applyHighContrast(): void {
    this.elementsToStyle.forEach(selector => {
      const element = document.querySelector(selector);
      if (element) {
        element.classList.add('highContrast');
      }
    });
    console.log('Applied high contrast');
  }

  private removeHighContrast(): void {
    this.elementsToStyle.forEach(selector => {
      const element = document.querySelector(selector);
      if (element) {
        element.classList.remove('highContrast');
      }
    });
    console.log('Removed high contrast');
  }
}
