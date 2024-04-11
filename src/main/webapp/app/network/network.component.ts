import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
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
})
export class NetworkComponent implements OnInit {
  @ViewChild('graphContainer', { static: true }) graphContainer!: ElementRef;

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

  ngOnInit(): void {
    this.fetchAndRenderGraph(this.timeRange);
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
        this.topArtistName = data.stats.topArtistName;
        this.topArtistImage = data.stats.topArtistImage;

        this.topTrackName = data.stats.topTrackByTopArtist.trackName;
        this.topTrackPreviewUrl = data.stats.topTrackByTopArtist.previewUrl;

        // Since we have new top track info, reset current audio and playback status
        this.isPlaying = false;
        this.currentAudio = null;

        this.topGenre = data.stats.topGenre;

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

  playPreview() {
    if (!this.currentAudio) {
      if (this.topTrackPreviewUrl) {
        this.currentAudio = new Audio(this.topTrackPreviewUrl);
        this.currentAudio.onended = () => (this.isPlaying = false); // Update isPlaying when audio ends
        this.currentAudio.volume = 0.025; // Set volume to half
      }
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

  private async fetchAndRenderGraph(timeRange: string): Promise<void> {
    try {
      clearGraph(this.graphContainer.nativeElement);
      const userImageUrl = await this.fetchUserImage();
      const artistsData = await this.fetchTopArtists(timeRange);
      const elements = getElements(artistsData, userImageUrl); // Ensure getElements accepts Artist[] as per the corrected structure
      renderGraph(this.graphContainer.nativeElement, 850, 600, elements.nodes, elements.links);
    } catch (error) {
      console.error('Error fetching and rendering graph:', error);
    }
  }
}
