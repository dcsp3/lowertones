import { renderGraph } from './topArtistsGraph'; // Adjust the path based on your project structure
import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';

@Component({
  selector: 'jhi-network',
  templateUrl: './network.component.html',
  styleUrls: ['./network.component.scss'],
})
export class NetworkComponent implements OnInit {
  @ViewChild('graphContainer', { static: true }) graphContainer!: ElementRef;

  constructor() {}

  ngOnInit(): void {
    // Call the function to create D3.js graph
    renderGraph(this.graphContainer.nativeElement, 750, 500);
  }
}
