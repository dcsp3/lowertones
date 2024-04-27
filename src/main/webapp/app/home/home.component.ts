import { Component, OnInit, OnDestroy, HostListener, ViewChild, ElementRef } from '@angular/core';
import { Router } from '@angular/router';
import { trigger, state, style, transition, animate } from '@angular/animations';
import { Subject, fromEvent } from 'rxjs';
import { debounceTime, takeUntil } from 'rxjs/operators';
import { LocationService } from '../shared/location.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';

import { clearGraph, getElements, renderGraph } from './networkPreview';

interface Artist {
  distance: number;
  name: string;
  id: string;
  genres: string[];
  imageUrl: string;
}

interface GraphData {
  graphData: Artist[];
}

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  animations: [
    trigger('fillAnimation', [
      state('start', style({ width: '0%' })),
      state('end', style({ width: '{{ fillPercentage }}%' }), { params: { fillPercentage: 0 } }),
      transition('start => end', animate('20s ease-out')),
    ]),
  ],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  particles: number[] = [];
  private readonly destroy$ = new Subject<void>();
  private sectionIds = ['hero', 'tableview-tab', 'recapd-tab', 'network-tab', 'visualisations-tab', 'vault-tab', 'getstarted-tab'];

  constructor(private accountService: AccountService, private router: Router, private locationService: LocationService) {}
  private resizeSubject = new Subject<Event>();

  currentSlide = 3;
  maxSlides = 5;
  autoSlideInterval: any;

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
    const totalParticles = 360 * 2; // Or any number you prefer
    this.particles = Array.from({ length: totalParticles }, (_, i) => i + 1);

    this.startAutoSlide();

    fromEvent(window, 'resize')
      .pipe(debounceTime(500), takeUntil(this.destroy$))
      .subscribe(event => this.handleResize(event));
  }

  private handleResize(event: Event) {
    const currentSection = this.getCurrentSectionInView();
    clearGraph(this.graphContainer.nativeElement);
    this.fetchAndRenderGraph();
    this.hasGraphBeenRendered = true;
  }

  @HostListener('window:scroll', ['$event'])
  onWindowScroll(): void {
    const currentSection = this.getCurrentSectionInView();
    console.log('Current section:', currentSection!);
    this.locationService.setCurrentTab(currentSection!);

    if (currentSection === 'network-tab' && !this.hasGraphBeenRendered) {
      this.fetchAndRenderGraph();
      this.hasGraphBeenRendered = true; // Set the flag to true after rendering
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
    clearInterval(this.autoSlideInterval);
  }

  startAutoSlide() {
    this.autoSlideInterval = setInterval(() => {
      this.currentSlide = (this.currentSlide % this.maxSlides) + 1;
    }, 3000);
  }

  // Network Stuff
  @ViewChild('graphContainer', { static: true }) graphContainer!: ElementRef;

  fillPercentage = 0;
  displayScore: string = '0.00';

  animateScore(finalScore: number): void {
    this.fillPercentage = finalScore;
    let currentScore = 0;
    const increment = finalScore / 100;
    const interval = setInterval(() => {
      currentScore += increment;
      if (currentScore >= finalScore) {
        currentScore = finalScore;
        clearInterval(interval);
      }
      this.displayScore = currentScore.toFixed(2);
    }, 25);
  }

  graphData: GraphData = {
    graphData: [
      {
        distance: 114.28571428571429,
        genres: ['atl hip hop', 'hip hop', 'rap', 'southern hip hop', 'trap'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eb6ef89e23678018c6d8630825',
        name: 'Future',
        id: '1RyvyyTE3xzB2ZywiAwp0i',
      },
      {
        distance: 128.57142857142858,
        genres: ['canadian hip hop', 'canadian pop', 'hip hop', 'pop rap', 'rap'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eb4293385d324db8558179afd9',
        name: 'Drake',
        id: '3TVXtAsR1Inumwj472S9r4',
      },
      {
        distance: 142.85714285714286,
        genres: ['hip hop', 'rap'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5ebdf9a1555f53a20087b8c5a5c',
        name: 'Metro Boomin',
        id: '0iEtIxbK0KxaSlF7G42ZOp',
      },
      {
        distance: 157.14285714285714,
        genres: ['ohio hip hop', 'pop rap'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eb6659c0c409fee150c1f7c879',
        name: 'mgk',
        id: '6TIYQ3jFPwQSRmorSezPxX',
      },
      {
        distance: 171.42857142857144,
        genres: ['pop punk', 'uk pop punk'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eb38597d1ca49138767b0746d1',
        name: 'Neck Deep',
        id: '2TM0qnbJH4QPhGMCdPt7fH',
      },
      {
        distance: 185.71428571428572,
        genres: ['alternative metal'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eb29ec9d388f7d0f9b3480f316',
        name: 'Avenged Sevenfold',
        id: '0nmQIMXWTXfhgOBdNzhGOs',
      },
      {
        distance: 200,
        genres: ['desi hip hop', 'hindi hip hop'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eb2ff3b81c16a59f19b0f6e2d2',
        name: 'Seedhe Maut',
        id: '2oBG74gAocPMFv6Ij9ykdo',
      },
      {
        distance: 214.28571428571428,
        genres: ['hip hop', 'pop rap', 'rap'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eb080d11275f15655a11b2610d',
        name: 'Dreamville',
        id: '1iNqsUDUraNWrj00bqssQG',
      },
      {
        distance: 228.57142857142858,
        genres: ['alternative metal', 'modern rock', 'pop punk', 'punk', 'rock', 'socal pop punk'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eb5da36f8b98dd965336a1507a',
        name: 'blink-182',
        id: '6FBDaR13swtiWwGhX1WQsP',
      },
      {
        distance: 242.85714285714286,
        genres: ['pixel', 'video game music'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eba9b8234e3071836212561d19',
        name: 'C418',
        id: '4uFZsG1vXrPcvnZ4iSQyrx',
      },
      {
        distance: 257.1428571428571,
        genres: ['atl hip hop', 'hip hop', 'rap'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eb4f8f76117470957c0e81e5b2',
        name: '21 Savage',
        id: '1URnnhqYAYcrqrcwql10ft',
      },
      {
        distance: 271.42857142857144,
        genres: ['pop', 'singer-songwriter pop', 'uk pop'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eb3bcef85e105dfc42399ef0ba',
        name: 'Ed Sheeran',
        id: '6eUKZXaKkcviH0Ku9w2n3V',
      },
      {
        distance: 285.7142857142857,
        genres: ['chicago rap', 'hip hop', 'rap'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eb6e835a500e791bf9c27a422a',
        name: 'Kanye West',
        id: '5K4W6rqBFWDnAN6FQUkS6x',
      },
      {
        distance: 300,
        genres: ['social media pop', 'uk hip hop'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5ebcb4ae963f0c01900f3e17712',
        name: 'KSI',
        id: '1nzgtKYFckznkcVMR3Gg4z',
      },
      {
        distance: 314.28571428571433,
        genres: ['hip hop', 'pop rap', 'rap', 'underground hip hop'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eb10781f7b76a5eb0d397e02c9',
        name: 'JID',
        id: '6U3ybJ9UHNKEdsH7ktGBZ7',
      },
      {
        distance: 328.57142857142856,
        genres: ['alternative metal', 'nu metal', 'rap metal', 'rock'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5ebff9f4de8c13f6f563acbfaf1',
        name: 'Slipknot',
        id: '05fG473iIaoy82BF1aGhL8',
      },
      {
        distance: 342.8571428571429,
        genres: ['hip hop', 'pop rap'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eb1cf142a710a2f3d9b7a62da1',
        name: 'NF',
        id: '6fOMl44jA4Sp5b9PpYCkzz',
      },
      {
        distance: 357.14285714285717,
        genres: ['hyper-rock'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eb4688c358a3f82f52b3fb57a5',
        name: 'Quadeca',
        id: '3zz52ViyCBcplK0ftEVPSS',
      },
      {
        distance: 371.42857142857144,
        genres: ['hard rock', 'metal', 'old school thrash', 'rock', 'thrash metal'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eb69ca98dd3083f1082d740e44',
        name: 'Metallica',
        id: '2ye2Wgw4gimLv2eAKyk1NB',
      },
      {
        distance: 385.7142857142857,
        genres: ['chicago rap', 'melodic rap', 'rap'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eb1908e1a8b79abf71d5598944',
        name: 'Juice WRLD',
        id: '4MCBfE4596Uoi2O4DtmEMz',
      },
      {
        distance: 400,
        genres: ['dfw rap', 'melodic rap', 'pop', 'rap'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eb6be070445b03e0b63147c2c1',
        name: 'Post Malone',
        id: '246dkjvS1zLTtiykXe5h60',
      },
      {
        distance: 414.2857142857143,
        genres: ['alternative metal', 'nu metal', 'post-grunge', 'rap metal', 'rock'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eb84a0dd74f21e8acce6a9fd49',
        name: 'Linkin Park',
        id: '6XyY86QOPPrYVGvF9ch6wz',
      },
      {
        distance: 428.5714285714286,
        genres: ['modern rock', 'permanent wave', 'punk', 'rock'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eb6ff0cd5ef2ecf733804984bb',
        name: 'Green Day',
        id: '7oPftvlwr6VrsViSDV7fJY',
      },
      {
        distance: 442.8571428571429,
        genres: ['melodic metalcore', 'metalcore', 'modern rock', 'rock', 'uk metalcore'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eb22569dee4597722952f4325b',
        name: 'Bring Me The Horizon',
        id: '1Ffb6ejR6Fe5IamqA5oRUF',
      },
      {
        distance: 457.14285714285717,
        genres: ['deep underground hip hop', 'kentucky hip hop', 'pop rap', 'rap'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eb2aab40ce03f3fa86163f78bb',
        name: 'Jack Harlow',
        id: '2LIk90788K0zvyj2JJVwkJ',
      },
      {
        distance: 471.42857142857144,
        genres: ['conscious hip hop', 'hip hop', 'rap', 'west coast rap'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eb437b9e2a82505b3d93ff1022',
        name: 'Kendrick Lamar',
        id: '2YZyLoL8N0Wb9xBt1NhZWg',
      },
      {
        distance: 485.7142857142857,
        genres: ['hip hop', 'rap', 'slap house'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eb19c2790744c792d05570bb71',
        name: 'Travis Scott',
        id: '0Y5tJX1MQlPlqiwlOH1tJY',
      },
      {
        distance: 500,
        genres: ['conscious hip hop', 'hip hop', 'north carolina hip hop', 'rap'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eb4b053c29fd4b317ff825f0dc',
        name: 'J. Cole',
        id: '6l3HvQ5sa6mXTsMTB19rO5',
      },
      {
        distance: 514.2857142857143,
        genres: ['alternative metal', 'nu metal', 'post-grunge', 'uk metalcore', 'welsh metal'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eb083627f7d306f40cd7ff8893',
        name: 'Bullet For My Valentine',
        id: '7iWiAD5LLKyiox2grgfmUT',
      },
      {
        distance: 528.5714285714287,
        genres: ['permanent wave', 'pop'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eb989ed05e1f0570cc4726c2d3',
        name: 'Coldplay',
        id: '4gzpq5DPGxSnKTe4SA8HAU',
      },
      {
        distance: 542.8571428571429,
        genres: ['detroit hip hop', 'hip hop', 'rap'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eba00b11c129b27a88fc72f36b',
        name: 'Eminem',
        id: '7dGJo4pcD2V6oG8kP0tJRR',
      },
      {
        distance: 557.1428571428571,
        genres: ['manchester hip hop', 'uk hip hop'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5ebae65f1f660825592448dd360',
        name: 'Aitch',
        id: '2PJEagPIxaBugeMjIyKVXF',
      },
      {
        distance: 571.4285714285714,
        genres: ['east coast hip hop', 'hip hop', 'rap', 'trap'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eb5c58c41a506a0d6b32cc6cad',
        name: 'A$AP Rocky',
        id: '13ubrt8QOOCPljQ2FL1Kca',
      },
      {
        distance: 585.7142857142858,
        genres: ['chicago drill', 'chicago rap', 'drill', 'hip hop', 'rap', 'trap'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eb09791422702c3fa9780468d4',
        name: 'Lil Durk',
        id: '3hcs9uc56yIGFCSy9leWe7',
      },
      {
        distance: 600,
        genres: ['pop rap', 'rap', 'trap'],
        imageUrl: 'https://i.scdn.co/image/ab6761610000e5eb99280563ab81232f397d5dce',
        name: 'Don Toliver',
        id: '4Gso3d4CscCijv0lmajZWs',
      },
    ],
  };

  hasGraphBeenRendered: boolean = false;

  private async fetchAndRenderGraph(): Promise<void> {
    try {
      clearGraph(this.graphContainer.nativeElement);

      const userImageUrl = 'https://i.scdn.co/image/ab67757000003b82155542ca98c6732c5e2ca938';

      const elements = getElements(this.graphData.graphData, userImageUrl);
      this.animateScore(66.97);
      renderGraph(this.graphContainer.nativeElement, elements.nodes, elements.links);
      this.hasGraphBeenRendered = true;
    } catch (error) {
      console.error('Error fetching and rendering graph:', error);
    }
  }
}
