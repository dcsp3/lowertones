import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-visualisations',
  templateUrl: './visualisations.component.html',
  styleUrls: ['./visualisations.component.scss'],
})
export class VisualisationsComponent implements OnInit {
  constructor() {}

  // method to perform api call to get top mid term artists - refer to testMedTermTopArtists.json to see the response

  // TODO: implement html, css, ts for the top artists genre pie chart
  // You'll be getting the data from the backend from a GET request to /api/top-artists-medium-term
  // the response will be the same format as testMedTermTopArtists.json but may differ from user to user

  // GET request to /api/top-artists-medium-term
  // response will be in the format of testMedTermTopArtists.json

  ngOnInit(): void {}
}
