import { Component, OnInit } from '@angular/core';
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
  highContrastElements: string[] = [
    '.navbar',
    '.footer',
    '.container',
    '.text-container',
    '.top-section',
    '.et-main',
    '.tabs-container',
    '.et-main',
    '.card',
    '.elements',
    '.description-text',
  ];

  constructor(
    private accountService: AccountService,
    private titleService: Title,
    private router: Router,
    private preferencesService: PreferencesService
  ) {}

  ngOnInit(): void {
    // try to log in automatically
    this.accountService.identity().subscribe();

    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.updateTitle();
      }
    });

    this.applyPreferences(); //ALSO SCALES NAVBAR, NEED TO STOP THIS
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
    this.applyPreferences();
  }

  public applyPreferences(): void {
    // Apply or remove high contrast to the CSS of the current page
    this.preferencesService.getHighContrast().subscribe(
      highContrast => {
        if (highContrast) {
          this.applyHighContrast();
        } else {
          this.removeHighContrast();
        }
      },
      error => {
        this.removeHighContrast();
        console.log('Could not find highContrast for appuser: ', error);
      }
    );

    this.preferencesService.getTextSize().subscribe(
      textSize => {
        this.applyTextSize(textSize);
      },
      error => {
        console.error('Error retrieving text size preference:', error);
      }
    );
  }

  private applyHighContrast(): void {
    this.highContrastElements.forEach(selector => {
      const elements = document.querySelectorAll(selector);
      elements.forEach(element => {
        element.classList.add('highContrast');
      });
    });

    this.highContrastElements.forEach(selector => {
      const elements = document.querySelectorAll(selector);
      elements.forEach(element => {
        const cardElement = element.querySelector('.card');
        if (cardElement) {
          (cardElement as HTMLElement).style.background = 'rgb(50, 50, 50)';
        }
        const recapdText = element.querySelector('.description-text');
        if (recapdText) {
          (recapdText as HTMLElement).style.background = 'rgb(50, 50, 50)';
          (recapdText as HTMLElement).style.borderRadius = '7px';
        }
      });
    });
  }

  private removeHighContrast(): void {
    this.highContrastElements.forEach(selector => {
      const element = document.querySelector(selector);
      if (element) {
        element.classList.remove('highContrast');
      }
    });

    this.highContrastElements.forEach(selector => {
      const elements = document.querySelectorAll(selector);
      elements.forEach(element => {
        const cardElement = element.querySelector('.card');
        if (cardElement) {
          (cardElement as HTMLElement).style.background = '';
        }
        const recapdText = element.querySelector('.description-text');
        if (recapdText) {
          (recapdText as HTMLElement).style.background = '';
          (recapdText as HTMLElement).style.borderRadius = '';
        }
      });
    });
  }

  public applyTextSize(textSize: number): void {
    const scalingFactors: { [key: number]: number } = { 0: 1.0, 100: 1.0, 115: 1.05, 125: 1.13 };
    const scalingFactor = scalingFactors[textSize] || 1.0; // Default to 100% scaling if size not found
    const allElements = document.querySelectorAll('*'); // Get all elements in the DOM

    // Filter elements that have a font-size property
    const elementsWithFontSize = Array.from(allElements).filter(element => {
      const computedStyle = window.getComputedStyle(element);
      return computedStyle.getPropertyValue('font-size') !== '';
    });

    elementsWithFontSize.forEach(element => {
      (element as HTMLElement).style.fontSize = ''; // Revert font size to default
    });

    elementsWithFontSize.forEach(element => {
      // Loop through each element and scale its font size
      const computedStyle = window.getComputedStyle(element); // Get the computed style of the element
      const fontSizeString = computedStyle.getPropertyValue('font-size'); // Get the font size as a string
      const currentFontSize = parseFloat(fontSizeString); // Parse the font size string to a number

      if (!isNaN(currentFontSize)) {
        // Check if the font size is a valid number
        // Check if the element or its parent belongs to the navbar
        if (!element.closest('.navbar')) {
          const newFontSize = currentFontSize * scalingFactor; // Calculate the new font size
          (element as HTMLElement).style.fontSize = newFontSize + 'px'; // Apply the new font size to the element
        }
      }
    });
  }
}
