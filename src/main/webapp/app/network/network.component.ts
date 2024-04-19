import { Component, OnInit, ElementRef, ViewChild, HostListener } from '@angular/core';
import { trigger, state, style, transition, animate } from '@angular/animations';
import { Subject } from 'rxjs';
import { debounceTime } from 'rxjs/operators';

import { clearGraph, getElements, renderGraph } from './topArtistsGraph';

interface Artist {
  distance: number;
  name: string;
  id: string;
  genres: string[];
  imageUrl: string;
}

interface TasteCategoryDetails {
  name: string;
  colorDark: string;
  colorLight: string;
}

@Component({
  selector: 'jhi-network',
  templateUrl: './network.component.html',
  styleUrls: ['./network.component.scss'],
  animations: [
    trigger('fillAnimation', [
      state('start', style({ width: '0%' })),
      state('end', style({ width: '{{ fillPercentage }}%' }), { params: { fillPercentage: 0 } }),
      transition('start => end', animate('20s ease-out')),
    ]),
  ],
})
export class NetworkComponent implements OnInit {
  @ViewChild('graphContainer', { static: true }) graphContainer!: ElementRef;
  @ViewChild('connectionsCheckbox') connectionsCheckbox!: ElementRef<HTMLInputElement>;

  timeRange: string = 'short-term';
  topArtistImage: string = '';
  topArtistName: string = '';

  topTrackName: string | null = null;
  topTrackPreviewUrl: string | null = null;

  isPlaying: boolean = false;
  currentAudio: HTMLAudioElement | null = null;

  topGenre: string = '';
  averagePopularity: string = '';
  tasteCategoryDetails: TasteCategoryDetails | null = null;

  fillPercentage = 0;
  displayScore: string = '0.00';

  private resizeSubject = new Subject<Event>();
  private timeRangeSubject = new Subject<string>();

  ngOnInit(): void {
    this.fetchAndRenderGraph(this.timeRange);

    this.resizeSubject.pipe(debounceTime(500)).subscribe(event => {
      this.onResize(event);
    });

    this.timeRangeSubject.pipe(debounceTime(100)).subscribe(newTimeRange => {
      this.handleTimeRangeChange(newTimeRange);
    });
  }

  @HostListener('window:resize', ['$event'])
  onWindowResize(event: Event) {
    clearGraph(this.graphContainer.nativeElement);
    this.resizeSubject.next(event);
  }

  private onResize(event: Event) {
    clearGraph(this.graphContainer.nativeElement);
    this.fetchAndRenderGraph(this.timeRange);
  }

  private handleTimeRangeChange(newTimeRange: string): void {
    if (newTimeRange !== this.timeRange) {
      this.timeRange = newTimeRange;

      if (this.connectionsCheckbox.nativeElement.checked) {
        this.connectionsCheckbox.nativeElement.checked = false;
        this.toggleConnections();
      }

      if (this.currentAudio && !this.currentAudio.paused) {
        this.currentAudio.pause();
        this.currentAudio.currentTime = 0;
      }
      this.isPlaying = false;
      this.currentAudio = null;

      this.fetchAndRenderGraph(this.timeRange);
    }
  }

  changeTimeRange(newTimeRange: string): void {
    this.timeRangeSubject.next(newTimeRange);
  }

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

  fetchTopArtists(timeRange: string): Promise<Artist[]> {
    const token = sessionStorage.getItem('jhi-authenticationToken')?.slice(1, -1);
    const headers: Headers = new Headers();
    headers.set('Authorization', 'Bearer ' + token);
    const request: RequestInfo = new Request(`/api/top-artists-${timeRange}`, {
      method: 'GET',
      headers: headers,
    });

    return fetch(request)
      .then(response => response.json())
      .then((data: { graphData: Artist[]; stats: any }) => {
        if (!data.graphData || data.graphData.length === 0) {
          throw new Error('No artist data found');
        }

        this.topArtistName = this.truncateText(data.stats.topArtistName, 7);
        this.topArtistImage = data.stats.topArtistImage;

        this.topTrackName = this.truncateText(data.stats.topTrackByTopArtist.trackName, 25);
        this.topTrackPreviewUrl = data.stats.topTrackByTopArtist.previewUrl;

        this.isPlaying = false;
        this.currentAudio = null;

        this.topGenre = this.truncateText(data.stats.topGenre, 25);

        this.averagePopularity = data.stats.averagePopularity;

        const tasteCategoryDetails = data.stats.tasteCategory;
        if (tasteCategoryDetails) {
          this.tasteCategoryDetails = {
            name: tasteCategoryDetails.name,
            colorDark: tasteCategoryDetails.colorDark,
            colorLight: tasteCategoryDetails.colorLight,
          };
        }

        return data.graphData;
      })
      .catch(error => {
        console.error('Error fetching top artists:', error);
        throw error;
      });
  }

