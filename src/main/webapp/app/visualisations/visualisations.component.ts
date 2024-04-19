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
  @ViewChild('pieChartSvg', { static: true }) private pieChartSvg!: ElementRef;
  @ViewChild('barChart1Svg', { static: true }) private barChart1Svg!: ElementRef;
  @ViewChild('barChart2Svg', { static: true }) private barChart2Svg!: ElementRef;

  constructor(private visualisationsService: VisualisationsService) {}

  ngOnInit() {
    this.createPieChart();
    this.createBarChart1();
    this.createBarChart2();

    this.visualisationsService.getShortTermArtists().subscribe({
      next: response => {
        this.response = response;
      },
    });
  }

  private createPieChart() {
    const data = [10, 20, 30, 40, 50];

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

  private createBarChart1() {
    const data: number[] = [30, 40, 50, 60, 70];
    const color = d3
      .scaleOrdinal<number, string>()
      .domain(data.map((d, i) => i))
      .range(d3.schemeCategory10);

    const svg = d3.select(this.barChart1Svg.nativeElement);
    const width = 350;
    const height = 350;
    const margin = { top: 20, right: 20, bottom: 30, left: 40 };
    const chartWidth = width - margin.left - margin.right;
    const chartHeight = height - margin.top - margin.bottom;

    const x = d3.scaleBand<number>().range([0, chartWidth]).padding(0.1);

    const y = d3.scaleLinear().range([chartHeight, 0]);

    const xAxis = d3.axisBottom(x);
    const yAxis = d3.axisLeft(y);

    const g = svg.append('g').attr('transform', 'translate(' + margin.left + ',' + margin.top + ')');

    x.domain(data.map((d, i) => i));
    y.domain([0, d3.max(data) || 0]);

    g.append('g')
      .attr('class', 'x-axis')
      .attr('transform', 'translate(0,' + chartHeight + ')')
      .call(xAxis);

    g.append('g').attr('class', 'y-axis').call(yAxis);

    g.selectAll('.bar')
      .data(data)
      .enter()
      .append('rect')
      .attr('class', 'bar')
      .attr('x', (d, i): number => x(i) || 0)
      .attr('y', (d): number => y(d) || 0)
      .attr('width', x.bandwidth())
      .attr('height', (d): number => chartHeight - (y(d) || 0))
      .attr('fill', (d, i): string => color(i));
  }

  private createBarChart2() {
    const data: number[] = [20, 50, 30, 40, 60];
    const color = d3
      .scaleOrdinal<number, string>()
      .domain(data.map((d, i) => i))
      .range(d3.schemeCategory10);

    const svg = d3.select(this.barChart2Svg.nativeElement);
    const width = 350;
    const height = 350;
    const margin = { top: 20, right: 20, bottom: 30, left: 40 };
    const chartWidth = width - margin.left - margin.right;
    const chartHeight = height - margin.top - margin.bottom;

    const x = d3.scaleBand<number>().range([0, chartWidth]).padding(0.1);

    const y = d3.scaleLinear().range([chartHeight, 0]);

    const xAxis = d3.axisBottom(x);
    const yAxis = d3.axisLeft(y);

    const g = svg.append('g').attr('transform', 'translate(' + margin.left + ',' + margin.top + ')');

    x.domain(data.map((d, i) => i));
    y.domain([0, d3.max(data) || 0]);

    g.append('g')
      .attr('class', 'x-axis')
      .attr('transform', 'translate(0,' + chartHeight + ')')
      .call(xAxis);

    g.append('g').attr('class', 'y-axis').call(yAxis);

    g.selectAll('.bar')
      .data(data)
      .enter()
      .append('rect')
      .attr('class', 'bar')
      .attr('x', (d, i): number => x(i) || 0)
      .attr('y', (d): number => y(d) || 0)
      .attr('width', x.bandwidth())
      .attr('height', (d): number => chartHeight - (y(d) || 0))
      .attr('fill', (d, i): string => color(i));
  }
}
