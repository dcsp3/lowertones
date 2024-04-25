import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { ApplicationConfigService } from 'app/core/config/application-config.service';

@Injectable({
  providedIn: 'root',
})
export class PreferencesService {
  private userResourceUrl = this.applicationConfigService.getEndpointFor('api/account/preferences');

  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  getAppUser(): Observable<IAppUser> {
    return this.http.post<IAppUser>('/api/account/preferences', {});
  }

  getHighContrast(): Observable<boolean> {
    return this.http.post<boolean>('/api/account/preferences/highContrast', {});
  }

  getTextSize(): Observable<number> {
    return this.http.post<number>('/api/account/preferences/textSize', {});
  }

  updateEmail(email: string): Observable<void> {
    const params = new HttpParams().set('email', email); // Constructing the request parameters with the email
    return this.http.post<void>(`/api/account/update-email`, null, { params });
  }

  deleteCurrentUser(login: string): Observable<{}> {
    return this.http.delete(`${this.userResourceUrl}/${login}`);
  }

  signOutAllDevices(login: string): Observable<void> {
    return this.http.post<void>('/api/account/preferences/' + login, {});
  }
}
