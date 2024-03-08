import { TestBed } from '@angular/core/testing';

import { IsLinkedService } from './is-linked.service';

describe('IsLinkedService', () => {
  let service: IsLinkedService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IsLinkedService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
