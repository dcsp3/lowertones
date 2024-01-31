import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-recapped',
  templateUrl: './recapped.component.html',
  styleUrls: ['./recapped.component.scss'],
})
export class RecappedComponent implements OnInit {
  selectedMusicianType: string = 'Producers'; // Default to prdoucers
  musicianTypes: string[] = ['Producers', 'Singers', 'Guitarists', 'Bassists', 'Drummers'];
  selectedTimeRange: string = 'lastMonth'; // Set the default time range to last Month
  topMusicians: any[] = [];
  timeframeLabel: string = 'Month'; // Default label

  constructor() {}

  ngOnInit(): void {
    this.loadTopMusicians();
  }

  loadTopMusicians(): void {
    // Implement the logic to fetch top musicians based on selectedMusicianType and time range
    // may want to call service to fetch data from the backend
    // Update this.topMusicians with the fetched data
    // For example, can make an HTTP request to Spring Boot backend
    // using Angular HttpClient to get data from API endpoint.
    // Update timeframeLabel based on the selectedTimeRange
    this.updateTimeframeLabel();
  }

  selectTimeRange(timeRange: string): void {
    // Set the selected time range
    this.selectedTimeRange = timeRange;

    // Implement logic to set the start and end date based on the selected time range
    // Update this.startDate and this.endDate accordingly
    // Then call loadTopMusicians to fetch data for the updated time range
    this.loadTopMusicians();
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
