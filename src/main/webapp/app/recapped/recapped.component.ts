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
  startDate: string; // ISO date format 'YYYY-MM-DD'
  endDate: string; // ISO date format 'YYYY-MM-DD'
  dateRange: DateRange;
  musicianType: MusicianType;
  scanEntireLibrary: boolean;
  scanTopSongs: boolean;
  scanSpecificPlaylist: boolean;
  playlistId?: string; // Optional, based on scanSpecificPlaylist
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
  selectedMusicianType: string = 'Producers';
  musicianTypes: string[] = ['Producers', 'Singers', 'Guitarists', 'Bassists', 'Drummers'];
  selectedTimeRange: string = 'lastMonth';
  topMusicians: any[] = [];
  timeframeLabel: string = 'Month';
  recappedForm: FormGroup;

  constructor(
    private elementRef: ElementRef,
    private renderer: Renderer2,
    private fb: FormBuilder,
    private recappedService: RecappedService
  ) {
    this.recappedForm = this.fb.group({
      startDate: [''],
      endDate: [''],
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
    if (this.recappedForm.valid) {
      const request: RecappedRequest = this.recappedForm.value;
      this.recappedService.submitRecappedRequest(request).subscribe({
        next: response => {
          console.log(response);
          // Handle the response, display the results
        },
        error: error => {
          console.error('Error fetching recapped info:', error);
        },
      });
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
    this.currentScreen = 'results';
    this.initVanillaTiltOnResultsScreen();
  }

  selectTimeRange(timeRange: string): void {
    this.selectedTimeRange = timeRange;
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
