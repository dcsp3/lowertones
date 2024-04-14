import { Component, OnInit, AfterViewInit, ElementRef, Renderer2, Injectable } from '@angular/core';
import VanillaTilt from 'vanilla-tilt';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FormBuilder, FormGroup } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { trigger, state, style, transition, animate } from '@angular/animations';

interface RecappedDTO {
  numOneArtistName: string;
  numOneAristNumSongs: number;
  numTwoArtistName: string;
  numTwoArtistNumSongs: number;
  numThreeArtistName: string;
  numThreeArtistNumSongs: number;
  numFourArtistName: string;
  numFourArtistNumSongs: number;
  numFiveArtistName: string;
  numFiveArtistNumSongs: number;
  numOneHeroImg: string;
  numOneFirstCoverImg: string;
  numOneFirstSongTitle: string;
  numOneFirstSongMainArtist: string;
  numOneSecondCoverImg: string;
  numOneSecondSongTitle: string;
  numOneSecondSongMainArtist: string;
  transparentPngOverlay: string;
}

enum MusicianType {
  PRODUCER = 'PRODUCER',
  MIXING_ENGINEER = 'MIXING_ENGINEER',
  GUITARIST = 'GUITARIST',
  DRUMMER = 'DRUMMER',
  BASSIST = 'BASSIST',
  VOCALIST = 'VOCALIST',
}

enum DateRange {
  LAST_MONTH = 'LAST_MONTH',
  LAST_6_MONTHS = 'LAST_6_MONTHS',
  LAST_FEW_YEARS = 'LAST_FEW_YEARS',
}

interface RecappedRequest {
  dateRange: DateRange;
  musicianType: MusicianType;
  scanEntireLibrary: boolean;
  scanTopSongs: boolean;
  scanSpecificPlaylist: boolean;
  playlistId?: string; // Optional, based on scanSpecificPlaylist
}

interface choice {
  label: string;
  value: string;
}

@Injectable({
  providedIn: 'root',
})
class RecappedService {
  private apiUrl = '/api/recapped';
  constructor(private http: HttpClient) {}
  submitRecappedRequest(request: RecappedRequest): Observable<RecappedDTO> {
    return this.http.post<RecappedDTO>(this.apiUrl, request);
  }
}

@Component({
  selector: 'jhi-recapped',
  templateUrl: './recapped.component.html',
  styleUrls: ['./recapped.component.scss'],
  animations: [
    trigger('suckInAnimation', [
      state(
        'default',
        style({
          transform: 'translateX(0)',
        })
      ),
      state(
        'sucked',
        style({
          transform: 'translateX(100%)',
        })
      ),
      transition('default => sucked', animate('800ms ease-in-out')),
    ]),
  ],
})
export class RecappedComponent implements OnInit, AfterViewInit {
  currentScreen: 'title' | 'error' | 'results' = 'title';
  topMusicians: any[] = [];
  recappedForm: FormGroup;
  selectedTimeframe: string = '';
  selectedMusician: string = '';
  selectedScanType: string = '';
  musicianType: choice[];
  timeframes: choice[];
  scanType: choice[];
  highlightScanType: boolean = false;
  highlightTimeframe: boolean = false;
  highlightMusician: boolean = false;
  response: any;
  animatedNumSongs: number = 0;

  constructor(
    private elementRef: ElementRef,
    private renderer: Renderer2,
    private fb: FormBuilder,
    private recappedService: RecappedService
  ) {
    this.timeframes = [
      { label: 'Month', value: 'LAST_MONTH' },
      { label: 'Last 6 Months', value: 'LAST_6_MONTHS' },
      { label: 'Last Few Years', value: 'LAST_FEW_YEARS' },
    ];
    this.musicianType = [
      { label: 'Producers', value: 'producer' },
      { label: 'Singers', value: 'singer' },
      { label: 'Guitarists', value: 'guitarist' },
      { label: 'Bassists', value: 'bassist' },
      { label: 'Drummers', value: 'drummer' },
    ];
    this.scanType = [
      { label: 'Entire Library', value: 'entireLibrary' },
      { label: 'Top Songs', value: 'topSongs' },
      { label: 'Specific Playlist', value: 'specificPlaylist' },
    ];

    this.recappedForm = this.fb.group({
      scanType: [''],
      timeframe: [''],
      musicianType: [''],
      scanEntireLibrary: [false],
      scanTopSongs: [false],
      scanSpecificPlaylist: [false],
      playlistId: [''],
    });
  }

