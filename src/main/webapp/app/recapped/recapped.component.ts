import { AfterViewInit, Component, ElementRef, OnInit, Renderer2 } from '@angular/core';
import VanillaTilt from 'vanilla-tilt';

@Component({
  selector: 'jhi-recapped',
  templateUrl: './recapped.component.html',
  styleUrls: ['./recapped.component.scss'],
})
export class RecappedComponent implements OnInit, AfterViewInit {
  currentScreen: 'title' | 'results' = 'title';
  selectedMusicianType: string = 'Producers';
  musicianTypes: string[] = ['Producers', 'Singers', 'Guitarists', 'Bassists', 'Drummers'];
  selectedTimeRange: string = 'lastMonth';
  topMusicians: any[] = [];
  timeframeLabel: string = 'Month';

  constructor(private elementRef: ElementRef, private renderer: Renderer2) {}

  ngOnInit(): void {
    this.loadTopMusicians();
  }

  ngAfterViewInit(): void {
    this.initVanillaTiltTitleScreen();
  }

  private initVanillaTiltTitleScreen(): void {
    const titleImgElement = this.elementRef.nativeElement.querySelector('.title-screen .vanilla-tilt-img');
    this.initVanillaTilt(titleImgElement);
  }

  private initVanillaTiltOnResultsScreen(): void {
    const imgElements = this.elementRef.nativeElement.querySelectorAll('.results-container .vanilla-tilt-img');

    imgElements.forEach((imgElement: HTMLElement) => {
      this.renderer.listen(imgElement, 'load', () => {
        this.initVanillaTilt(imgElement);
      });
    });
  }

  private initVanillaTilt(element: HTMLElement): void {
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

  goToSelectionScreen(): void {
    this.currentScreen = 'title';
  }

  goToResultsScreen(): void {
    this.currentScreen = 'results';
    this.initVanillaTiltOnResultsScreen();
  }

  loadTopMusicians(): void {
    this.updateTimeframeLabel();
    // Implement logic to fetch top musicians based on selectedMusicianType and time range
    // Update this.topMusicians with the fetched data
  }

  selectTimeRange(timeRange: string): void {
    this.selectedTimeRange = timeRange;
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
