import { Component, OnInit, ElementRef } from '@angular/core';
import * as d3 from 'd3';

@Component({
  selector: 'jhi-visualisations',
  templateUrl: './visualisations.component.html',
  styleUrls: ['./visualisations.component.scss'],
})
export class VisualisationsComponent implements OnInit {
  constructor(private elementRef: ElementRef) {}

  ngOnInit() {
    this.createPieChart();
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
      .select(this.elementRef.nativeElement)
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

  // method to perform api call to get top mid term artists - refer to testMedTermTopArtists.json to see the response

  // TODO: implement html, css, ts for the top artists genre pie chart
  // You'll be getting the data from the backend from a GET request to /api/top-artists-medium-term
  // the response will be the same format as testMedTermTopArtists.json but may differ from user to user

  // GET request to /api/top-artists-medium-term
  // response will be in the format of testMedTermTopArtists.json
}
