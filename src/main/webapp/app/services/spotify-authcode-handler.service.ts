import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class SpotifyAuthcodeHandlerService {
  constructor(private http: HttpClient) {}

  sendAuthorizationCode(code: string) {
    return this.http.post<any>('/api/spotify/exchange-code', { code });
  }
}
