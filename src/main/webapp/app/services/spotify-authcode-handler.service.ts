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
    // Get the current URL
    const currentUrl = window.location.href;

    // Include the current URL in the request body along with the code
    return this.http.post<any>('/api/spotify/exchange-code', { code, currentUrl }).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('Error sending authorization code:', error);
        return throwError(error);
      })
    );
  }
}
