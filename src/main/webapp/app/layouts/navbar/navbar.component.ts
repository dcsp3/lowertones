import { Component, OnInit, OnDestroy } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';

import { VERSION } from 'app/app.constants';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/login/login.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { EntityNavbarItems } from 'app/entities/entity-navbar-items';
import { HttpClient } from '@angular/common/http';

//custom services go here
import { SpotifyAuthcodeHandlerService } from '../../services/spotify-authcode-handler.service';
import { LocationService } from '../../shared/location.service';

@Component({
  selector: 'jhi-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit, OnDestroy {
  currentTab: string | null = null;
  private tabSubscription: Subscription = new Subscription();
  inProduction?: boolean;
  isNavbarCollapsed = true;
  openAPIEnabled?: boolean;
  version = '';
  account: Account | null = null;
  entitiesNavbarItems: any[] = [];
  currentSection: string | null = null;

  constructor(
    private locationService: LocationService,
    private cookieService: CookieService,
    private loginService: LoginService,
    private accountService: AccountService,
    private profileService: ProfileService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private http: HttpClient,
    private spotifyAuthCodeHandler: SpotifyAuthcodeHandlerService
  ) {
    if (VERSION) {
      this.version = VERSION.toLowerCase().startsWith('v') ? VERSION : `v${VERSION}`;
    }
  }

  ngOnInit(): void {
    this.entitiesNavbarItems = EntityNavbarItems;
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });
    this.tabSubscription = this.locationService.getCurrentTab().subscribe(sectionId => {
      this.currentSection = sectionId;
    });
    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
      console.log('User Authorities:', account?.authorities);
      console.log('Has Any Authority:', this.hasAnyAuthority(['ADMIN', 'USER']));
    });
    this.activatedRoute.queryParams.subscribe(params => {
      const code = params['code'];
      if (code) {
        this.cookieService.set('code', code);
        this.router.navigate(['/']);
        this.spotifyAuthCodeHandler.sendAuthorizationCode(code).subscribe(
          response => {
            console.log('Received access token:', response);
            const accessExpirationDate = new Date();
            accessExpirationDate.setSeconds(accessExpirationDate.getSeconds() + 3600);
            const refreshExpirationDate = new Date();
            refreshExpirationDate.setSeconds(refreshExpirationDate.getDay() + 3600 * 24 * 30);
            this.cookieService.set('access_token', response.access_token, accessExpirationDate);
            this.cookieService.set('refresh_token', response.refresh_token, refreshExpirationDate);
          },
          error => {
            console.error('Error sending authorization code:', error);
          }
        );
      }
    });
  }

  collapseNavbar(): void {
    this.isNavbarCollapsed = true;
  }

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

  hasAnyAuthority(authorities: string[]): boolean {
    return this.account?.authorities?.some(authority => authorities.includes(authority)) || false;
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  logout(): void {
    this.collapseNavbar();
    this.loginService.logout();
    this.router.navigate(['']);
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }

  performSearch(event: any): void {
    if (event.key === 'Enter') {
      // Add search logic here
      const searchTerm = (event.target as HTMLInputElement).value;
      console.log(`Performing search for: ${searchTerm}`);
      // Trigger search function here
    }
  }

  navigateIfAuthenticated(targetRoute: string, sectionId: string): void {
    if (this.hasAnyAuthority !== null) {
      this.currentSection = sectionId;
      this.router.navigate([targetRoute]);
      this.collapseNavbar();
    } else {
      this.login();
    }
  }

  ngOnDestroy(): void {
    if (this.tabSubscription) {
      this.tabSubscription.unsubscribe();
    }
  }
}
