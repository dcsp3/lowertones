import { Component, OnInit } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';

@Component({
  selector: 'jhi-link-spotify',
  templateUrl: './link-spotify.component.html',
  styleUrls: ['./link-spotify.component.scss'],
})
export class LinkSpotifyComponent implements OnInit {
  constructor(private cookieService: CookieService) {}

  ngOnInit(): void {}

  loginWithSpotify(event: MouseEvent): void {
    console.log('Login Event Intialised');
    event.stopPropagation();
    const client_id = '668d334388b04520ba9e25b3d2289e78';
    const redirect_uri = window.location.origin;
    const state = this.generateRandomString(16);
    const scope =
      'playlist-modify-private playlist-read-collaborative playlist-read-private playlist-modify-public user-top-read user-read-recently-played user-library-modify user-library-read user-read-email user-read-private';
    const url =
      'https://accounts.spotify.com/authorize?' +
      new URLSearchParams({
        response_type: 'code',
        client_id: client_id,
        scope: scope,
        redirect_uri: redirect_uri,
        state: state,
      });
    window.location.href = url.toString();
    // Check if the URL contains the authorization code immediately after initiating the Spotify login
  }
  isLoggedIn(): boolean {
    const accessToken = this.cookieService.get('access_token');
    if (accessToken != '') {
      console.log('Access Token Present:', accessToken);
    }
    return !!accessToken;
  }
  generateRandomString(length: number): string {
    const charset = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
    let randomString = '';
    for (let i = 0; i < length; i++) {
      const randomIndex = Math.floor(Math.random() * charset.length);
      randomString += charset[randomIndex];
    }
    return randomString;
  }
}