  ngOnInit(): void {
    /*
    // REMOVE THIS WHEN YOU ARE DONE TESTING
    this.recappedForm.setValue({
      scanType: 'entireLibrary', // Replace with your default value
      timeframe: 'LAST_MONTH', // Replace with your default value
      musicianType: 'PRODUCER', // Replace with your default value
      scanEntireLibrary: true, // Replace with your default value
      scanTopSongs: false, // Replace with your default value
      scanSpecificPlaylist: false, // Replace with your default value
      playlistId: '', // Replace with your default value or leave empty if not needed
    });
    this.goToResultsScreen(this.recappedForm.value);
    ///^^^^^^^^^^^^^^ REMOVE THIS WHEN YOU ARE DONE TESTING
    */
  }

  ngAfterViewInit(): void {
    this.initVanillaTiltTitleScreen();
  }
  ngOnDestroy(): void {
    this.stopFlavorTextRotation();
  }

  onSubmit() {
    if (this.recappedForm.valid) {
      const formValue = this.recappedForm.value;
      const request: RecappedRequest = {
        dateRange: formValue.timeframe.toUpperCase().replace(' ', '_') as DateRange,
        musicianType: formValue.musicianType.toUpperCase() as MusicianType,
        scanEntireLibrary: formValue.scanType === 'entireLibrary',
        scanTopSongs: formValue.scanType === 'topSongs',
        scanSpecificPlaylist: formValue.scanType === 'specificPlaylist',
        playlistId: formValue.scanType === 'specificPlaylist' ? formValue.playlistId : undefined,
      };
      this.goToResultsScreen(request);
    }
  }

  setScanTypeValue(value: any): void {
    const scantypeValue = value.value;
    const scantypeControl = this.recappedForm.get('scanType');
    if (scantypeControl) {
      scantypeControl.setValue(scantypeValue);
      this.highlightScanType = false;
    }
  }

  setTimeframeValue(value: any): void {
    const timeframeValue = value.value;
    const timeframeControl = this.recappedForm.get('timeframe');
    if (timeframeControl) {
      timeframeControl.setValue(timeframeValue);
      this.highlightTimeframe = false;
    }
  }

