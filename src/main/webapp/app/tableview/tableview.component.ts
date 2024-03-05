import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-tableview',
  templateUrl: './tableview.component.html',
  styleUrls: ['./tableview.component.scss'],
})
export class TableviewComponent implements OnInit {
  songData: any[];

  constructor() {
    this.songData = new Array(100);
  }

  ngOnInit(): void {
    this.songData.fill({ selected: false, title: 'lorem', artist: 'ipsum', length: '50s', genre: 'test', release: 2000 }, 0, 15);
  }
}
