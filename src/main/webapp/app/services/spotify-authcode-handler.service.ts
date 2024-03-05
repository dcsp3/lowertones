import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SpotifyAuthcodeHandlerService {
  constructor(private http: HttpClient) {}

  sendAuthorizationCode(code: string) {
    let currentUrl = window.location.href.split('?')[0];
    // Remove trailing slash if present
    if (currentUrl.endsWith('/')) {
      currentUrl = currentUrl.slice(0, -1);
    }
    return this.http.post<any>('/api/spotify/exchange-code', { code, url: currentUrl }).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('Error sending authorization code:', error);
        return throwError(error);
      })
    );
  }
}
