import { Component, OnInit, AfterViewInit, ElementRef, Renderer2, Injectable } from '@angular/core';
import VanillaTilt from 'vanilla-tilt';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FormBuilder, FormGroup } from '@angular/forms';

interface RecappedDTO {
  numOneArtistName: string;
  numTwoArtistName: string;
  numThreeArtistName: string;
  numFourArtistName: string;
  numFiveArtistName: string;
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
})
export class RecappedComponent implements OnInit, AfterViewInit {
  currentScreen: 'title' | 'results' = 'title';
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

  ngOnInit(): void {}

  ngAfterViewInit(): void {
    this.initVanillaTiltTitleScreen();
  }

  onSubmit() {
    console.log('submitting form');
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

      this.recappedService.submitRecappedRequest(request).subscribe({
        next: response => {
          console.log('response from backend' + response.numOneArtistName);
        },
        error: error => {
          console.error('Error fetching recapped info:', error);
        },
      });
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

  goToResultsScreen(): void {
    this.onSubmit();

    console.log(this.recappedForm.get('musicianType')?.value);
    console.log(this.recappedForm.get('scanType')?.value);
    console.log(this.recappedForm.get('timeframe')?.value);
    const isSelectionComplete =
      this.recappedForm.get('scanType')?.value && this.recappedForm.get('timeframe')?.value && this.recappedForm.get('musicianType')?.value;

    if (!isSelectionComplete) {
      this.highlightEmptySelections();
      return;
    }
    this.currentScreen = 'results';
    this.initVanillaTiltOnResultsScreen();
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
}