  truncateText(text: string, maxLength: number): string {
    return text.length > maxLength ? text.substring(0, maxLength) + '…' : text;
  }

  playPreview() {
    if (this.topTrackPreviewUrl) {
      if (!this.currentAudio) {
        this.currentAudio = new Audio(this.topTrackPreviewUrl);
        this.currentAudio.onended = () => (this.isPlaying = false);
        this.currentAudio.volume = 0.3;
      }

      if (this.currentAudio) {
        if (this.isPlaying) {
          this.currentAudio.pause();
        } else {
          this.currentAudio.play().catch(error => console.error('Playback failed', error));
        }
        this.isPlaying = !this.isPlaying;
      }
    }
  }

  fetchUserImage(): Promise<any> {
    const token = sessionStorage.getItem('jhi-authenticationToken')?.slice(1, -1);

    const headers: Headers = new Headers();
    headers.set('Authorization', 'Bearer ' + token);

    const request: RequestInfo = new Request('/api/get-user-details', {
      method: 'GET',
      headers: headers,
    });

    return fetch(request)
      .then(response => response.json())
      .then(data => {
        if (data.images && data.images.length > 0) {
          return data.images[1].url;
        } else {
          return 'https://www.clipartkey.com/mpngs/m/152-1520367_user-profile-default-image-png-clipart-png-download.png';
        }
      })
      .catch(error => console.error('Error fetching user details:', error));
  }

  toggleConnections(event?: Event): void {
    const checkboxChecked = event ? (event.target as HTMLInputElement).checked : this.connectionsCheckbox.nativeElement.checked;

    clearGraph(this.graphContainer.nativeElement);
    this.renderGraphBasedOnConnections(checkboxChecked);
  }

  artistConnections: { [key: string]: string[] } = {
    Future: ['Drake', '21 Savage'],
    Drake: ['Future', 'J. Cole'],
    'Neck Deep': ['blink-182'],
    Metallica: ['Slipknot', 'Avenged Sevenfold'],
    'J. Cole': ['Drake'],
    Eminem: ['Dr. Dre'],
  };

  async renderGraphBasedOnConnections(includeConnections: boolean): Promise<void> {
    try {
      const containerWidth = this.graphContainer.nativeElement.offsetWidth;
      const containerHeight = this.graphContainer.nativeElement.offsetHeight;

      const userImageUrl = await this.fetchUserImage();
      const artistsData = await this.fetchTopArtists(this.timeRange);

      const elements = getElements(artistsData, userImageUrl, includeConnections ? this.artistConnections : {});
      renderGraph(this.graphContainer.nativeElement, containerWidth, containerHeight, elements.nodes, elements.links);
    } catch (error) {
      console.error('Error rendering graph:', error);
    }
  }

  private async fetchAndRenderGraph(timeRange: string, includeConnections: boolean = false): Promise<void> {
    clearGraph(this.graphContainer.nativeElement);
    try {
      const containerWidth = this.graphContainer.nativeElement.offsetWidth;
      const containerHeight = this.graphContainer.nativeElement.offsetHeight;

      const userImageUrl = await this.fetchUserImage();
      const artistsData = await this.fetchTopArtists(timeRange);

      const elements = getElements(artistsData, userImageUrl, includeConnections ? this.artistConnections : {});
      this.animateScore(parseFloat(this.averagePopularity));
      renderGraph(this.graphContainer.nativeElement, containerWidth, containerHeight, elements.nodes, elements.links);
    } catch (error) {
      console.error('Error fetching and rendering graph:', error);
    }
  }
}
