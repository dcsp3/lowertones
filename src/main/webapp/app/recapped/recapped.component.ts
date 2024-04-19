import { Component, OnInit, AfterViewInit, ElementRef, Renderer2, Injectable, HostListener } from '@angular/core';
import VanillaTilt from 'vanilla-tilt';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FormBuilder, FormGroup } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { trigger, state, style, transition, animate } from '@angular/animations';
import { RecappedService } from './recapped.service';
import { suckInAnimation, fadeInOut } from './animations';
import { RecappedRequest, RecappedDTO, MusicianType, Timeframe, choice } from './models';
import { formatDate } from '@angular/common';

interface Playlist {
  name: string;
  spotifyId: string;
  snapshotId: string;
}

interface QuizCard {
  name: string;
  image: string;
}

@Injectable({
  providedIn: 'root',
})
class PlaylistService {
  private apiUrl = '/api/get-user-playlists';
  constructor(private http: HttpClient) {}
  getPlaylists(): Observable<any> {
    return this.http.get(this.apiUrl);
  }
}

@Component({
  selector: 'jhi-recapped',
  templateUrl: './recapped.component.html',
  styleUrls: ['./recapped.selections.scss', './recapped.results.scss'],
  animations: [suckInAnimation, fadeInOut],
})
export class RecappedComponent implements OnInit, AfterViewInit {
  //different screens
  currentScreen: 'title' | 'error' | 'results' = 'title';

  animationState: 'default' | 'sucked' = 'default';
  isLoading: boolean = false;
  recappedForm: FormGroup;

  selectedTimeframe: string = '';
  selectedMusician: string = '';
  selectedScanType: string = '';
  musicianType: choice[];
  timeframes: choice[];
  rangeDates!: Date[];
  scanType: choice[];
  highlightScanType: boolean = false;
  highlightTimeframe: boolean = false;
  highlightMusician: boolean = false;

  response: any;
  animatedNumSongs: number = 0;

  maxDate = new Date(Date.now());
  currentPage = 0;
  totalPages = 6;
  scrolling = false;
  scrollUpAccumulator = 0;
  scrollDownAccumulator = 0;

  quizCardLeft: QuizCard = { name: '', image: '' };
  quizCardCenter: QuizCard = { name: '', image: '' };
  quizCardRight: QuizCard = { name: '', image: '' };

  constructor(
    private elementRef: ElementRef,
    private renderer: Renderer2,
    private fb: FormBuilder,
    private recappedService: RecappedService,
    private playlistService: PlaylistService
  ) {
    this.timeframes = [
      { label: 'Month', value: 'LAST_MONTH' },
      { label: 'Last 6 Months', value: 'LAST_6_MONTHS' },
      { label: 'Last Few Years', value: 'LAST_FEW_YEARS' },
    ];
    this.musicianType = [
      { label: 'Producers', value: 'producer' },
      { label: 'Mixing Engineers', value: 'mixing_engineer' },
      { label: 'Singers', value: 'singer' },
      { label: 'Rappers', value: 'rapper' },
      { label: 'Guitarists', value: 'guitarist' },
      { label: 'Bassists', value: 'bassist' },
      { label: 'Drummers', value: 'drummer' },
      { label: 'Pianists', value: 'pianist' },
      { label: 'Violinists', value: 'violinist' },
      { label: 'Trumpeters', value: 'trumpeter' },
      { label: 'Saxophonists', value: 'saxophonist' },
      { label: 'DJs', value: 'dj' },
      { label: 'Songwriters', value: 'songwriter' },
      { label: 'Composers', value: 'composer' },
    ];
    this.scanType = [
      { label: 'My Entire Library', value: 'entireLibrary' },
      { label: 'My Top Songs', value: 'topSongs' },
    ];

    this.recappedForm = this.fb.group({
      scanType: [''],
      timeframe: [''],
      musicianType: [''],
    });
  }

  ngOnInit(): void {
    /*
        // REMOVE THIS WHEN YOU ARE DONE TESTING
        this.recappedForm.setValue({
            scanType: 'entireLibrary', // Replace with your default value
            timeframe: 'LAST_MONTH', // Replace with your default value
            musicianType: 'PRODUCER', // Replace with your default value
            playlistId: '', // Replace with your default value or leave empty if not needed
        });
        this.goToResultsScreen(this.recappedForm.value);
        ///^^^^^^^^^^^^^^ REMOVE THIS WHEN YOU ARE DONE TESTING
        */

    this.fetchPlaylists();
  }

  ngAfterViewInit(): void {
    this.initVanillaTiltTitleScreen();
  }
  ngOnDestroy(): void {
    this.stopFlavorTextRotation();
  }

