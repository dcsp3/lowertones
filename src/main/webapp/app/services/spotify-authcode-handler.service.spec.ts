import { TestBed } from '@angular/core/testing';

import { SpotifyAuthcodeHandlerService } from './spotify-authcode-handler.service';

describe('SpotifyAuthcodeHandlerService', () => {
  let service: SpotifyAuthcodeHandlerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SpotifyAuthcodeHandlerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
