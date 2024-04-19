import { Component, OnInit, ElementRef, ViewChild, HostListener } from '@angular/core';
import { trigger, state, style, transition, animate } from '@angular/animations';

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

interface ApiResponse {
  graphData: Artist[];
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

  ngOnInit(): void {
    this.fetchAndRenderGraph(this.timeRange);
  }

  animateScore(finalScore: number): void {
    this.fillPercentage = finalScore; // Set the final score percentage for animation
    let currentScore = 0;
    const increment = finalScore / 100;
    const interval = setInterval(() => {
      currentScore += increment;
      if (currentScore >= finalScore) {
        currentScore = finalScore;
        clearInterval(interval);
      }
      this.displayScore = currentScore.toFixed(2);
    }, 25); // Adjust timing based on preference
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    clearGraph(this.graphContainer.nativeElement);
    this.fetchAndRenderGraph(this.timeRange); // Re-fetch and re-render the graph on window resize
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

        // Directly use the stats from the response
        this.topArtistName = this.truncateText(data.stats.topArtistName, 7);
        this.topArtistImage = data.stats.topArtistImage;

        this.topTrackName = this.truncateText(data.stats.topTrackByTopArtist.trackName, 25);
        this.topTrackPreviewUrl = data.stats.topTrackByTopArtist.previewUrl;

        // Since we have new top track info, reset current audio and playback status
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
    // If the text is longer than the `maxLength`, truncate it and add an ellipsis.
    return text.length > maxLength ? text.substring(0, maxLength) + '…' : text;
  }

  playPreview() {
    if (this.topTrackPreviewUrl) {
      // Ensure there's a URL before attempting to play
      if (!this.currentAudio) {
        this.currentAudio = new Audio(this.topTrackPreviewUrl);
        this.currentAudio.onended = () => (this.isPlaying = false); // Update isPlaying when audio ends
        this.currentAudio.volume = 0.3;
      }

      if (this.currentAudio) {
        if (this.isPlaying) {
          this.currentAudio.pause();
        } else {
          this.currentAudio.play().catch(error => console.error('Playback failed', error));
        }
        this.isPlaying = !this.isPlaying; // Toggle playback status
      }
    }
  }

  fetchUserImage(): Promise<any> {
    // Get auth token
    const token = sessionStorage.getItem('jhi-authenticationToken')?.slice(1, -1);

    // Set headers for API call
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
          return data.images[1].url; // Get the highest res image
        } else {
          // If no user img present then reutrn default img
          return 'https://www.clipartkey.com/mpngs/m/152-1520367_user-profile-default-image-png-clipart-png-download.png';
        }
      })
      .catch(error => console.error('Error fetching user details:', error));
  }

  changeTimeRange(newTimeRange: string): void {
    if (newTimeRange !== this.timeRange) {
      this.timeRange = newTimeRange;

      // Uncheck the checkbox
      if (this.connectionsCheckbox.nativeElement.checked) {
        this.connectionsCheckbox.nativeElement.checked = false;
        //this.toggleConnections(); // Optionally update graph if necessary
      }

      // Stop and reset the current audio if it's playing
      if (this.currentAudio && !this.currentAudio.paused) {
        this.currentAudio.pause();
        this.currentAudio.currentTime = 0; // Reset audio playback to the start
      }
      // Reset playback status and current audio
      this.isPlaying = false;
      this.currentAudio = null;

      // Fetch new data for the selected time range
      this.fetchAndRenderGraph(this.timeRange);
    }
  }

  toggleConnections(event?: Event): void {
    const checkboxChecked = event ? (event.target as HTMLInputElement).checked : this.connectionsCheckbox.nativeElement.checked;

    clearGraph(this.graphContainer.nativeElement);
    this.renderGraphBasedOnConnections(checkboxChecked);
  }

  artistConnections: { [key: string]: string[] } = {
    Future: ['Drake', '21 Savage'], // Future and Drake, 21 Savage share similar music styles
    Drake: ['Future', 'J. Cole'], // Drake collaborates often with Future and J. Cole
    'Neck Deep': ['blink-182'], // Both are from a similar genre
    Metallica: ['Slipknot', 'Avenged Sevenfold'], // All are metal bands
    'J. Cole': ['Drake'], // Hip-hop artists often collaborate
    Eminem: ['Dr. Dre'], // Not in the provided list but an example of adding influential connections
  };

  async renderGraphBasedOnConnections(includeConnections: boolean): Promise<void> {
    try {
      const containerWidth = this.graphContainer.nativeElement.offsetWidth;
      const containerHeight = this.graphContainer.nativeElement.offsetHeight;

      const userImageUrl = await this.fetchUserImage();
      const artistsData = await this.fetchTopArtists(this.timeRange);
      // Pass the artist connections data along with other parameters
      const elements = getElements(artistsData, userImageUrl, includeConnections ? this.artistConnections : {});
      renderGraph(this.graphContainer.nativeElement, containerWidth, containerHeight, elements.nodes, elements.links);
    } catch (error) {
      console.error('Error rendering graph:', error);
    }
  }

  private async fetchAndRenderGraph(timeRange: string, includeConnections: boolean = false): Promise<void> {
    try {
      clearGraph(this.graphContainer.nativeElement);

      const containerWidth = this.graphContainer.nativeElement.offsetWidth;
      const containerHeight = this.graphContainer.nativeElement.offsetHeight;

      const userImageUrl = await this.fetchUserImage();
      const artistsData = await this.fetchTopArtists(timeRange);
      // Pass the artist connections data along with other parameters
      const elements = getElements(artistsData, userImageUrl, includeConnections ? this.artistConnections : {});
      this.animateScore(parseFloat(this.averagePopularity)); // Call to start animation once data is loaded
      renderGraph(this.graphContainer.nativeElement, containerWidth, containerHeight, elements.nodes, elements.links);
    } catch (error) {
      console.error('Error fetching and rendering graph:', error);
    }
  }
}