  onSubmit() {
    if (this.recappedForm.valid) {
      console.log('Form submitted:', this.recappedForm.value);
      const formValue = this.recappedForm.value;
      let timeframeValue = formValue.timeframe;
      if (typeof timeframeValue === 'object') {
        timeframeValue = timeframeValue.value;
      } else if (this.rangeDates && this.rangeDates.length === 2) {
        const startDate = formatDate(this.rangeDates[0], 'yyyy-MM-dd', 'en-US');
        const endDate = formatDate(this.rangeDates[1], 'yyyy-MM-dd', 'en-US');
        timeframeValue = `${startDate} - ${endDate}`;
      }
      const request: RecappedRequest = {
        timeframe: timeframeValue,
        musicianType: formValue.musicianType.toUpperCase() as MusicianType,
        scanType: formValue.scanType,
      };
      this.goToResultsScreen(request);
    }
  }

  fetchPlaylists() {
    this.playlistService.getPlaylists().subscribe({
      next: (response: Playlist[]) => {
        const playlistOptions = response.map((playlist: Playlist) => ({
          label: playlist.name,
          value: playlist.spotifyId,
        }));
        this.scanType = [...this.scanType, ...playlistOptions];
      },
      error: error => {
        console.error('Error fetching playlists:', error);
      },
    });
  }

  setScanTypeValue(value: any): void {
    this.selectedScanType = value.value;
    const scantypeControl = this.recappedForm.get('scanType');
    if (scantypeControl) {
      scantypeControl.setValue(this.selectedScanType);
      this.highlightScanType = false;
      this.rangeDates = [];
      this.selectedTimeframe = '';
      this.recappedForm.get('timeframe')?.setValue('');
    }
  }

  setTimeframeValue(value: any): void {
    const timeframeControl = this.recappedForm.get('timeframe');
    if (timeframeControl) {
      timeframeControl.setValue(value);
      this.highlightTimeframe = false;
    }
  }

