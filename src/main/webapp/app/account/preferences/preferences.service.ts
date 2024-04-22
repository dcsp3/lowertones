import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
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

  deleteCurrentUser(login: string): Observable<{}> {
    return this.http.delete(`${this.userResourceUrl}/${login}`);
  }
}
