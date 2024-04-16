import { Component, OnInit, ElementRef, ViewChild, Injectable } from '@angular/core';
import * as d3 from 'd3';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// takes the data from the backend VisualisationsDTO
interface VisualisationsDTO {
  topArtist1Percent: number;
  topArtist2Percent: number;
  topArtist3Percent: number;
  topArtist4Percent: number;
  topArtist5Percent: number;
  topArtist6Percent: number;

  topArtist1Name: String;
  topArtist2Name: String;
  topArtist3Name: String;
  topArtist4Name: String;
  topArtist5Name: String;
  topArtist6Name: String;
}

@Injectable({
  providedIn: 'root',
})
export class VisualisationsService {
  private apiUrl = '/api/visualisations';
  constructor(private http: HttpClient) {}
  getShortTermArtists(): Observable<VisualisationsDTO> {
    return this.http.get<VisualisationsDTO>(this.apiUrl);
  }
}

@Component({
  selector: 'jhi-visualisations',
  templateUrl: './visualisations.component.html',
  styleUrls: ['./visualisations.component.scss'],
})
export class VisualisationsComponent implements OnInit {
  response: any;
  @ViewChild('chartSvg', { static: true }) private chartSvg!: ElementRef;

  constructor(private visualisationsService: VisualisationsService) {}

  ngOnInit() {
    this.createPieChart();
    this.visualisationsService.getShortTermArtists().subscribe({
      next: response => {
        this.response = response;
      },
    });
    console.log('RESPONSE', this.response);
  }

  private createPieChart() {
    const data = [10, 20, 30, 40, 50];

    const width = 400;
    const height = 400;
    const radius = Math.min(width, height) / 2;

    const color = d3
      .scaleOrdinal()
      .domain(data.map((d, i) => i.toString()))
      .range(d3.schemeCategory10);

    const svg = d3
      .select(this.chartSvg.nativeElement)
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
      .attr('fill', (d: any, i: any) => color(i.toString()) as string); // Casting to string

    arc
      .append('text')
      .attr('transform', (d: any) => `translate(${path.centroid(d)})`)
      .attr('dy', '0.35em')
      .text((d: any) => d.data);
  }
}