  onDateRangeChange(dates: Date[]): void {
    if (dates && dates.length === 2) {
      const startDate = formatDate(dates[0], 'yyyy-MM-dd', 'en-US');
      const endDate = formatDate(dates[1], 'yyyy-MM-dd', 'en-US');
      const formattedRange = `${startDate} - ${endDate}`;
      this.recappedForm.get('timeframe')!.setValue(formattedRange);
    } else {
      this.recappedForm.get('timeframe')!.setValue('');
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
        this.navigateToPage(0);
        this.initVanillaTiltOnResultsScreen();
        this.quizRandomizer();
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

  @HostListener('window:mousewheel', ['$event'])
  onWindowScroll(event: WheelEvent) {
    if (!this.scrolling) {
      if (event.deltaY < 0) {
        this.scrollUpAccumulator++;
        this.scrollDownAccumulator = 0;
      } else if (event.deltaY > 0) {
        this.scrollDownAccumulator++;
        this.scrollUpAccumulator = 0;
      }
      if (this.scrollUpAccumulator > 3) {
        this.navigateUp();
        this.scrollUpAccumulator = 0;
      }
      if (this.scrollDownAccumulator > 3) {
        this.navigateDown();
        this.scrollDownAccumulator = 0;
      }
    }
  }
  getActivePageClass() {
    return { ['active-page' + this.currentPage]: true };
  }

  navigateDown() {
    if (this.currentPage < this.totalPages - 1) {
      this.animatePageTransition(() => {
        this.currentPage++;
        if (this.currentPage === 0) {
          this.animateNumber(this.response?.totalSongs);
        }
        if (this.currentPage === 2) {
          this.animateNumber(this.response?.numOneAristNumSongs);
        }
      });
    }
  }

  navigateUp() {
    if (this.currentPage > 0) {
      this.animatePageTransition(() => {
        this.currentPage--;
        if (this.currentPage === 0) {
          this.animateNumber(this.response?.totalSongs);
        }
        if (this.currentPage === 2) {
          this.animateNumber(this.response?.numOneAristNumSongs);
        }
      });
    }
  }

  animatePageTransition(callback: () => void) {
    this.scrolling = true;
    setTimeout(() => {
      callback();
      this.scrolling = false;
    }, 300);
  }

  navigateToPage(pageNumber: number): void {
    if (!this.scrolling && pageNumber >= 0 && pageNumber < this.totalPages) {
      this.currentPage = pageNumber;
      if (this.currentPage === 0) {
        this.animateNumber(this.response?.totalSongs);
      }
      if (this.currentPage === 2) {
        this.animateNumber(this.response?.numOneAristNumSongs);
      }
    }
  }

  getTimeframeLabel(): string {
    if (typeof this.recappedForm.value.timeframe === 'object') {
      return this.recappedForm.value.timeframe.label.toLowerCase();
    } else if (this.rangeDates && this.rangeDates.length === 2) {
      const startDate = this.rangeDates[0];
      const endDate = this.rangeDates[1];
      const diffTime = endDate.getTime() - startDate.getTime();
      const diffDays = diffTime / (1000 * 60 * 60 * 24);
      if (diffDays < 30) {
        return diffDays + ' days';
      } else if (diffDays < 365) {
        return (diffDays / 30).toFixed(1) + ' months';
      } else {
        return (diffDays / 365).toFixed(1) + ' years';
      }
    } else {
      return this.recappedForm.get('timeframe')?.value;
    }
  }

  getMusicianTypeLabel(): string {
    const selectedMusicianTypeValue = this.recappedForm.get('musicianType')?.value;
    return selectedMusicianTypeValue;
  }

  getTotalLibraryDuration(): string {
    const totalDuration = this.response?.totalDuration;
    if (totalDuration > 60) {
      return `${(totalDuration / 60).toFixed(1)} hours`;
    } else if (totalDuration > 1440) {
      return `${(totalDuration / 1440).toFixed(3)} days`;
    } else if (totalDuration > 10080) {
      return `${(totalDuration / 10080).toFixed(3)} weeks`;
    } else {
      return `${(totalDuration / 43800).toFixed(3)} months`;
    }
  }

  getPercentageOfSpotifyLibrary(): string {
    const totalSongs = this.response?.totalSongs;
    const totalArtists = this.response?.totalArtists;
    const totalSpotifySongs = 1000000000;
    const totalSpotifyArtists = 11000000;
    const songPercentage = (totalSongs / totalSpotifySongs) * 100;
    const artistPercentage = (totalArtists / totalSpotifyArtists) * 100;
    return `${songPercentage.toFixed(7)}% of songs and ${artistPercentage.toFixed(7)}% of artists`;
  }

  randomNumbers: (number | undefined)[] = [undefined, undefined, undefined];

  quizRandomizer() {
    let indices = [0, 1, 2];
    let randomIndex = indices.splice(Math.floor(Math.random() * 3), 1)[0];
    this.randomNumbers[randomIndex] = 1;
    while (this.randomNumbers.includes(undefined)) {
      let randomNum = Math.floor(Math.random() * 5) + 1;
      if (!this.randomNumbers.includes(randomNum) || this.randomNumbers.filter(x => x === randomNum).length < (randomNum === 1 ? 1 : 0)) {
        let firstUndefinedIndex = this.randomNumbers.indexOf(undefined);
        if (firstUndefinedIndex !== -1) {
          this.randomNumbers[firstUndefinedIndex] = randomNum;
        }
      }
    }
    this.quizCardLeft = this.getQuizCard(this.randomNumbers[0]!);
    this.quizCardCenter = this.getQuizCard(this.randomNumbers[1]!);
    this.quizCardRight = this.getQuizCard(this.randomNumbers[2]!);
  }

  getQuizCard(randomNumber: number): QuizCard {
    switch (randomNumber) {
      case 1:
        return { name: this.response?.numOneArtistName || '', image: this.response?.numOneArtistImage || '' };
      case 2:
        return { name: this.response?.numTwoArtistName || '', image: this.response?.numTwoArtistImage || '' };
      case 3:
        return { name: this.response?.numThreeArtistName || '', image: this.response?.numThreeArtistImage || '' };
      case 4:
        return { name: this.response?.numFourArtistName || '', image: this.response?.numFourArtistImage || '' };
      case 5:
        return { name: this.response?.numFiveArtistName || '', image: this.response?.numFiveArtistImage || '' };
      default:
        return { name: '', image: '' };
    }
  }

  selectedCard: 'left' | 'center' | 'right' | null = null;
  isSelectionCorrect: boolean | null = null;

  selectCard(position: 'left' | 'center' | 'right') {
    const selectedIndex = position === 'left' ? 0 : position === 'center' ? 1 : 2;
    this.isSelectionCorrect = this.randomNumbers[selectedIndex] === 1;
    this.selectedCard = position;

    if (!this.isSelectionCorrect) {
      setTimeout(() => {
        const correctIndex = this.randomNumbers.indexOf(1);
        const correctPosition = correctIndex === 0 ? 'left' : correctIndex === 1 ? 'center' : 'right';
        this.selectedCard = correctPosition;
        this.isSelectionCorrect = true;
      }, 1000);
    }
  }

  cardClass(position: 'left' | 'center' | 'right'): string {
    if (this.selectedCard === position) {
      return this.isSelectionCorrect ? 'correct' : 'incorrect';
    }
    return '';
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
    }, 200);
  }

  easeOutExpo(t: number, b: number, c: number, d: number) {
    return t === d ? b + c : c * (-Math.pow(2, (-10 * t) / d) + 1) + b;
  }
}
