import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContributor } from '../contributor.model';

@Component({
  selector: 'jhi-contributor-detail',
  templateUrl: './contributor-detail.component.html',
})
export class ContributorDetailComponent implements OnInit {
  contributor: IContributor | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contributor }) => {
      this.contributor = contributor;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
