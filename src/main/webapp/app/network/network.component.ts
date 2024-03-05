import { CookieService } from 'ngx-cookie-service';
import { fetchData, renderGraph, clearGraph } from './topArtistsGraph';
import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';

@Component({
  selector: 'jhi-network',
  templateUrl: './network.component.html',
  styleUrls: ['./network.component.scss'],
})
export class NetworkComponent implements OnInit {
  @ViewChild('graphContainer', { static: true }) graphContainer!: ElementRef;

  timeRange: string = 'short_term'; // Default time range

  constructor(private cookieService: CookieService) {}

  ngOnInit(): void {
    this.fetchAndRenderGraph();
  }

  changeTimeRange(newTimeRange: string): void {
    // Only change the time range if the new time range isn't the same as the current time range (to avoid unnecessary API calls)
    if (newTimeRange !== this.timeRange) {
      this.timeRange = newTimeRange;
      console.log(`New Time Range: ${this.timeRange}`);
      this.fetchAndRenderGraph();
    } else {
      return; // Exit early if the time range hasn't changed
    }
  }

  private async fetchAndRenderGraph(): Promise<void> {
    const accessToken = this.cookieService.get('access_token');
    const refreshToken = this.cookieService.get('refresh_token');

    try {
      // Clear the existing graph
      clearGraph(this.graphContainer.nativeElement);

      // Fetch new data
      const elements = await fetchData(this.timeRange, accessToken, refreshToken);

      // Render the new graph with updated data
      renderGraph(this.graphContainer.nativeElement, 750, 500, elements.nodes, elements.links);
    } catch (error) {
      console.error('Error fetching and rendering graph:', error);
    }
  }
}
