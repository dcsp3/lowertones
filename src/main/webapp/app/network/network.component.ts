import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { clearGraph, getElements, renderGraph } from './topArtistsGraph';

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
  topGenre: string = '';

  ngOnInit(): void {
    this.fetchAndRenderGraph(this.timeRange); // Fetch and render graph on component init
  }

  fetchTopArtists(timeRange: string): Promise<any> {
    const token = sessionStorage.getItem('jhi-authenticationToken')?.slice(1, -1); // Adjust as necessary for token format
    const headers: Headers = new Headers();
    headers.set('Authorization', 'Bearer ' + token);
    // Adjust the request URL to your local endpoint that handles the top artists
    const request: RequestInfo = new Request('/api/top-artists-' + timeRange, {
      method: 'GET',
      headers: headers,
    });

    return fetch(request)
      .then(response => response.json())
      .then(data => {
        const topArtist = data[0]; // Get the first artist in the list
        this.topArtistName = topArtist[1]; // Artist's name
        this.topArtistImage = topArtist[4]; // Artist's image URL

        // Assuming each artist's genres are now included in the data and is a list of strings
        let genreCounts = new Map<string, number>();
        data.forEach((artist: any) => {
          artist[3].forEach((genre: string) => {
            genreCounts.set(genre, (genreCounts.get(genre) || 0) + 1);
          });
        });

        // Determine the top genre
        let topGenreCount = 0;
        genreCounts.forEach((count, genre) => {
          if (count > topGenreCount) {
            topGenreCount = count;
            this.topGenre = genre;
          }
        });

        return data;
      })
      .catch(error => console.error('Error fetching top artists:', error));
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
    // Only change the time range if the new time range isn't the same as the current time range (to avoid unnecessary API calls)
    if (newTimeRange !== this.timeRange) {
      this.timeRange = newTimeRange;
      // console.log(`New Time Range: ${this.timeRange}`);
      this.fetchAndRenderGraph(this.timeRange);
    } else {
      return; // Exit early if the time range hasn't changed
    }
  }

  private async fetchAndRenderGraph(timeRange: string): Promise<void> {
    try {
      // Clear the existing graph
      clearGraph(this.graphContainer.nativeElement);

      // Get user image
      const userImageUrl = await this.fetchUserImage();

      // Fetch new data
      const data = await this.fetchTopArtists(timeRange);
      const elements = getElements(data, userImageUrl);

      renderGraph(this.graphContainer.nativeElement, 850, 600, elements.nodes, elements.links);
    } catch (error) {
      console.error('Error fetching and rendering graph:', error);
    }
  }
}