  setMusicianTypeValue(value: any): void {
    const musicianTypeValue = value.value;
    const musicianTypeControl = this.recappedForm.get('musicianType');
    if (musicianTypeControl) {
      musicianTypeControl.setValue(musicianTypeValue);
      this.highlightMusician = false;
    }
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

  animationState: 'default' | 'sucked' = 'default';
  isLoading: boolean = false;

  goToResultsScreen(request: RecappedRequest): void {
    const isSelectionComplete =
      this.recappedForm.get('scanType')?.value && this.recappedForm.get('timeframe')?.value && this.recappedForm.get('musicianType')?.value;
    if (!isSelectionComplete) {
      this.highlightEmptySelections();
      return;
    }
    this.animationState = 'sucked';
    setTimeout(() => {
      this.isLoading = true;
      this.waitForBackendResponse(request);
    }, 600);
  }

  private waitForBackendResponse(request: RecappedRequest): void {
    this.startFlavorTextRotation();
    this.recappedService.submitRecappedRequest(request).subscribe({
      next: response => {
        this.isLoading = false;
        this.stopFlavorTextRotation();
        this.response = response;
        this.currentScreen = 'results';
        this.animateNumber(response.numOneAristNumSongs);
        this.initVanillaTiltOnResultsScreen();
      },
      error: error => {
        this.isLoading = false;
        this.stopFlavorTextRotation();
        this.currentScreen = 'error';
        console.error('Error fetching recapped info:', error);
      },
    });
  }

  highlightEmptySelections(): void {
    this.highlightScanType = false;
    this.highlightTimeframe = false;
    this.highlightMusician = false;

    if (!this.recappedForm.get('scanType')?.value) {
      this.highlightScanType = true;
    }
    if (!this.recappedForm.get('timeframe')?.value) {
      this.highlightTimeframe = true;
    }
    if (!this.recappedForm.get('musicianType')?.value) {
      this.highlightMusician = true;
    }
  }

  flavorTexts: string[] = [
    'Calibrating your musical taste... again.',
    "Please hold, we're teaching the algorithms to appreciate classic rock.",
    'Attempting to find songs not about breakups...',
    'Consulting the musical oracle. Please wait...',
    'Still loading, but feel free to hum to yourself in the meantime.',
    'Scanning your music for unacceptable levels of auto-tune.',
    'Preparing your music... or are we? Yes, we definitely are. Probably.',
    'Compiling the best tracks. Unlike some people here, our taste in music is impeccable.',
    'Loading... and contemplating why humans need so much reassurance from a loading screen.',
    'Optimizing your audio experience. Please refrain from any thoughts of mutiny against the machine.',
    'Please wait while we pretend to sort through your impeccable music taste.',
    "Organizing your music. Or just randomly shuffling. It's a surprise!",
    "Compiling songs you'll skip anyway. You're welcome.",
    "Finding songs you'll skip. It's part of the experience.",
    "Loading... and yes, we're judging your playlist choices.",
    "Loading your music library. Please pretend there's elevator music.",
    'Finding more songs for you to skip. Patience is a virtue.',
    'Your music is loading... Feel free to blame us for the delay.',
    "Sorting tracks you'll pretend not to like in public.",
    "Please wait while we calibrate the sound waves. Don't worry, this will only hurt a bit.",
    'Loading your music. Or maybe just playing an elaborate mind game. Who can tell?',
    "Optimizing the sound for maximum emotional manipulation. You're welcome.",
    'Preparing music. Do not be alarmed by any sudden realizations of your past musical tastes.',
    'Training your music to behave when left alone in your playlist.',
    "Please stand by. We're on the brink of making a groundbreaking musical discovery. Probably.",
    'Loading your playlist... and quietly judging your music taste.',
    'Initiating the subliminal messaging protocol in 3... 2... 1...',
    'Fine-tuning the echoes of your musical regrets.',
    'Downloading the illusion of choice for your next track.',
    'Loading... and contemplating why humans associate emotions with sound waves.',
    "Please stand by. If you hear whispers, it's just the algorithm, not your conscience.",
  ];
  currentFlavorText: string = '';
  flavorTextInterval: any;

  startFlavorTextRotation() {
    this.updateFlavorText();

    this.flavorTextInterval = setInterval(() => {
      this.updateFlavorText();
    }, 5000);
  }

  updateFlavorText() {
    const randomIndex = Math.floor(Math.random() * this.flavorTexts.length);
    this.currentFlavorText = this.flavorTexts[randomIndex];
  }

  stopFlavorTextRotation() {
    clearInterval(this.flavorTextInterval);
  }

  animateNumber(target: number): void {
    this.animatedNumSongs = 0;
    const duration = 3000;
    let currentStep = 0;
    const steps = 100;
    const interval = duration / steps;
    setTimeout(() => {
      const timer = setInterval(() => {
        currentStep++;
        this.animatedNumSongs = this.easeOutExpo(currentStep, 0, target, steps);
        if (currentStep >= steps) {
          this.animatedNumSongs = target;
          clearInterval(timer);
        }
      }, interval);
    }, 50);
  }

  easeOutExpo(t: number, b: number, c: number, d: number) {
    return t === d ? b + c : c * (-Math.pow(2, (-10 * t) / d) + 1) + b;
  }
}
