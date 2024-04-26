import { Component, OnInit, ElementRef, ViewChild, Injectable } from '@angular/core';
import * as d3 from 'd3';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { choice } from '../recapped/models';

// takes the data from the backend VisualisationsDTO
interface VisualisationsDTO {
  numOfSongs: number;
  topArtist1Count: number;
  topArtist2Count: number;
  topArtist3Count: number;
  topArtist4Count: number;
  topArtist5Count: number;

  topArtist1Name: string;
  topArtist2Name: string;
  topArtist3Name: string;
  topArtist4Name: string;
  topArtist5Name: string;

  topGenre1Percent: number;
  topGenre2Percent: number;
  topGenre3Percent: number;
  topGenre4Percent: number;
  topGenre5Percent: number;

  topGenre1Name: string;
  topGenre2Name: string;
  topGenre3Name: string;
  topGenre4Name: string;
  topGenre5Name: string;

  // variables for the features of the users liked songs
  avgPopularity: number;
  avgDanceability: number;
  avgEnergy: number;
  avgAcousticness: number;
  avgTempo: number;
}

interface Playlist {
  name: string;
  spotifyId: string;
  snapshotId: string;
}

@Injectable({
  providedIn: 'root',
})
export class VisualisationsService {
  private apiUrl = '/api/visualisations';
  constructor(private http: HttpClient) {}

  getVisualisations(playlistId: string): Observable<VisualisationsDTO> {
    return this.http.post<VisualisationsDTO>(this.apiUrl, playlistId);
  }
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
  selector: 'jhi-visualisations',
  templateUrl: './visualisations.component.html',
  styleUrls: ['./visualisations.component.scss'],
})
export class VisualisationsComponent implements OnInit {
  scanType: choice[];
  selectedScanType: string = '';
  @ViewChild('pieChartSvg', { static: true }) private pieChartSvg!: ElementRef;
  @ViewChild('barChart1Svg', { static: true }) private barChart1Svg!: ElementRef;
  @ViewChild('barChart2Svg', { static: true }) private barChart2Svg!: ElementRef;

  constructor(private visualisationsService: VisualisationsService, private playlistService: PlaylistService) {
    this.scanType = [
      { label: 'My Entire Library', value: 'entireLibrary' },
      { label: 'My Top Songs', value: 'topSongs' },
    ];
  }

  response?: VisualisationsDTO;
  ngOnInit() {
    this.fetchPlaylists();
  }

  getVisualisations(arg: string) {
    console.log('Getting visualisation');
    this.visualisationsService.getVisualisations(arg).subscribe({
      next: response => {
        console.log('Response:', response);
        if (response) {
          const formattedResponse: VisualisationsDTO = {
            numOfSongs: response.numOfSongs,
            topArtist1Count: response.topArtist1Count,
            topArtist2Count: response.topArtist2Count,
            topArtist3Count: response.topArtist3Count,
            topArtist4Count: response.topArtist4Count,
            topArtist5Count: response.topArtist5Count,
            topArtist1Name: response.topArtist1Name || '',
            topArtist2Name: response.topArtist2Name || '',
            topArtist3Name: response.topArtist3Name || '',
            topArtist4Name: response.topArtist4Name || '',
            topArtist5Name: response.topArtist5Name || '',
            topGenre1Percent: response.topGenre1Percent,
            topGenre2Percent: response.topGenre2Percent,
            topGenre3Percent: response.topGenre3Percent,
            topGenre4Percent: response.topGenre4Percent,
            topGenre5Percent: response.topGenre5Percent,
            topGenre1Name: response.topGenre1Name,
            topGenre2Name: response.topGenre2Name,
            topGenre3Name: response.topGenre3Name,
            topGenre4Name: response.topGenre4Name,
            topGenre5Name: response.topGenre5Name,
            avgPopularity: response.avgPopularity,
            avgDanceability: response.avgDanceability,
            avgEnergy: response.avgEnergy,
            avgAcousticness: response.avgAcousticness,
            avgTempo: response.avgTempo,
          };

          this.response = formattedResponse;
          console.log('Formatted RESPONSE HERE:', this.response);

          console.log('RESPONSE HERE:', this.response);
          this.createPieChart();
          this.createBarChart1();
          this.createBarChart2();
        }
      },
      error: error => {
        console.error('Error fetching visualisations:', error);
      },
    });
  }

