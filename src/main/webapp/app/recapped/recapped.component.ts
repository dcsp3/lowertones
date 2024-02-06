import { AfterViewInit, Component, ElementRef, OnInit, Renderer2 } from '@angular/core';
import VanillaTilt from 'vanilla-tilt';

@Component({
  selector: 'jhi-recapped',
  templateUrl: './recapped.component.html',
  styleUrls: ['./recapped.component.scss'],
})
export class RecappedComponent implements OnInit, AfterViewInit {
  currentScreen: 'title' | 'selection' | 'results' = 'title'; // Declare currentScreen property
  selectedMusicianType: string = 'Producers'; // Default to prdoucers
  musicianTypes: string[] = ['Producers', 'Singers', 'Guitarists', 'Bassists', 'Drummers'];
  selectedTimeRange: string = 'lastMonth'; // Set the default time range to last Month
  topMusicians: any[] = [];
  timeframeLabel: string = 'Month'; // Default label

  constructor(private elementRef: ElementRef, private renderer: Renderer2) {}

  ngOnInit(): void {
    this.loadTopMusicians();
  }

  ngAfterViewInit(): void {
    // Initialize VanillaTilt on title screen
    const titleImgElement = this.elementRef.nativeElement.querySelector('.title-screen .vanilla-tilt-img');
    this.initVanillaTilt(titleImgElement);
  }

  // Add this method to initialize VanillaTilt on an element
  initVanillaTilt(element: HTMLElement): void {
    VanillaTilt.init(element, {
      max: 32,
      speed: 2000,
      glare: true,
      'max-glare': 0,
      easing: 'cubic-bezier(.03,.98,.52,.99)',
      perspective: 900,
      transition: true,
    });
  }

  // Add this method to initialize VanillaTilt on result screen images
  initVanillaTiltOnResultsScreen(): void {
    const imgElements = this.elementRef.nativeElement.querySelectorAll('.results-container .vanilla-tilt-img');

    // Use Renderer2 to listen for changes and then initialize VanillaTilt
    imgElements.forEach((imgElement: HTMLElement) => {
      this.renderer.listen(imgElement, 'load', () => {
        this.initVanillaTilt(imgElement);
      });
    });
  }

  goToSelectionScreen(): void {
    this.currentScreen = 'selection';
  }
  // Update the method to navigate between screens
  goToResultsScreen(): void {
    this.currentScreen = 'results';
    this.initVanillaTiltOnResultsScreen(); // Initialize VanillaTilt on results screen
  }

  loadTopMusicians(): void {
    // Implement the logic to fetch top musicians based on selectedMusicianType and time range
    // may want to call service to fetch data from the backend
    // Update this.topMusicians with the fetched data
    // For example, can make an HTTP request to Spring Boot backend
    // using Angular HttpClient to get data from API endpoint.
    // Update timeframeLabel based on the selectedTimeRange
    this.updateTimeframeLabel();
  }

  selectTimeRange(timeRange: string): void {
    // Set the selected time range
    this.selectedTimeRange = timeRange;

    // Implement logic to set the start and end date based on the selected time range
    // Update this.startDate and this.endDate accordingly
    // Then call loadTopMusicians to fetch data for the updated time range
    this.loadTopMusicians();
  }

  updateTimeframeLabel(): void {
    switch (this.selectedTimeRange) {
      case 'lastMonth':
        this.timeframeLabel = 'Month';
        break;
      case 'last6Months':
        this.timeframeLabel = '6 Months';
        break;
      case 'lastFewYears':
        this.timeframeLabel = 'Few Years';
        break;
      default:
        this.timeframeLabel = 'Month';
        break;
    }
  }
}