  onScanTypeChange(event: any) {
    console.log('Selected scan type:', event.value.value);
    this.getVisualisations(event.value.value);
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

  private createPieChart() {
    if (this.response) {
      const data = [
        this.response?.topGenre1Percent,
        this.response?.topGenre4Percent,
        this.response?.topGenre3Percent,
        this.response?.topGenre2Percent,
        this.response?.topGenre1Percent,
      ];

      const width = 300;
      const height = 300;
      const radius = Math.min(width, height) / 2;

      const color = d3
        .scaleOrdinal()
        .domain(data.map((d, i) => i.toString()))
        .range(d3.schemeCategory10);

      const svg = d3
        .select(this.pieChartSvg.nativeElement)
        .append('svg')
        .attr('width', width)
        .attr('height', height)
        .append('g')
        .attr('transform', `translate(${width / 2},${height / 2})`);

      const pie = d3
        .pie()
        .value((d: any) => d)
        .sort(null);

      const path = d3
        .arc()
        .outerRadius(radius - 10)
        .innerRadius(0);

      const arc = svg.selectAll('.arc').data(pie(data)).enter().append('g').attr('class', 'arc');

      arc
        .append('path')
        .attr('d', (d: any) => path(d))
        .attr('fill', (d: any, i: any) => color(i.toString()) as string);

      arc
        .append('text')
        .attr('transform', (d: any) => `translate(${path.centroid(d)})`)
        .attr('dy', '0.35em')
        .text((d: any) => d.data);
    }
  }

  private createBarChart1() {
    if (this.response) {
      const data = [
        { name: this.response.topArtist1Name, count: this.response.topArtist1Count },
        { name: this.response.topArtist2Name, count: this.response.topArtist2Count },
        { name: this.response.topArtist3Name, count: this.response.topArtist3Count },
        { name: this.response.topArtist4Name, count: this.response.topArtist4Count },
        { name: this.response.topArtist5Name, count: this.response.topArtist5Count },
      ];

      const svg = d3.select(this.barChart1Svg.nativeElement);
      const width = 350;
      const height = 350;
      const margin = { top: 20, right: 20, bottom: 50, left: 40 };
      const chartWidth = width - margin.left - margin.right;
      const chartHeight = height - margin.top - margin.bottom;

      const x = d3.scaleBand().range([0, chartWidth]).padding(0.1);
      const y = d3.scaleLinear().range([chartHeight, 0]);

      const xAxis = d3.axisBottom(x);
      const yAxis = d3.axisLeft(y);

      x.domain(data.map(d => d.name));
      y.domain([0, d3.max(data, d => d.count) || 0]);

      svg
        .append('g')
        .attr('transform', `translate(${margin.left},${margin.top + chartHeight})`)
        .call(xAxis)
        .selectAll('text')
        .style('text-anchor', 'end')
        .attr('transform', 'rotate(-45)');

      svg.append('g').attr('transform', `translate(${margin.left},${margin.top})`).call(yAxis);

      svg
        .selectAll('.bar')
        .data(data)
        .enter()
        .append('rect')
        .attr('class', 'bar')
        .attr('x', d => x(d.name)! + margin.left)
        .attr('y', d => y(d.count) + margin.top)
        .attr('width', x.bandwidth())
        .attr('height', d => chartHeight - y(d.count))
        .attr('fill', 'steelblue');
    }
  }

  private createBarChart2() {
    if (this.response) {
      const data = [
        { feature: 'Energy', value: this.response.avgEnergy },
        { feature: 'Danceability', value: this.response.avgDanceability },
        { feature: 'Acousticness', value: this.response.avgAcousticness },
      ];
      console.log(
        'Data values:',
        data.map(d => d.value)
      );

      const svg = d3.select(this.barChart2Svg.nativeElement);
      const width = 350;
      const height = 350;
      const margin = { top: 20, right: 20, bottom: 50, left: 40 };
      const chartWidth = width - margin.left - margin.right;
      const chartHeight = height - margin.top - margin.bottom;

      const x = d3.scaleBand().range([0, chartWidth]).padding(0.1);
      const y = d3.scaleLinear().range([chartHeight, 0]);

      const xAxis = d3.axisBottom(x);
      const yAxis = d3.axisLeft(y);

      x.domain(data.map(d => d.feature));
      y.domain([0, d3.max(data, d => d.value) || 0]);

      svg
        .append('g')
        .attr('transform', `translate(${margin.left},${margin.top + chartHeight})`)
        .call(xAxis)
        .selectAll('text')
        .style('text-anchor', 'end')
        .attr('transform', 'rotate(-45)');

      svg.append('g').attr('transform', `translate(${margin.left},${margin.top})`).call(yAxis);

      svg
        .selectAll('.bar')
        .data(data)
        .enter()
        .append('rect')
        .attr('class', 'bar')
        .attr('x', d => x(d.feature)! + margin.left)
        .attr('y', d => y(d.value) + margin.top)
        .attr('width', x.bandwidth())
        .attr('height', d => chartHeight - y(d.value))
        .attr('fill', 'steelblue');
    }
  }

  //{ feature: 'Popularity', value: this.response.avgPopularity },
  //{ feature: 'Tempo', value: this.response.avgTempo },
}
